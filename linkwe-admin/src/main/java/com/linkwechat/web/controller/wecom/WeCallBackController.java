package com.linkwechat.web.controller.wecom;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.common.utils.wecom.WxCryptUtil;
import com.linkwechat.web.controller.common.CommonController;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.factory.WeEventHandle;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author danmo
 * @description 企微回调通知接口
 * @date 2020/11/6 17:31
 **/
@Slf4j
@RestController
@RequestMapping("/wecom/callback")
public class WeCallBackController extends CommonController {
    @Autowired
    private WeEventHandle weEventHandle;

    @Value("${wecome.callBack.appIdOrCorpId}")
    private String appIdOrCorpId;
    @Value("${wecome.callBack.token}")
    private String token;
    @Value("${wecome.callBack.encodingAesKey}")
    private String encodingAesKey;

    @PostMapping(value = "/recive")
    public String recive(@RequestBody String msg, @RequestParam(name = "msg_signature") String signature,
                         String timestamp, String nonce) {
        WxCryptUtil wxCryptUtil = new WxCryptUtil(token, encodingAesKey, appIdOrCorpId);
        try {
            String decrypt = wxCryptUtil.decrypt(signature, timestamp, nonce, msg);
            WxCpXmlMessageVO wxCpXmlMessage = StrXmlToBean(decrypt);
            log.info("企微回调通知接口 wxCpXmlMessage:{}", JSONObject.toJSONString(wxCpXmlMessage));
            try {
                WeCallBackEventFactory factory = weEventHandle.factory(wxCpXmlMessage.getEvent());
                if (factory !=null){
                    Threads.SINGLE_THREAD_POOL.submit(() -> factory.eventHandle(wxCpXmlMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();;
            String sRespData = WxCryptUtil.getTextRespData("success");
            return wxCryptUtil.encrypt(sRespData);
        }
    }

    @GetMapping(value = "/recive")
    public String recive(HttpServletRequest request) {
        // 微信加密签名
        String sVerifyMsgSig = request.getParameter("msg_signature");
        // 时间戳
        String sVerifyTimeStamp = request.getParameter("timestamp");
        // 随机数
        String sVerifyNonce = request.getParameter("nonce");
        // 随机字符串
        String sVerifyEchoStr = request.getParameter("echostr");

        WxCryptUtil wxCryptUtil = new WxCryptUtil(token, encodingAesKey, appIdOrCorpId);
        try {
            return wxCryptUtil.verifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
        } catch (Exception e) {
            return "error";
        }
    }

    private WxCpXmlMessageVO StrXmlToBean(String xmlStr){
        XStream xstream = XStreamInitializer.getInstance();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.processAnnotations(WxCpXmlMessage.class);
        xstream.processAnnotations(WxCpXmlMessageVO.class);
        xstream.processAnnotations(WxCpXmlMessageVO.ScanCodeInfo.class);
        xstream.processAnnotations(WxCpXmlMessageVO.SendPicsInfo.class);
        xstream.processAnnotations(WxCpXmlMessageVO.SendPicsInfo.Item.class);
        xstream.processAnnotations(WxCpXmlMessageVO.SendLocationInfo.class);
        xstream.processAnnotations(WxCpXmlMessageVO.BatchJob.class);
        WxCpXmlMessageVO wxCpXmlMessage = (WxCpXmlMessageVO)xstream.fromXML(xmlStr);
        return wxCpXmlMessage;
    }
}
