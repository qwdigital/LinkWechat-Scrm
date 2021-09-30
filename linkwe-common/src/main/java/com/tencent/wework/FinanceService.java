package com.tencent.wework;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.common.utils.uuid.IdUtils;
import com.linkwechat.common.utils.wecom.RSAUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author danmo
 * @description
 * @date 2020/12/2 16:01
 **/
@Slf4j
public class FinanceService{

    /**
     * NewSdk返回的sdk指针
     */
    private long sdk = 0;
    /**
     * 超时时间，单位秒
     */
    private final long timeout = 5 * 60;

    /**
     * 企业id
     */
    private String corpId;

    /**
     * 会话存档密钥
     */
    private String secret;

    /**
     * 会话私钥
     */
    private String privateKey;

    /**
     * 代理
     */
    private String proxy;

    /**
     * 代理密码
     */
    private String passwd;

    /**
     * 一次拉取的消息条数，最大值1000条，超过1000条会返回错误
     */
    private final long LIMIT = 1_000L;

    private RuoYiConfig ruoYiConfig;

    public FinanceService(String corpId, String secret, String privateKey){
        this(corpId,secret,privateKey,"","");
    }
    /**
     * 构造器
     * @param corpId 企业id
     * @param secret 会话存档密钥
     * @param privateKey 会话私钥
     */
    public FinanceService(String corpId, String secret, String privateKey, String proxy, String passwd){
        this.corpId = corpId;
        this.secret = secret;
        this.privateKey = privateKey;
        this.proxy = proxy;
        this.passwd = passwd;
        ruoYiConfig = SpringUtils.getBean(RuoYiConfig.class);
        //初始化
        initSDK();
    }

    /**
     * 初始化
     */
    private void initSDK() {
        if (0 == sdk) {
            sdk = Finance.NewSdk();
            Finance.Init(sdk, corpId, secret);
        }
    }

    /**
     * 拉取聊天记录
     *
     * @param seq    消息的seq值，标识消息的序号
     */
    public void getChatData(long seq, Consumer<JSONObject> consumer) {
        long slice = Finance.NewSlice();
        int ret = Finance.GetChatData(sdk, seq, LIMIT, proxy, passwd, timeout, slice);
        if (ret != 0) {
            log.info("getChatData ret " + ret);
            return;
        }
        String content = Finance.GetContentFromSlice(slice);
        JSONArray chatDataArr = JSONObject.parseObject(content).getJSONArray("chatdata");
        log.info("开始执行数据解析:------------");
        AtomicLong LocalSEQ = new AtomicLong();
        if (CollectionUtil.isNotEmpty(chatDataArr)) {
            chatDataArr.stream().map(data -> (JSONObject) data).forEach(data -> {
                LocalSEQ.set(data.getLong("seq"));
                JSONObject jsonObject = decryptChatRecord(sdk, data.getString("encrypt_random_key"), data.getString("encrypt_chat_msg"));
                if (jsonObject != null) {
                    jsonObject.put("seq", LocalSEQ.get());
                    consumer.accept(jsonObject);
                }
            });
            log.info("数据解析完成:------------");
        }
        Finance.FreeSlice(slice);
    }

    /**
     * @param sdk               初始化时候获取到的值
     * @param encryptRandomKey 企业微信返回的随机密钥
     * @param encryptChatMsg  企业微信返回的单条记录的密文消息
     * @return JSONObject 返回不同格式的聊天数据,格式有二十来种
     * 详情请看官网 https://open.work.weixin.qq.com/api/doc/90000/90135/91774#%E6%B6%88%E6%81%AF%E6%A0%BC%E5%BC%8F
     */
    private JSONObject decryptChatRecord(Long sdk, String encryptRandomKey, String encryptChatMsg) {
        Long msg = null;
        try {
            //获取私钥
            PrivateKey privateKeyObj = RSAUtil.getPrivateKey(privateKey);
            String str = RSAUtil.decryptRSA(encryptRandomKey, privateKeyObj);
            //初始化参数slice
            msg = Finance.NewSlice();

            //解密
            Finance.DecryptData(sdk, str, encryptChatMsg, msg);
            String jsonDataStr = Finance.GetContentFromSlice(msg);
            JSONObject realJsonData = JSONObject.parseObject(jsonDataStr);
            String msgType = realJsonData.getString("msgtype");
            if (StringUtils.isNotEmpty(msgType)) {
                getSwitchType(realJsonData, msgType);
            }
            log.info("数据解析:------------" + realJsonData.toJSONString());
            return realJsonData;
        } catch (Exception e) {
            log.error("解析密文失败");
            return null;
        } finally {
            if (msg != null) {
                //释放参数slice
                Finance.FreeSlice(msg);
            }
        }
    }

    private void getSwitchType(JSONObject realJsonData, String msgType) {
        switch (msgType) {
            case "image":
                setMediaImageData(realJsonData, msgType);
                break;
            case "voice":
                setMediaVoiceData(realJsonData, msgType);
                break;
            case "video":
                setMediaVideoData(realJsonData, msgType);
                break;
            case "emotion":
                setMediaEmotionData(realJsonData, msgType);
                break;
            case "file":
                setMediaFileData(realJsonData, msgType);
                break;
            case "mixed":
                setMediaMixedData(realJsonData, msgType);
                break;
            case "meeting_voice_call":
            case "voip_doc_share":
                setMediaMeetingVoiceCallData(realJsonData, msgType);
                break;
            default:
                break;
        }
    }

    private void setMediaMeetingVoiceCallData(JSONObject realJsonData, String msgType) {
        JSONObject meetingVoiceCall = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String fileName = meetingVoiceCall.getString("filename");
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaMixedData(JSONObject realJsonData, String msgType) {
        JSONObject mixedData = realJsonData.getJSONObject(msgType);
        JSONArray items = mixedData.getJSONArray("item");
        items.stream().map(item -> (JSONObject) item).forEach(item -> {
            getSwitchType(item, item.getString("type"));
        });
    }

    private void setMediaFileData(JSONObject realJsonData, String msgType) {
        JSONObject emotionData = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String filename = emotionData.getString("filename");
        //String fileext = emotionData.getString("fileext");
        //String fileName = filename+"."+fileext;
        getPath(realJsonData, msgType, filename);
    }


    private void setMediaImageData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".jpg";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaVoiceData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".amr";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaVideoData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".mp4";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaEmotionData(JSONObject realJsonData, String msgType) {
        String fileName = "";
        JSONObject emotionData = realJsonData.getJSONObject(msgType);
        Integer type = emotionData.getInteger("type");
        switch (type) {
            case 1:
                fileName = IdUtils.simpleUUID() + ".gif";
                break;
            case 2:
                fileName = IdUtils.simpleUUID() + ".png";
                break;
            default:
                break;
        }
        getPath(realJsonData, msgType, fileName);
    }

    private void getPath(JSONObject realJsonData, String msgType, String fileName) {
        String filePath = getFilePath(msgType);
        JSONObject data = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String sdkfileid = data.getString("sdkfileid");
        try {
            getMediaData(sdkfileid, filePath, fileName);
            if(ruoYiConfig.getFile().isStartCosUpload()) {
                //开启云上传开关则云上传，不然上传本地
                InputStream inputStream = new FileInputStream(new File(filePath,fileName));
                String fileFullName = FileUploadUtils.upload2Cos(inputStream, FileUtil.getSuffix(fileName), ruoYiConfig.getFile().getCos());
                String imgUrlPrefix = ruoYiConfig.getFile().getCos().getCosImgUrlPrefix();
                data.put("attachment", imgUrlPrefix + fileFullName);
            }else {
                data.put("attachment", filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (realJsonData.containsKey("content")) {
            realJsonData.put("content", data);
        } else {
            realJsonData.put(msgType, data);
        }

    }

    private String getFilePath(String msgType) {
        return RuoYiConfig.getProfile() + "/" + msgType + "/" + DateUtils.getDate();
    }

    private void getMediaData(String sdkFileId, String filePath, String fileName) {
        String indexbuf = "";
        while (true) {
            long mediaData = Finance.NewMediaData();
            int ret = Finance.GetMediaData(sdk, indexbuf, sdkFileId, proxy, passwd, timeout, mediaData);
            log.info("getMediaData ret:" + ret);
            if (ret != 0) {
                return;
            }
            try {
                File f = new File(filePath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                File file = new File(filePath, fileName);
                if (!file.isDirectory()) {
                    file.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write(Finance.GetData(mediaData));
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Finance.IsMediaDataFinish(mediaData) == 1) {
                Finance.FreeMediaData(mediaData);
                break;
            } else {
                indexbuf = Finance.GetOutIndexBuf(mediaData);
                Finance.FreeMediaData(mediaData);
            }
        }
    }
}
