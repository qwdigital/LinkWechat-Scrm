package com.linkwechat.web.controller.system;


import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.model.LoginBody;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.web.domain.Sys;
import com.linkwechat.web.domain.vo.LoginParamVo;
import com.linkwechat.web.service.SysLoginService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("auth")
public class SysLoginController {


    @Autowired
    private SysLoginService sysLoginService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;


    @Autowired
    private QwUserClient qwUserClient;

    @Autowired
    private RedisService redisService;


    /**
     * 获取登陆相关参数
     *
     * @return
     */
    @GetMapping("/findLoginParam")
    public AjaxResult findLoginParam() {
        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);
        if (Objects.isNull(weCorpAccount)) {
            return AjaxResult.error(HttpStatus.NOT_TO_CONFIG, "请使用超管账号登陆系统做相关配置");
        }


        String joinCorpQr = "";

        if (!linkWeChatConfig.isDemoEnviron()) {
            joinCorpQr = redisService.getCacheObject(WeConstans.JOINCORPQR);
            if (StringUtils.isEmpty(joinCorpQr)) {
                joinCorpQr = qwUserClient.getJoinQrcode(new WeCorpQrQuery()).getData().getJoinQrcode();
                redisService.setCacheObject(WeConstans.JOINCORPQR, joinCorpQr, WeConstans.JOINCORPQR_EFFETC_TIME, TimeUnit.SECONDS);
            }
        } else {
            joinCorpQr = linkWeChatConfig.getCustomerServiceQrUrl();
        }


        return AjaxResult.success(
                LoginParamVo.builder().loginQr(MessageFormat.format(linkWeChatConfig.getWecomeLoginUrl(),
                        weCorpAccount.getCorpId(),
                        weCorpAccount.getAgentId()))
                        .joinCorpQr(
                                joinCorpQr
                        )
                        .build()
        );

    }


    /**
     * 扫码登陆
     *
     * @param authCode
     * @return
     */
    @GetMapping("/qrLogin")
    public AjaxResult<Map<String, Object>> customerLogin(String authCode) {
        Map<String, Object> map = sysLoginService.customerLogin(authCode);
        return AjaxResult.success(map);
    }


    /**
     * 账号密码登陆
     *
     * @param loginBody 登录信息 ALL_PERMISSION
     * @return 结果
     */
    @PostMapping("/accountLogin")
    public AjaxResult<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        return AjaxResult.success(sysLoginService.login(loginBody.getUsername(), loginBody.getPassword()));
    }

    /**
     * 移动端应用登陆
     *
     * @param auth_code
     * @return
     */
    @GetMapping("/linkLogin")
    public AjaxResult<Map<String, Object>> linkLogin(String auth_code) {
        Map<String, Object> map = sysLoginService.linkLogin(auth_code);
        return AjaxResult.success(map);
    }

    /**
     * 企业微信h5回掉地址
     * @param redirectUrl
     * @return
     */
    @GetMapping("/wcRedirect")
    public AjaxResult wcRedirect(@ApiParam(value = "回调地址",required = true) String redirectUrl){

        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);

        if(null == weCorpAccount || StringUtils.isEmpty(weCorpAccount.getCorpId()) || StringUtils.isEmpty(weCorpAccount.getAgentId())){

            return AjaxResult.error("企业微信相关配置不可为空");
        }

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_privateinfo&state=%s&agentid=%s#wechat_redirect";
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new WeComException("回调地址解析失败");
        }
        //todo

        String state = "linkwechat";
        String qrcodeUrl = String.format(
                baseUrl,
                weCorpAccount.getCorpId(),
                redirectUrl,
                state,
                weCorpAccount.getAgentId());
        return AjaxResult.success(qrcodeUrl);


    }

    /**
     * 微信登录跳转
     *
     * @return
     */
    @GetMapping("/wxRedirect")
    public AjaxResult  wxRedirect(@ApiParam(value = "回调地址",required = true) String redirectUrl){

        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);

        if(null == weCorpAccount || StringUtils.isEmpty(weCorpAccount.getWxAppId())){

            return AjaxResult.error("公众号相关配置不可为空");
        }

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new WeComException("回调地址解析失败");
        }
        //todo

        String state = "linkwechat";
        String qrcodeUrl = String.format(
                baseUrl,
                weCorpAccount.getWxAppId(),
                redirectUrl,
                state);
        return AjaxResult.success(qrcodeUrl);
    }



    /**
     * 微信登录
     *
     * @return
     */
    @GetMapping("/wxLogin")
    public AjaxResult<String> wxLogin(@ApiParam(value = "URL上的code参数",required = true) String code){
        Map<String, Object> map = sysLoginService.wxLogin(code);
        return AjaxResult.success(map);
    }

}
