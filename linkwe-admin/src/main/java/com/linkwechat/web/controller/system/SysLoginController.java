package com.linkwechat.web.controller.system;

import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysMenu;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginBody;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.framework.web.service.SysLoginService;
import com.linkwechat.framework.web.service.SysPermissionService;
import com.linkwechat.framework.web.service.TokenService;
import com.linkwechat.system.service.ISysMenuService;
import com.linkwechat.wecom.client.WeAccessTokenClient;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.domain.dto.WeLoginUserInfoDto;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private IWeCorpAccountService iWxCorpAccountService;


    @Autowired
    private WeAccessTokenClient weAccessTokenClient;

//    @Autowired
//    private IWeGroupCodeService weGroupCodeService;


    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }



    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        //校验用户是否拥有可用corpid
        WeCorpAccount wxCorpAccount
                = iWxCorpAccountService.findValidWeCorpAccount();
        if(null != wxCorpAccount){
            user.setValidCropId(true);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取企业扫码登录相关参数
     * @return
     */
    @GetMapping("/findWxQrLoginInfo")
    public AjaxResult findQrLoginParm(){

        WeCorpAccount validWeCorpAccount
                = iWxCorpAccountService.findValidWeCorpAccount();
        if(null != validWeCorpAccount){
            validWeCorpAccount.setContactSecret(null);
            validWeCorpAccount.setCorpSecret(null);
            validWeCorpAccount.setProviderSecret(null);
        }

        return AjaxResult.success(validWeCorpAccount);
    }



    /**
     * 扫码登录微信端回调
     * @param auth_code
     * @return
     */
    @GetMapping("/wxQrLogin")
    public AjaxResult wxQrLogin(String auth_code){

        AjaxResult ajax = AjaxResult.success();

        WeLoginUserInfoDto loginInfo = weAccessTokenClient.getLoginInfo(auth_code);
        if( null != loginInfo.getUser_info()){

                String token = loginService.noPwdLogin(loginInfo.getUser_info().getUserid());
                ajax.put(Constants.TOKEN, token);

        }

        return ajax;

    }

}
