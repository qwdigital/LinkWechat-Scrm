package com.tencent.wework;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.common.utils.thread.WeMsgAuditThreadExecutor;
import com.linkwechat.common.utils.uuid.IdUtils;
import com.linkwechat.common.utils.wecom.RSAUtil;
import com.linkwechat.fegin.QwFileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PrivateKey;
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


    private RedisService redisService;
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
    private final long LIMIT = 1000L;




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
        //初始化
        initSDK();
    }

    /**
     * 初始化
     */
    private void initSDK() {
        if (0 == sdk) {
            sdk = Finance.NewSdk();
            int ret = Finance.Init(sdk, corpId, secret);
            if(ret != 0){
                Finance.DestroySdk(sdk);
                System.out.println("init sdk err ret " + ret);
                return;
            }
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
        if (CollectionUtil.isNotEmpty(chatDataArr)) {
            redisService.setCacheObject("we:chat:seq:" + corpId,chatDataArr.getJSONObject(chatDataArr.size() -1).getLong("seq"));
            chatDataArr.stream().map(data -> (JSONObject) data).forEach(data -> {
                JSONObject jsonObject = decryptChatRecord(data.getString("encrypt_random_key"), data.getString("encrypt_chat_msg"));
                if (jsonObject != null) {
                    jsonObject.put("seq", data.getLong("seq"));
                    jsonObject.put("corpId",this.corpId);
                    log.info("数据发送消息:------------"+jsonObject.toJSONString());
                    consumer.accept(jsonObject);
                }
            });
            log.info("数据解析完成:------------");
        }
        Finance.FreeSlice(slice);
    }

    /**
     * @param encryptRandomKey 企业微信返回的随机密钥
     * @param encryptChatMsg  企业微信返回的单条记录的密文消息
     * @return JSONObject 返回不同格式的聊天数据,格式有二十来种
     * 详情请看官网 https://open.work.weixin.qq.com/api/doc/90000/90135/91774#%E6%B6%88%E6%81%AF%E6%A0%BC%E5%BC%8F
     */
    private JSONObject decryptChatRecord(String encryptRandomKey, String encryptChatMsg) {
        JSONObject realJsonData = new JSONObject();
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
            log.info("数据解析:------------{}",jsonDataStr);
            realJsonData = JSONObject.parseObject(jsonDataStr);
        } catch (Exception e) {
            log.error("解析密文失败",e);
            realJsonData=null;
        } finally {
            if (msg != null) {
                //释放参数slice
                Finance.FreeSlice(msg);
            }
        }
        return realJsonData;
    }


    public void getMediaData(String sdkFileId, String filePath, String fileName) {
        String indexbuf = "";
        while (true) {
            long mediaData = Finance.NewMediaData();
            int ret = Finance.GetMediaData(sdk, indexbuf, sdkFileId, proxy, passwd, 5 * 60, mediaData);
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

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
