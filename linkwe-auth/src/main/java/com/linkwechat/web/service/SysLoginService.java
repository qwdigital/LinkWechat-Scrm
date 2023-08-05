package com.linkwechat.web.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.entity.SysUserDept;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import com.linkwechat.common.enums.CommonErrorCodeEnum;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.domain.wecom.vo.weixin.WxTokenVo;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.fegin.QxAuthClient;
import com.linkwechat.framework.service.TokenService;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Lazy
    @Resource
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;


    @Resource
    private QxAuthClient qxAuthClient;

    @Resource
    private QwUserClient qwUserClient;


    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;


    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private ISysUserDeptService iSysUserDeptService;

    @Value("${rsa.privateKey}")
    private String rasPriKey;

    @Value("${rsa.publicKey}")
    private String rsaPubKey;

    /**
     * 登录验证（密码登陆）
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public Map<String, Object> login(String userName, String password) {
        SysUser sysUser = sysUserService.selectUserByUserName(userName);
        if (StringUtils.isNull(sysUser)) {
            throw new ServiceException("用户不存在/密码错误");
        }
        try {
            RSA rsa = SecureUtil.rsa(rasPriKey, rsaPubKey);
            //登录密码
            String loginPwd = rsa.decryptStr(password, KeyType.PrivateKey);

            //数据库密码
            if (StrUtil.isBlank(sysUser.getPassword())) {
                throw new ServiceException("用户不存在/密码错误");
            }
            String databasePwd = rsa.decryptStr(sysUser.getPassword(), KeyType.PrivateKey);
            if (!loginPwd.equals(databasePwd)) {
                throw new ServiceException("用户不存在/密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录异常：{}", e.getMessage());
            throw new ServiceException("用户不存在/密码错误");
        }
        // 生成token
        LoginUser sysUserVo = findLoginUser(sysUser);
        return tokenService.createToken(sysUserVo);
    }


    /**
     * 登录验证（扫码登录)
     *
     * @param authCode
     */
    public Map<String, Object> customerLogin(String authCode) {
        //调用企业微信接口获取用户信息
        WeUserQuery query = new WeUserQuery();
        query.setCode(authCode);
        WeLoginUserVo weLoginUser = qwUserClient.getLoginUser(query).getData();

        if (Objects.isNull(weLoginUser)) {
            throw new WeComException(CommonErrorCodeEnum.ERROR_CODE_10001.getErrorCode(),
                    CommonErrorCodeEnum.ERROR_CODE_10001.getErrorMsg());
        }
        if (weLoginUser.getErrCode() != null && weLoginUser.getErrCode() != 0) {
            throw new WeComException(weLoginUser.getErrCode(),
                    WeErrorCodeEnum.parseEnum(weLoginUser.getErrCode()).getErrorMsg());
        }
        SysUser sysUser = sysUserService.selectUserByWeUserId(weLoginUser.getUserId());
        LoginUser sysUserVo = findLoginUser(sysUser);
        return tokenService.createToken(sysUserVo);
    }


    //获取登陆用户相关数据
    private LoginUser findLoginUser(SysUser sysUser) {
        if (Objects.isNull(sysUser)) {
            throw new WeComException(CommonErrorCodeEnum.ERROR_CODE_10003.getErrorCode(), CommonErrorCodeEnum.ERROR_CODE_10003.getErrorMsg());
        }
        if (Objects.equals("1", sysUser.getStatus())) {
            throw new WeComException(CommonErrorCodeEnum.ERROR_CODE_10006.getErrorCode(), CommonErrorCodeEnum.ERROR_CODE_10006.getErrorMsg());
        }
        //获取用户所在部门
        List<SysUserDept> sysUserDeptList = iSysUserDeptService.list(new LambdaQueryWrapper<SysUserDept>()
                .eq(SysUserDept::getUserId, sysUser.getUserId())
                .eq(SysUserDept::getDelFlag, Constants.NORMAL_CODE));
        if (CollectionUtil.isNotEmpty(sysUserDeptList)) {
            sysUser.setDeptIds(StringUtils.join(sysUserDeptList.stream().map(SysUserDept::getDeptId).collect(Collectors.toList()), ","));
        }
        // 角色集合
        List<SysRole> sysRoles = sysRoleService.selectRolesPermissionByUserId(sysUser.getUserId());
        sysUser.setRoles(sysRoles);

        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setUserType(sysUser.getUserType());
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : sysRoles) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        sysUserVo.setRoles(permsSet);
        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);
        if (Objects.nonNull(weCorpAccount)) {
            sysUserVo.setCorpId(weCorpAccount.getCorpId());
            sysUserVo.setCorpName(weCorpAccount.getCompanyName());
        }
        return sysUserVo;
    }


    public Map<String, Object> linkLogin(String authCode) {
        //调用企业微信接口获取用户信息
        WeUserQuery query = new WeUserQuery();
        query.setCode(authCode);
        WeLoginUserVo weLoginUserVo = qwUserClient.getLoginUser(query).getData();

        if (weLoginUserVo == null) {
            throw new WeComException(CommonErrorCodeEnum.ERROR_CODE_10001.getErrorCode(),
                    CommonErrorCodeEnum.ERROR_CODE_10001.getErrorMsg());
        }
        if (weLoginUserVo.getErrCode() != null && weLoginUserVo.getErrCode() != 0) {
            throw new WeComException(weLoginUserVo.getErrCode(),
                    WeErrorCodeEnum.parseEnum(weLoginUserVo.getErrCode()).getErrorMsg());
        }

        SysUser sysUser = sysUserService.selectUserByWeUserId(weLoginUserVo.getUserId());
        LoginUser sysUserVo = findLoginUser(sysUser);
        //获取员工共头像等信息
        if (StringUtils.isNotEmpty(weLoginUserVo.getUserTicket())) {
            sysUserService.getUserSensitiveInfo(sysUser.getUserId(), weLoginUserVo.getUserTicket());
        }
        return tokenService.createToken(sysUserVo);
    }

    public Map<String, Object> wxLogin(String authCode) {
        WxTokenVo wxTokenVo = qxAuthClient.getToken(authCode).getData();
        if (wxTokenVo == null) {
            throw new WeComException(CommonErrorCodeEnum.ERROR_CODE_10001.getErrorCode(),
                    CommonErrorCodeEnum.ERROR_CODE_10001.getErrorMsg());
        }
        if (StringUtils.isNotEmpty(wxTokenVo.getErrMsg())) {
            throw new WeComException(wxTokenVo.getErrCode(), wxTokenVo.getErrMsg());
        }

        WxLoginUser wxLoginToken = tokenService.getWxLoginToken(wxTokenVo.getOpenId());
        if (wxLoginToken != null) {
            wxLoginToken.setExpireTime(wxTokenVo.getExpiresIn());
            wxLoginToken.setLoginTime(System.currentTimeMillis());
            return tokenService.createToken(wxLoginToken);
        }

        WxAuthUserInfoVo wxAuthUserInfoVo = qxAuthClient.getUserInfo(wxTokenVo.getOpenId(), "zh_CN").getData();
        if (wxAuthUserInfoVo == null) {
            throw new WeComException(CommonErrorCodeEnum.ERROR_CODE_10001.getErrorCode(),
                    CommonErrorCodeEnum.ERROR_CODE_10001.getErrorMsg());
        }
        if (StringUtils.isNotEmpty(wxAuthUserInfoVo.getErrMsg())) {
            throw new WeComException(wxAuthUserInfoVo.getErrCode(), wxAuthUserInfoVo.getErrMsg());
        }
        //保存微信用户信息
        wxUserService.saveOrUpdate(wxAuthUserInfoVo);
        WxLoginUser wxLoginUser = new WxLoginUser();
        BeanUtils.copyPropertiesASM(wxAuthUserInfoVo, wxLoginUser);
        wxLoginUser.setExpireTime(wxTokenVo.getExpiresIn());
        wxLoginUser.setLoginTime(System.currentTimeMillis());
        return tokenService.createToken(wxLoginUser);
    }

}
