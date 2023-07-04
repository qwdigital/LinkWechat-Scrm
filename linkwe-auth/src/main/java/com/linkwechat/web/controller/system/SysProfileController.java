package com.linkwechat.web.controller.system;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.framework.service.TokenService;
import com.linkwechat.web.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Autowired
    private QwFileClient qwFileClient;

    @Value("${rsa.privateKey}")
    private String rasPriKey;

    @Value("${rsa.publicKey}")
    private String rsaPubKey;


    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getSysUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUserName()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        if (userService.updateUserProfile(user) > 0) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // 更新缓存用户信息
            loginUser.getSysUser().setNickName(user.getNickName());
            loginUser.getSysUser().setPhoneNumber(user.getPhoneNumber());
            loginUser.getSysUser().setEmail(user.getEmail());
            loginUser.getSysUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        if (linkWeChatConfig.isEditPwd()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String password = loginUser.getSysUser().getPassword();
            try {
                RSA rsa = SecureUtil.rsa(rasPriKey, rsaPubKey);
                String oldPasswordPlain = rsa.decryptStr(oldPassword, KeyType.PrivateKey);
                String newPasswordPlain = rsa.decryptStr(newPassword, KeyType.PrivateKey);
                String databasePwdPlain = rsa.decryptStr(password, KeyType.PrivateKey);
                //判断旧密码是否正确
                if (!oldPasswordPlain.equals(databasePwdPlain)) {
                    return AjaxResult.error("修改密码失败，旧密码错误");
                }
                //判断新旧密码是否一致
                if (oldPasswordPlain.equals(newPasswordPlain)) {
                    return AjaxResult.error("新密码不能与旧密码相同");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("修改密码，ras错误:{}", e.getMessage());
            }

            //更新密码
            LambdaUpdateWrapper<SysUser> wrapper = Wrappers.lambdaUpdate();
            wrapper.eq(SysUser::getUserId, loginUser.getSysUser().getUserId());
            wrapper.set(SysUser::getPassword, newPassword);
            boolean update = userService.update(wrapper);
            if (update) {
                // 更新缓存用户密码
                loginUser.getSysUser().setPassword(newPassword);
                tokenService.setLoginUser(loginUser);
                return AjaxResult.success();
            }
            return AjaxResult.error("修改密码异常，请联系管理员");
        } else {
            return AjaxResult.error("当前环境密码不可修改");
        }
    }

    /**
     * 头像上传
     */
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            FileEntity fileEntity = qwFileClient.upload(file).getData();
            if (null == fileEntity || StringUtils.isEmpty(fileEntity.getUrl())) {
                return AjaxResult.error("更新头像失败，请稍后再试");

            }
            if (userService.updateUserAvatar(loginUser.getUserName(), fileEntity.getUrl())) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", fileEntity.getUrl());
                // 更新缓存用户头像
                loginUser.getSysUser().setAvatar(fileEntity.getUrl());
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
