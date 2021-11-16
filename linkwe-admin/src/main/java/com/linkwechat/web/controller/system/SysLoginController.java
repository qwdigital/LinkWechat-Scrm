package com.linkwechat.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysMenu;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.core.domain.model.LoginBody;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.framework.web.service.SysLoginService;
import com.linkwechat.framework.web.service.SysPermissionService;
import com.linkwechat.framework.web.service.TokenService;
import com.linkwechat.system.service.ISysDeptService;
import com.linkwechat.system.service.ISysMenuService;
import com.linkwechat.system.service.ISysUserService;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.dto.WeLoginUserInfoDto;
import com.linkwechat.wecom.domain.dto.WeUserInfoDto;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.apache.commons.collections4.CollectionUtils;
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
    private WeUserClient weUserClient;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysUserService sysUserService;


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

        List<SysDept> sysDepts = iSysDeptService.selectDeptList(SysDept.builder().parentId(new Long(0)).build());

        if(!CollectionUtils.isEmpty(sysDepts)){
            user.setCompanyName(
                    sysDepts.stream().findFirst().get().getDeptName()
            );
        }

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

//    /**
//     * 获取企业扫码登录相关参数
//     * @return
//     */
//    @GetMapping("/findWxQrLoginInfo")
//    public AjaxResult findQrLoginParm(){
//
//        WeCorpAccount validWeCorpAccount
//                = iWxCorpAccountService.findValidWeCorpAccount();
//        if(null != validWeCorpAccount){
//            validWeCorpAccount.setContactSecret(null);
//            validWeCorpAccount.setCorpSecret(null);
//            validWeCorpAccount.setProviderSecret(null);
//        }
//
//        return AjaxResult.success(validWeCorpAccount);
//    }



//    /**
//     * 扫码登录微信端回调
//     * @param auth_code 授权code
//     * @param agentId 应用id
//     * @return
//     */
//    @GetMapping("/wxQrLogin")
//    public AjaxResult wxQrLogin(String auth_code, String agentId){
//
//        AjaxResult ajax = AjaxResult.success();
//
//        WeUserInfoDto userInfo = weUserClient.getUserInfo(auth_code, agentId);
//        if( null != userInfo){
//            SysUser sysUser = sysUserService.selectUserByCorpUserId(userInfo.getUserId());
//            if (null != sysUser){
//                String token = loginService.noPwdLogin(sysUser.getUserName());
//                ajax.put(Constants.TOKEN, token);
//            }else {
//                return AjaxResult.error("请绑定后再登录");
//            }
//        }
//        return ajax;
//    }



//    /**
//     * 通过企业id和企业密钥登录
//     * @param corpId
//     * @param corpSecret
//     * @return
//     */
//    @GetMapping("/corpLogin")
//    public AjaxResult corpLogin(String corpId,String corpSecret){
//
//        WeCorpAccount weCorpAccount = iWxCorpAccountService.getOne(new LambdaQueryWrapper<WeCorpAccount>()
//                .eq(WeCorpAccount::getCorpId, corpId)
//                .eq(WeCorpAccount::getCorpSecret, corpSecret)
//                .eq(WeCorpAccount::getDelFlag, Constants.NORMAL_CODE));
//
//
//        if(weCorpAccount == null){
//
//            return AjaxResult.error("当前企业id与企业密码不匹配或不存在");
//        }
//
//        return AjaxResult.success(
//                loginService.noPwdLogin(weCorpAccount.getCorpAccount())
//        );
//    }


}
