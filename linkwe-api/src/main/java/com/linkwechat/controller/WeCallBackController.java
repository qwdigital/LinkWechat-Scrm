package com.linkwechat.controller;

import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.wecom.WxCryptUtil;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.service.IWeCorpAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WeCallBackController {

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @ApiModelProperty("post数据接收")
    @PostMapping(value = "/recive/{corpId}")
    public String recive(@RequestBody String msg, @RequestParam(name = "msg_signature") String signature,
                         String timestamp, String nonce, @PathVariable("corpId") String corpId) {
        log.info("post数据接收>>>>>>>>>>corpId:{},msg:{}",corpId,msg);
        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(corpId);
        WxCryptUtil wxCryptUtil = new WxCryptUtil(weCorpAccount.getToken(), weCorpAccount.getEncodingAesKey(), corpId);
        try {
            String decrypt = wxCryptUtil.decrypt(signature, timestamp, nonce, msg);
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeCallBackEx(), rabbitMQSettingConfig.getWeCallBackRk(), decrypt);
            return decrypt;
        } catch (Exception e) {
            log.error("post数据接收>>>>>>error",e);
            String sRespData = WxCryptUtil.getTextRespData("success");
            return wxCryptUtil.encrypt(sRespData);
        }
    }

    @ApiModelProperty("get数据校验")
    @GetMapping(value = "/recive/{corpId}")
    public String recive(HttpServletRequest request, @PathVariable("corpId") String corpId) {
        log.info("get数据校验>>>>>>>>>>corpId:{}",corpId);
        // 微信加密签名
        String sVerifyMsgSig = request.getParameter("msg_signature");
        // 时间戳
        String sVerifyTimeStamp = request.getParameter("timestamp");
        // 随机数
        String sVerifyNonce = request.getParameter("nonce");
        // 随机字符串
        String sVerifyEchoStr = request.getParameter("echostr");

        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(corpId);
        WxCryptUtil wxCryptUtil = new WxCryptUtil(weCorpAccount.getToken(), weCorpAccount.getEncodingAesKey(), corpId);
        try {
            if(StringUtils.isEmpty(sVerifyEchoStr)){
                return "success";
            }
            return wxCryptUtil.verifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
        } catch (Exception e) {
            log.error("get数据校验>>>>>>error",e);
            return "error";
        }
    }
}
