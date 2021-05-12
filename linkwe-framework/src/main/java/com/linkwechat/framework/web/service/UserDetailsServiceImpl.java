package com.linkwechat.framework.web.service;

import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.UserStatus;
import com.linkwechat.common.exception.BaseException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {

            log.info("登录用户：{} 不存在.", username);
            throw new BaseException("对不起，您的账号：" + username + " 不存在");
//            //企业管理登录
//            //查询企业管理相关账号
//            WeCorpAccount weCorpByAccount = iWeCorpAccountService.findWeCorpByAccount(username);
//            if(null != weCorpByAccount){
//                //注册到we_user表中
//                user=SysUser.builder()
//                        .userName(weCorpByAccount.getCropAccount())
//                        .nickName(weCorpByAccount.getCompanyName())
//                        .userType(Constants.USER_TYOE_CORP_ADMIN)
//                        .roleIds(ArrayUtil.toArray(roleMapper.selectRoleList(SysRole.builder()
//                                .roleKey(Constants.DEFAULT_WECOME_CORP_ADMIN)
//                                .build()).stream().map(SysRole::getRoleId).collect(Collectors.toList()), Long.class))
//                        .password(SecurityUtils.encryptPassword(ruoYiConfig.getWeUserDefaultPwd()))
//                        .build();
//            }

//            else{
//
//                //we_user表中去查询，如果该表为空则提示用户不存在，如果不为空，则将用户记录注册到系统用户表中
//                WeUser weUser
//                        = iWeUserService.getById(username);
//                if(null == weUser){
//                    log.info("登录用户：{} 不存在.", username);
//                    throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
//                }
//
//                //注册到we_user表中
//                user=SysUser.builder()
//                        .userName(weUser.getUserId())
//                        .nickName(weUser.getName())
//                        .userType(Constants.USER_TYPE_WECOME)
//                        .email(weUser.getEmail())
//                        .phonenumber(weUser.getMobile())
//                        .sex(weUser.getGender() ==0 ? "1": weUser.getGender() .toString())
//                        .avatar(weUser.getAvatarMediaid())
//                        .roleIds(ArrayUtil.toArray(roleMapper.selectRoleList(SysRole.builder()
//                                .roleKey(Constants.DEFAULT_WECOME_ROLE_KEY)
//                                .build()).stream().map(SysRole::getRoleId).collect(Collectors.toList()), Long.class))
//                        .password(SecurityUtils.encryptPassword(ruoYiConfig.getWeUserDefaultPwd()))
//                        .build();
//            }
//            userService.insertUser(
//                    user
//            );
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

       //当前登录用户为企业管理，设置corpId和密钥相关
//       if(user.getUserType().equals(Constants.USER_TYOE_CORP_ADMIN)){
//          user.setWeCorpAccount(
//                  iWeCorpAccountService.findWeCorpByAccount(username)
//          );
//       }


        return createLoginUser(user);
    }




    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
