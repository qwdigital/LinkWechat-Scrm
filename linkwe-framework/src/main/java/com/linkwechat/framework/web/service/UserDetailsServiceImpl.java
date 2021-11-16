package com.linkwechat.framework.web.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.UserStatus;
import com.linkwechat.common.exception.BaseException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.system.service.ISysRoleService;
import com.linkwechat.system.service.ISysUserService;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeUserService;
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


    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;


    @Autowired
    private ISysRoleService iSysRoleService;


    @Autowired
    private RuoYiConfig ruoYiConfig;


    @Autowired
    private IWeUserService iWeUserService;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {

            //we_user表中去查询，如果该表为空则提示用户不存在，如果不为空，则将用户记录注册到系统用户表中
            WeUser  weUser=iWeUserService.getOne(new LambdaQueryWrapper<WeUser>()
                    .eq(WeUser::getMobile,username)
                    .ne(WeUser::getIsActivate,new Integer(6)));
            if(null == weUser){
                throw new BaseException("对不起，您的账号：" + username + " 不存在");
            }
//            if(StrUtil.isBlank(weUser.getMobile())){
//                throw new BaseException("请填写当前企业员工的手机号：" + username + " 然后再登录");
//            }

//         //查询企业管理相关账号
//            WeCorpAccount weCorpByAccount = iWeCorpAccountService.getOne(new LambdaQueryWrapper<WeCorpAccount>().eq(WeCorpAccount::getCorpAccount, username)
//                    .eq(WeCorpAccount::getDelFlag, Constants.NORMAL_CODE));
//            if(null != weCorpByAccount){
//                //注册到we_user表中
//                user=SysUser.builder()
//                        .userName(weCorpByAccount.getCorpAccount())
//                        .nickName(weCorpByAccount.getCompanyName())
//                        .userType(Constants.USER_TYOE_CORP_ADMIN)
//                        .roleIds(ArrayUtil.toArray(iSysRoleService.selectRoleList(SysRole.builder()
//                                .roleKey(Constants.DEFAULT_WECOME_CORP_ADMIN)
//                                .build()).stream().map(SysRole::getRoleId).collect(Collectors.toList()), Long.class))
//                        .password(SecurityUtils.encryptPassword(ruoYiConfig.getWeUserDefaultPwd()))
//                        .build();
//            } else{
//                //we_user表中去查询，如果该表为空则提示用户不存在，如果不为空，则将用户记录注册到系统用户表中
//                WeUser  weUser=iWeUserService.getOne(new LambdaQueryWrapper<WeUser>()
//                        .eq(WeUser::getMobile,username)
//                        .ne(WeUser::getIsActivate,new Integer(6)));
//                if(null == weUser){
//                    throw new BaseException("对不起，您的账号：" + username + " 不存在");
//                }
//                if(StrUtil.isBlank(weUser.getMobile())){
//                    throw new BaseException("请填写当前企业员工的手机号：" + username + " 然后再登录");
//                }
//                //注册到we_user表中
//                user=SysUser.builder()
//                        .userName(username)
//                        .nickName(weUser.getName())
//                        .userType(Constants.USER_TYPE_WECOME)
//                        .email(weUser.getEmail())
//                        .phonenumber(weUser.getMobile())
//                        .sex(weUser.getGender() ==0 ? "1": weUser.getGender() .toString())
//                        .avatar(weUser.getHeadImageUrl())
//                        .weUserId(weUser.getUserId())
//                        .roleIds(ArrayUtil.toArray(iSysRoleService.selectRoleList(SysRole.builder()
//                                .roleKey(Constants.DEFAULT_WECOME_ROLE_KEY)
//                                .build()).stream().map(SysRole::getRoleId).collect(Collectors.toList()), Long.class))
//                        .password(SecurityUtils.encryptPassword(ruoYiConfig.getWeUserDefaultPwd()))
//                        .build();
//
//            }
//
//            if(user != null){
//                userService.insertUser(
//                        user
//                );
//            }
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


//       //当前登录用户为企业管理，设置corpId和密钥相关
//       if(Constants.USER_TYOE_CORP_ADMIN.equals(user.getUserType())){
//          user.setWeCorpAccount(
//                  iWeCorpAccountService.getOne(new LambdaQueryWrapper<WeCorpAccount>().eq(WeCorpAccount::getCorpAccount, username)
//                          .eq(WeCorpAccount::getDelFlag, Constants.NORMAL_CODE))
//          );
//       }


        return createLoginUser(user);
    }




    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
