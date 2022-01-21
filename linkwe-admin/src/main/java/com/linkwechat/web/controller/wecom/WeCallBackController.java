package com.linkwechat.web.controller.wecom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.utils.wecom.WxCryptUtil;
import com.linkwechat.web.controller.common.CommonController;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.event.WeEventPublisherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author danmo
 * @description 企微回调通知接口
 * @date 2020/11/6 17:31
 **/
@Api(tags = "企微回调通知")
@Slf4j
@RestController
@RequestMapping("/wecom/callback")
public class WeCallBackController extends CommonController {
    @Autowired
    private WeEventPublisherService weEventPublisherService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @ApiModelProperty("post数据接收")
    @PostMapping(value = "/recive/{corpId}")
    public String recive(@RequestBody String msg, @RequestParam(name = "msg_signature") String signature,
                         String timestamp, String nonce,@PathVariable("corpId") String corpId) {
        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(corpId);
        WxCryptUtil wxCryptUtil = new WxCryptUtil(corpAccount.getToken(), corpAccount.getEncodingAesKey(), corpId);
        try {
            String decrypt = wxCryptUtil.decrypt(signature, timestamp, nonce, msg);
            weEventPublisherService.register(decrypt);
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();;
            String sRespData = WxCryptUtil.getTextRespData("success");
            return wxCryptUtil.encrypt(sRespData);
        }
    }

    @ApiModelProperty("get数据校验")
    @GetMapping(value = "/recive/{corpId}")
    public String recive(HttpServletRequest request,@PathVariable("corpId") String corpId) {
        // 微信加密签名
        String sVerifyMsgSig = request.getParameter("msg_signature");
        // 时间戳
        String sVerifyTimeStamp = request.getParameter("timestamp");
        // 随机数
        String sVerifyNonce = request.getParameter("nonce");
        // 随机字符串
        String sVerifyEchoStr = request.getParameter("echostr");

        WeCorpAccount corpAccount =  weCorpAccountService.getCorpAccountByCorpId(corpId);
        WxCryptUtil wxCryptUtil = new WxCryptUtil(corpAccount.getToken(), corpAccount.getEncodingAesKey(), corpId);
        try {
            return wxCryptUtil.verifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
        } catch (Exception e) {
            return "error";
        }
    }
}
