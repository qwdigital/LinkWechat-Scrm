package com.tencent.wework;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.CosConfig;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
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
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author danmo
 * @description
 * @date 2020/12/2 16:01
 **/
@Slf4j
public class FinanceUtils {
    /**
     * NewSdk返回的sdk指针
     */
    private static volatile long sdk = 0;
    /**
     * 超时时间，单位秒
     */
    private final static long timeout = 5 * 60;

    private static String downloadWeWorkPath = RuoYiConfig.getDownloadWeWorkPath();

    /**
     * 初始化
     *
     * @param corpId 企业id
     * @param secret 会话存档密钥
     */
    public static void initSDK(String corpId, String secret) {
        if (sdk == 0) {
            sdk = Finance.NewSdk();
            Finance.Init(sdk, corpId, secret);
        }
    }

    private final static String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEAjGwIQIMGDJonMQdX3OJzZN0YVoYbORK1FU/8vhwdI9ixxSGW\n" +
            "nrAQc6r8k3g1QTXzmKAxdVlAa1OGZCpl5X5A6C9JwDhFpuN+SKJxfxtHNv1oBk0r\n" +
            "LnDftsdlibZFQn3fiMomxKyU8eujWBe31+pLQXaTSdSHcNhb+NrBa7AtNUr/c7bK\n" +
            "nnFUZHDpXFB4Z0BhK1cRh2NN7ppLNnIJXpduGrwGtLdx++LsBaX2vjO4qW/DKP2Z\n" +
            "3gZON1tGoofS1IPhCg0naoRN3lL7ctFAyxOr2ualGOc6hpvBGfjnBKU42aoaKQEi\n" +
            "7tVJHChonZXMRcm4mdPu5371IzPeR3NYlP3ESQIDAQABAoIBAEux8MDV4GzqrDXB\n" +
            "v5tSduHoTTZXFFWvv+29MIFKpmqsZSfe81KXlkbD5WOZaRu9+ZJMyFx5wEAaxlc4\n" +
            "g7UyWcrPsvHrY6CTAYAmEQhzq+/4qhq7fouKTQ46boHNv4gYUmdqkXtzjNbcLzFN\n" +
            "l8zMf8TKJpul9VzfRLThN2LVlYkkETcCukzZrK+aSCi6E/dL+noL1ndZYmWF5kbB\n" +
            "lk881x5CdbusYBtoxfh6ie0EXfCyK4duX3OdvXRcSpTdqiqTcrtt9oinh4ZHV930\n" +
            "BCTPkGGz5UL3C243W3dlzuonNXqwl++FCSyRMVONYSgb4NRJn4fvehjGtXyAv3Sz\n" +
            "nSAVoAECgYEA9dhYk4DT2Hclaqo/0ugQvUp7oIPN9qFgrZ6nU94SHkdrBqhFrCmz\n" +
            "32GeYGSsQJQx3T9Kuip7oGX9AOKn3f30NrcuAe9VzzWAkCKQgQgmtMVySAPWCVr9\n" +
            "4MH77hc3ijxr6iGd70BQ7EDyVQGRvZqOLl1wu7yZ9vhbVHA87KpgHf0CgYEAkjjn\n" +
            "mNudh+rs8ibrGu+md8bXJdxHtIbncXF5VsV7wi4HlHfKnWqy+z2If1akm2iacnsi\n" +
            "XDVqyYNExdtoxmbPc/R9ZJ8jP0FLPoWACcjP03r9H+5eiXrmniZbElGshnwKHFFh\n" +
            "ZlY1xPAXFgEjh10DDG1wK6/9eB13o+NdP7c7yz0CgYEA3HujNOadF84DaJ43j1dB\n" +
            "bAKYzWRoG4CvzAY12ymW5kD244mWssjT4MsT1y9yrJg8AEj7I+tl5HyTY2/jTVys\n" +
            "4UV3pZSYSLWbD6lwH8jHrehGjf+ivdE1T5Wp8+YukXOO9PQhSKlN6hR1QXee1YT/\n" +
            "buWubTDAhJZIknn2qepDOaECgYA/OnITLCwTYOey0ldpn2V5d7cC/RzYmFkuOeZB\n" +
            "OkYIsoS+k4o+xau9bl8+yQWG2hWnGU3DV6nkl2m+sOC+oihkuL0cLKA+MwrJb0rt\n" +
            "cDNF/HHGQ/oHMujUTDunUT/sK2jn09ztNqwri7I/5qApYXP3BL+zdHeDYCKi8Kca\n" +
            "7S3i9QKBgQCgKgQf8u+oRojMH14iJssAyNEhZgmJ85s/BeiUmMuW5K8iZZ5pnVuq\n" +
            "8nc/IJGVPHRTObdGCd1L4d5UERiX46Z1BjYv/nuK5DScDcB3mq6K9VXJLNE+MxwW\n" +
            "wOVT+6FjmBRucGXczAkWXCH7++y6gYzE+OuRIdv5sVkE24gNG7sawg==\n" +
            "-----END RSA PRIVATE KEY-----";

    /**
     * 拉取聊天记录
     *
     * @param seq    消息的seq值，标识消息的序号
     * @param proxy  代理
     * @param passwd 密码
     */
    public static List<JSONObject> getChatData(long seq, String proxy, String passwd, RedisCache redisCache) {
        List<JSONObject> resList = new ArrayList<>();
        long slice = Finance.NewSlice();
        int ret = Finance.GetChatData(sdk, seq, WeConstans.LIMIT, proxy, passwd, timeout, slice);
        if (ret != 0) {
            log.info("getChatData ret " + ret);
            return null;
        }
        String content = Finance.GetContentFromSlice(slice);
        JSONArray chatdataArr = JSONObject.parseObject(content).getJSONArray("chatdata");
        log.info("开始执行数据解析:------------");
        AtomicLong LocalSEQ = new AtomicLong();
        if (CollectionUtil.isNotEmpty(chatdataArr)) {
            chatdataArr.stream().map(data -> (JSONObject) data).forEach(data -> {
                try {
                    LocalSEQ.set(data.getLong("seq"));
                    JSONObject jsonObject = decryptChatRecord(sdk, data.getString("encrypt_random_key"),
                            data.getString("encrypt_chat_msg"), privateKey);
                    if (jsonObject == null) {
                        return;
                    }
                    jsonObject.put("seq", LocalSEQ.get());
                    resList.add(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            log.info("数据解析完成:------------");
        }
        Finance.FreeSlice(slice);
        redisCache.setCacheObject(WeConstans.CONTACT_SEQ_KEY, LocalSEQ.get());
        return resList;
    }

    /**
     * @param sdk               初始化时候获取到的值
     * @param ncrypt_random_key 企业微信返回的随机密钥
     * @param encrypt_chat_msg  企业微信返回的单条记录的密文消息
     * @param privateKey        企业微信管理后台设置的私钥,!!!版本记得对应上!!!
     * @return JSONObject 返回不同格式的聊天数据,格式有二十来种
     * 详情请看官网 https://open.work.weixin.qq.com/api/doc/90000/90135/91774#%E6%B6%88%E6%81%AF%E6%A0%BC%E5%BC%8F
     */
    private static JSONObject decryptChatRecord(Long sdk, String ncrypt_random_key, String encrypt_chat_msg, String privateKey) {
        Long msg = null;
        try {
            //获取私钥
            PrivateKey privateKeyObj = RSAUtil.getPrivateKey(privateKey);
            String str = RSAUtil.decryptRSA(ncrypt_random_key, privateKeyObj);
            //初始化参数slice
            msg = Finance.NewSlice();

            //解密
            Finance.DecryptData(sdk, str, encrypt_chat_msg, msg);
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

    private static void getSwitchType(JSONObject realJsonData, String msgType) {
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

    private static void setMediaMeetingVoiceCallData(JSONObject realJsonData, String msgType) {
        JSONObject meetingVoiceCall = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String fileName = meetingVoiceCall.getString("filename");
        getPath(realJsonData, msgType, fileName);
    }

    private static void setMediaMixedData(JSONObject realJsonData, String msgType) {
        JSONObject mixedData = realJsonData.getJSONObject(msgType);
        JSONArray items = mixedData.getJSONArray("item");
        items.stream().map(item -> (JSONObject) item).forEach(item -> {
            getSwitchType(item, item.getString("type"));
        });
    }

    private static void setMediaFileData(JSONObject realJsonData, String msgType) {
        JSONObject emotionData = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String filename = emotionData.getString("filename");
        //String fileext = emotionData.getString("fileext");
        //String fileName = filename+"."+fileext;
        getPath(realJsonData, msgType, filename);
    }


    private static void setMediaImageData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".jpg";
        getPath(realJsonData, msgType, fileName);
    }

    private static void setMediaVoiceData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".mp3";
        getPath(realJsonData, msgType, fileName);
    }

    private static void setMediaVideoData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".mp4";
        getPath(realJsonData, msgType, fileName);
    }

    private static void setMediaEmotionData(JSONObject realJsonData, String msgType) {
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

    /*//本地存储
    private static void getPath(JSONObject realJsonData, String msgType, String fileName) {
        String filePath = getFilePath(msgType);
        JSONObject data = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String sdkfileid = data.getString("sdkfileid");
        try {
            getMediaData(sdkfileid, "", "", filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String currentDir = StringUtils.substring(filePath,downloadWeWorkPath.length()+1);
        String pathFileName;
        if(org.apache.commons.lang3.StringUtils.isBlank(currentDir)){
            pathFileName = Constants.RESOURCE_PREFIX + "/" +fileName;
        }else {
            pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        }
        data.put("attachment", pathFileName);
        if (realJsonData.containsKey("content")) {
            realJsonData.put("content", data);
        } else {
            realJsonData.put(msgType, data);
        }

    }*/

    //云存储
    private static void getPath(JSONObject realJsonData, String msgType, String fileName) {
        String filePath = getFilePath(msgType);
        JSONObject data = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String sdkfileid = data.getString("sdkfileid");
        try {
            getMediaData(sdkfileid, "", "", filePath, fileName);
            CosConfig cosConfig = SpringUtils.getBean(CosConfig.class);
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            StringBuilder cosUrl = new StringBuilder(cosConfig.getImgUrlPrefix());
            String cosFilePath = FileUploadUtils.upload2Cos(new FileInputStream(new File(filePath, fileName)), suffix, cosConfig);
            cosUrl.append(cosFilePath);
            data.put("attachment", cosUrl.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (realJsonData.containsKey("content")) {
            realJsonData.put("content", data);
        } else {
            realJsonData.put(msgType, data);
        }

    }

    private static String getFilePath(String msgType) {
        return StringUtils.format(downloadWeWorkPath, msgType, DateUtils.getDate());
    }

    private static void getMediaData(String sdkFileid, String proxy, String passwd, String filePath, String fileName) {
        String indexbuf = "";
        while (true) {
            long media_data = Finance.NewMediaData();
            int ret = Finance.GetMediaData(sdk, indexbuf, sdkFileid, proxy, passwd, timeout, media_data);
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
                outputStream.write(Finance.GetData(media_data));
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Finance.IsMediaDataFinish(media_data) == 1) {
                Finance.FreeMediaData(media_data);
                break;
            } else {
                indexbuf = Finance.GetOutIndexBuf(media_data);
                Finance.FreeMediaData(media_data);
            }
        }
    }
}
