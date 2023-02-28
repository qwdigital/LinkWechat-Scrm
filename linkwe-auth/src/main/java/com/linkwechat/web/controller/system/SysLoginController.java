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
     * @return
     */
    @GetMapping("/findLoginParam")
    public AjaxResult findLoginParam(){
        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);
        if(Objects.isNull(weCorpAccount)){
            return AjaxResult.error(HttpStatus.NOT_TO_CONFIG,"请使用超管账号登陆系统做相关配置");
        }



        String joinCorpQr="";

        if(!linkWeChatConfig.isDemoEnviron()){
            joinCorpQr = redisService.getCacheObject(WeConstans.JOINCORPQR);
            if(StringUtils.isEmpty(joinCorpQr)){
                joinCorpQr=qwUserClient.getJoinQrcode(new WeCorpQrQuery()).getData().getJoinQrcode();
                redisService.setCacheObject(WeConstans.JOINCORPQR,joinCorpQr, WeConstans.JOINCORPQR_EFFETC_TIME , TimeUnit.SECONDS);
            }
        }else{
            joinCorpQr=linkWeChatConfig.getCustomerServiceQrUrl();
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
     * @param loginBody 登录信息ALL_PERMISSION
     * @return 结果
     */
    @PostMapping("/accountLogin")
    public AjaxResult<Map<String, Object>> login(@RequestBody LoginBody loginBody) {

        return AjaxResult.success(
                sysLoginService.login(loginBody.getUsername(),loginBody.getPassword())
        );
    }




}
