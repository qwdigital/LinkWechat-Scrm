package com.linkwechat.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.UserConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.entity.SysUserDept;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.enums.CorpUserEnum;
import com.linkwechat.common.enums.RoleType;
import com.linkwechat.common.enums.UserTypes;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.user.vo.WeUserScreenConditVo;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.fegin.QwCorpClient;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.service.IWeLeaveUserService;
import com.linkwechat.web.domain.SysPost;
import com.linkwechat.web.domain.SysUserPost;
import com.linkwechat.web.domain.SysUserRole;
import com.linkwechat.web.domain.vo.UserRoleVo;
import com.linkwechat.web.domain.vo.UserVo;
import com.linkwechat.web.mapper.*;
import com.linkwechat.web.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysUserDeptMapper userDeptMapper;

    @Resource
    private SysPostMapper postMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysUserPostMapper userPostMapper;

    @Resource
    private SysRoleDeptMapper roleDeptMapper;

    @Resource
    private ISysConfigService configService;

    @Resource
    private ISysUserDeptService sysUserDeptService;

    @Resource
    private QwUserClient userClient;

    @Resource
    private QwCorpClient corpClient;

    @Autowired
    private IWeLeaveUserService iWeLeaveUserService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysRoleService sysRoleService;


    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    @Override
    public List<UserVo> selectUserVoList(SysUser sysUser,PageDomain pageDomain) {
        List<UserVo> userDeptList = userMapper.selectUserDeptList(sysUser,pageDomain);

        if(CollectionUtil.isNotEmpty(userDeptList)){

            List<UserRoleVo> userRoleList = userMapper.selectUserRoleList(userDeptList.stream().map(UserVo::getUserId).collect(Collectors.toList()));

            Map<Long, List<UserRoleVo>> userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(UserRoleVo::getUserId));
            Set<Long> roleIdSet = new HashSet<>();
            Map<Long, List<SysDept>> map = new HashMap<>();
            userDeptList.stream().filter(Objects::nonNull).forEach(u -> {
                if (CollectionUtils.isNotEmpty(userRoleMap.get(u.getUserId()))) {
                    List<Long> roleIdList = userRoleMap.get(u.getUserId()).stream().map(UserRoleVo::getRoleId).collect(Collectors.toList());
                    u.setRoles(userRoleMap.get(u.getUserId()));
                    roleIdSet.addAll(roleIdList);
                }
            });
            roleIdSet.stream().filter(Objects::nonNull).forEach(roleId -> {
                List<SysDept> depts = roleDeptMapper.selectRoleDeptList(roleId);
                map.put(roleId, depts);
            });
            return userDeptList.stream().peek(user -> {
                if (CollectionUtils.isNotEmpty(userRoleMap.get(user.getUserId()))) {
                    Set<SysDept> s = new HashSet<>();
                    userRoleMap.get(user.getUserId()).forEach(r -> s.addAll(new HashSet<>(map.get(r.getRoleId()))));
                    user.setRoleDepts(new ArrayList<>(s));
                }
            }).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public int selectCountUserDeptList(SysUser sysUser) {
        return userMapper.selectCountUserDeptList(sysUser);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhoneNumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateUser(SysUserDTO sysUser) {
        WeUserQuery query = new WeUserQuery();
        query.setUserid(sysUser.getWeUserId());
        query.setCorpid(sysUser.getCorpId());
        AjaxResult<WeUserDetailVo> result = userClient.getUserInfo(query);
        List<Long> delUserDeptId = new ArrayList<>();
        List<SysUserDept> userDeptList = new ArrayList<>();
        if (result.getData() != null) {
            WeUserDetailVo vo = result.getData();
            SysUser user = sysUserGenerator(vo);
            SysUser userExist = selectUserByWeUserId(user.getWeUserId());
            if (userExist != null) {
                user.setUserId(userExist.getUserId());
            }
            for (int i = 0; i < vo.getDepartment().size(); i++) {
                delUserDeptId.addAll(new LambdaQueryChainWrapper<>(userDeptMapper).eq(SysUserDept::getWeUserId, vo.getUserId()).eq(SysUserDept::getDeptId, vo.getDepartment().get(i)).list().stream().map(SysUserDept::getUserDeptId).collect(Collectors.toList()));
                userDeptList.add(userDeptGenerator(user,vo, i));
            }

//            sysUserDeptService.removeByIds(delUserDeptId);
            sysUserDeptService.remove(new LambdaQueryWrapper<SysUserDept>()
                    .eq(SysUserDept::getWeUserId,sysUser.getWeUserId()));

            sysUserDeptService.saveOrUpdateBatch(userDeptList);
            userMapper.updateUser(user);
        }

    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        userMapper.deleteUserByIds(userIds);
        //删除成功，同步删除企业微信


        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser((SysUserDTO) user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


    @Override
    public List<SysUser> syncWeUser(Long deptId, String corpId) {
        WeUserListQuery query = new WeUserListQuery();
        query.setDepartment_id(String.valueOf(deptId));
        query.setFetch_child(0);
        query.setCorpid(corpId);
        query.setFetch_child(1);
        AjaxResult<WeUserListVo> vo = userClient.getList(query);
        List<SysUserDept> userDeptList = new ArrayList<>();
        List<Long> delUserDeptId = new ArrayList<>();
        List<SysUser> userList = vo.getData().getUserList().stream().map(u -> {
            SysUser user = sysUserGenerator(u);
            for (int i = 0; i < u.getDepartment().size(); i++) {
                delUserDeptId.addAll(new LambdaQueryChainWrapper<>(userDeptMapper).eq(SysUserDept::getWeUserId, u.getUserId()).eq(SysUserDept::getDeptId, u.getDepartment().get(i)).list().stream().map(SysUserDept::getUserDeptId).collect(Collectors.toList()));
                userDeptList.add(userDeptGenerator(user,u, i));
            }
            return user;
        }).collect(Collectors.toList());

        if(CollectionUtil.isNotEmpty(userList)){
            this.baseMapper.batchAddOrUpdate(userList);

        }

        //数据库做检查,检索出为分配角色的用户,然后为用户分配默认角色
        List<SysUserRole> allUserRole = userRoleMapper.findAllUserRole();
        if(CollectionUtil.isEmpty(allUserRole)){
            allUserRole=new ArrayList<>();
        }
        Collection<Long> subtractUserId = CollectionUtils.subtract(
                userList.stream().map(SysUser::getUserId).collect(Collectors.toList())
                , allUserRole.stream().map(SysUserRole::getUserId).collect(Collectors.toList()));
        if(CollectionUtil.isNotEmpty(subtractUserId)){
            List<SysUserRole> sysUserRoles=new ArrayList<>();
            //为当前用户配置默认角色
            //默认权限入库,同时绑定超管
            Optional<RoleType> optionalRoleType = RoleType.of(5);
            if (optionalRoleType.isPresent()) {
                //获取当前租户下的普通成员
                List<SysRole> defaultRoles = sysRoleService.selectRoleList(
                        new SysRole(optionalRoleType.get().getSysRoleKey()));
                if (CollectionUtil.isNotEmpty(defaultRoles)) {
                    subtractUserId.stream().forEach(userId->{
                        sysUserRoles.add(SysUserRole.builder()
                                        .roleId(defaultRoles.stream().findFirst().get().getRoleId())
                                        .userId(userId)
                                .build());
                    });
                    if(CollectionUtil.isNotEmpty(sysUserRoles)){
                        userRoleMapper.batchUserRole(sysUserRoles);
                    }
                }
            }
        }


//        Map<String, Long> userIdMap = userList.stream().collect(Collectors.toMap(SysUser::getWeUserId, SysUser::getUserId));
//        sysUserDeptService.removeByIds(delUserDeptId);

        sysUserDeptService.remove(new LambdaQueryWrapper<SysUserDept>()
                .in(SysUserDept::getWeUserId,userList.stream()
                        .map(SysUser::getWeUserId).collect(Collectors.toList())));
        sysUserDeptService.saveBatch(userDeptList);
//        sysUserDeptService.saveBatch(userDeptList.stream().peek(userDept -> {
//            userDept.setUserId(userIdMap.get(userDept.getWeUserId()));
//        }).collect(Collectors.toList()));

        return userList;
    }

    @Override
    public SysUser selectUserByWeUserId(String weUserId){
         return this.baseMapper.selectUserByWeUserId(weUserId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveUser(String[] weUserIds) {
        List<SysUser> weUsers = new ArrayList<>();
        CollectionUtil.newArrayList(weUserIds).forEach(weUserId -> {
            SysUser sysUser = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getWeUserId, weUserId));
            if (null != sysUser) {
                sysUser.setIsAllocate(CorpUserEnum.NO_IS_ALLOCATE.getKey());
                sysUser.setDimissionTime(new Date());
                weUsers.add(sysUser);
            }
            if (this.updateBatchById(weUsers) && this.removeByIds(weUsers.stream().map(SysUser::getUserId).collect(Collectors.toList()))) {
                //生成等待分配的客户以群记录
                iWeLeaveUserService.createWaitAllocateCustomerAndGroup(weUserId.split(","));
            }

        });

    }

    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_MAIL_LIST)
    public void syncUserAndDept() {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getUserDepartRk(), JSONObject.toJSONString(loginUser));


    }

    @Override
    public void syncUserAndDeptHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
        SecurityContextHolder.setUserType(loginUser.getUserType());

        List<SysDept> deptList = sysDeptService.syncWeDepartment(loginUser.getCorpId());
        deptList.forEach(dept -> {
            this.syncWeUser(dept.getDeptId(), loginUser.getCorpId());
        });

    }

    @Override
    @Transactional
    public SysUser addUser(SysUserDTO sysUser) {
        SysUser user=new SysUser();
        WeUserQuery query = new WeUserQuery();
        query.setUserid(sysUser.getWeUserId());
        query.setCorpid(sysUser.getCorpId());
        AjaxResult<WeUserDetailVo> result = userClient.getUserInfo(query);
        List<SysUserDept> userDeptList = new ArrayList<>();
        if (result.getData() != null) {
            WeUserDetailVo vo = result.getData();
             user = sysUserGenerator(vo);
            for (int i = 0; i < vo.getDepartment().size(); i++) {
                userDeptList.add(userDeptGenerator(user,vo, i));
            }
            this.baseMapper.batchAddOrUpdate(ListUtil.toList(user));
            SysRole role = new LambdaQueryChainWrapper<>(roleMapper).eq(SysRole::getRoleKey, RoleType.WECOME_USER_TYPE_CY.getSysRoleKey())
                    .eq(SysRole::getDelFlag, 0).one();
            if (role != null) {
                userRoleMapper.batchUserRole(ListUtil.toList(
                        SysUserRole.builder().roleId(role.getRoleId()).userId(user.getUserId()).build()));
            }
            SysUser finalUser = user;
            sysUserDeptService.saveBatch(userDeptList.stream().peek(userDept -> {
                userDept.setUserId(finalUser.getUserId());
            }).collect(Collectors.toList()));
        }
        return user;
    }

    @Override
    @Transactional
    public void editUserRole(SysUserDTO user) {

        SysUser sysUser = this.getById(user.getUserId());
        if(null != sysUser){
            //删除原有角色
            userRoleMapper.deleteUserRoleByUserId(sysUser.getUserId());
            List<Long> roleIds = ListUtil.toList(user.getRoleIds());
            if(CollectionUtil.isNotEmpty(roleIds)){

                List<SysRole> sysRoles = roleMapper.selectBatchIds(roleIds);

                if(CollectionUtil.isNotEmpty(sysRoles)){
                    //新增新角色
                    List<SysUserRole> sysUserRoles=new ArrayList<>();
                    sysRoles.stream().forEach(sysUserRole->{
                        //查询角色类
                        sysUserRoles.add(
                                SysUserRole.builder()
                                        .userId(sysUser.getUserId())
                                        .roleId(sysUserRole.getRoleId())
                                        .build()
                        );
                    });
                    userRoleMapper.batchUserRole(sysUserRoles);

                    Set<String> roleKeys
                            = sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());

                    if(roleKeys.contains(RoleType.WECOME_USER_TYPE_FJGLY.getSysRoleKey())){//分级管理员
                        sysUser.setUserType(UserTypes.USER_TYPE_FJ_ADMIN.getSysRoleKey());
                    }else if(roleKeys.contains(RoleType.WECOME_USER_TYPE_CY.getSysRoleKey())){//普通成员
                        sysUser.setUserType(UserTypes.USER_TYPE_COMMON_USER.getSysRoleKey());
                    }else{
                        sysUser.setUserType(UserTypes.USER_TYPE_SELFBUILD_USER.getSysRoleKey());//自建角色
                    }
                    this.updateById(sysUser);
                }



            }
        }
    }

    @Override
    public void batchEditUserRole(Long roleId,List<SysUserDTO> users){

        List<SysUser> sysUsers
                = this.listByIds(users.stream().map(SysUserDTO::getUserId).collect(Collectors.toSet()));

        if(CollectionUtil.isNotEmpty(sysUsers)){
            //删除原有角色用户的当前角色
            userRoleMapper.deleteUserRole(
                    sysUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet()).toArray(new Long[]{}),roleId
            );

            SysRole sysRole = roleMapper.selectRoleById(roleId);

            if(null != sysRole){
                //新增新角色
                List<SysUserRole> sysUserRoles=new ArrayList<>();
                sysUsers.stream().forEach(user->{
                    //查询角色类
                    sysUserRoles.add(
                            SysUserRole.builder()
                                    .userId(user.getUserId())
                                    .roleId(roleId)
                                    .build()
                    );

                    if(RoleType.WECOME_USER_TYPE_FJGLY.getSysRoleKey().contains(sysRole.getRoleKey())){//分级管理员
                        user.setUserType(UserTypes.USER_TYPE_FJ_ADMIN.getSysRoleKey());

                    }else if(RoleType.WECOME_USER_TYPE_CY.getSysRoleKey().contains(sysRole.getRoleKey())){//普通成员

                        user.setUserType(UserTypes.USER_TYPE_COMMON_USER.getSysRoleKey());
                    }else{
                        user.setUserType(UserTypes.USER_TYPE_SELFBUILD_USER.getSysRoleKey());//自建角色
                    }


                });

                userRoleMapper.batchUserRole(sysUserRoles);


//                this.updateBatchById(sysUsers);
                this.baseMapper.batchAddOrUpdate(sysUsers);


            }


        }

    }



    @Override
    public void getUserSensitiveInfo(String userTicket) {
        getUserSensitiveInfo(SecurityUtils.getUserId(),userTicket);
    }

    @Async
    @Override
    public void getUserSensitiveInfo(Long userId, String userTicket) {
        SysUser sysUser = getById(userId);
        if (StringUtils.isNotEmpty(sysUser.getAvatar())) {
            return;
        }
        WeUserQuery query = new WeUserQuery();
        query.setUser_ticket(userTicket);
        WeUserDetailVo data = userClient.getUserSensitiveInfo(query).getData();

        sysUser.setAvatar(data.getAvatar());
        sysUser.setSex(String.valueOf(data.getGender()));
        sysUser.setPhoneNumber(data.getMobile());
        sysUser.setBizMail(data.getBizMail());
        sysUser.setEmail(data.getEmail());
        sysUser.setQrCode(data.getQrCode());
        sysUser.setAddress(data.getAddress());
        updateById(sysUser);
    }

    @Override
    public List<SysUser> findAllSysUser(String weUserIds, String positions, String deptIds) {
        return this.baseMapper.findAllSysUser(weUserIds,positions,deptIds);
    }

    @Override
    public SysUser findOrSynchSysUser(String weuserId) {

        List<SysUser> sysUser = this.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getWeUserId, weuserId));

        if(CollectionUtil.isEmpty(sysUser)){//保存在则从企业微信端获取同时入库

            SysUserDTO sysUserDTO=new SysUserDTO();
            sysUserDTO.setWeUserId(weuserId);
            sysUserDTO.setCorpId(SecurityUtils.getCorpId());
            return this.addUser(sysUserDTO);
        }


        return sysUser.stream().findFirst().get();
    }


    private SysUserDept userDeptGenerator(SysUser user,WeUserDetailVo u, int index) {
        SysUserDept userDept = new SysUserDept();
        userDept.setUserId(user.getUserId());
        userDept.setWeUserId(u.getUserId());
        userDept.setOpenUserid(u.getOpenUserId());
        userDept.setDeptId(Long.parseLong(String.valueOf(u.getDepartment().get(index))));
        if (u.getOrder() != null) {
            userDept.setOrderInDept(String.valueOf(u.getOrder().get(index)));
        } else {
            userDept.setOrderInDept("0");
        }
        if (u.getIsLeaderInDept() != null) {
            userDept.setLeaderInDept(u.getIsLeaderInDept().get(index));
        } else {
            userDept.setLeaderInDept(0);
        }
        return userDept;
    }

    private SysUser sysUserGenerator(WeUserDetailVo u) {
        log.debug("WeUserDetailVo: {}", u);
        SysUser user = null;

        if (user == null) {
            user = new SysUser();
        }
        user.setUserId(SnowFlakeUtil.nextId());
        user.setWeUserId(u.getUserId());
        if (u.getMainDepartment() != null) {
            user.setDeptId(Long.parseLong(String.valueOf(u.getMainDepartment())));
        } else {
            if (u.getDepartment().size() != 0) {
                user.setDeptId(Long.parseLong(String.valueOf(u.getDepartment().get(0))));
            } else {
                user.setDeptId(1L);
            }
        }
        user.setUserName(u.getName());
        user.setPosition(u.getPosition());
        user.setPhoneNumber(u.getMobile());
        user.setSex(String.valueOf(u.getGender()));
        user.setEmail(u.getEmail());
        user.setBizMail(u.getBizMail());
        if (u.getDirectLeader() != null) {
            user.setLeader(String.join(",", u.getDirectLeader()));
        }
        user.setAvatar(u.getAvatar());
        user.setThumbAvatar(u.getThumbAvatar());
        user.setTelephone(u.getTelephone());
        user.setNickName(u.getAlias());
        user.setExtAttr(u.getExtAttr());
        if (u.getStatus() != null) {
            user.setWeUserStatus(String.valueOf(u.getStatus()));
        } else {
            user.setWeUserStatus("1");
        }
        user.setQrCode(u.getQrCode());
        user.setExternalProfile(u.getExternalProfile());
        user.setExternalPosition(u.getExternalPosition());
        user.setAddress(u.getAddress());
        user.setCreateBy(SecurityUtils.getUserName());
        user.setCreateById(SecurityUtils.getUserId());
        user.setCreateTime(new Date());
        user.setUpdateBy(SecurityUtils.getUserName());
        user.setUpdateById(SecurityUtils.getUserId());
        user.setUpdateTime(new Date());
        user.setStatus("0");
        return user;
    }


    @Override
    public List<SysUserVo> getUserListByWeUserIds(SysUserQuery query) {
        List<SysUser> sysUserList = list(new LambdaQueryWrapper<SysUser>()
                .in(CollectionUtil.isNotEmpty(query.getWeUserIds()), SysUser::getWeUserId, query.getWeUserIds())
                .in(CollectionUtil.isNotEmpty(query.getDeptIds()),SysUser::getDeptId,query.getDeptIds())
                .eq(SysUser::getDelFlag, 0));
        if(CollectionUtil.isNotEmpty(sysUserList)){
            List<SysUserVo> list = sysUserList.stream().map(item -> {
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtil.copyProperties(item, sysUserVo);
                return sysUserVo;
            }).collect(Collectors.toList());
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> screenConditWeUser(String weUserIds, String deptIds,String positions) {
        List<String> weUserIdList=new ArrayList<>();

        if(StringUtils.isNotEmpty(weUserIds)){
            weUserIdList.addAll(
                    ListUtil.toList(weUserIds.split(","))
            );
        }


        if(StringUtils.isNotEmpty(positions) || StringUtils.isNotEmpty(deptIds)){
            List<SysUser> allSysUser = this.baseMapper.findAllSysUser(null,positions,deptIds);
            if(CollectionUtil.isNotEmpty(allSysUser)){
                weUserIdList.addAll(
                        allSysUser.stream().map(SysUser::getWeUserId).collect(Collectors.toList())
                );
            }
        }
        return weUserIdList;
    }

    @Override
    @Transactional
    public void builderLeaveSysUser(List<SysUser> sysUsers) {
        if(CollectionUtil.isNotEmpty(sysUsers)){
            this.updateBatchById(sysUsers);
            this.removeByIds(sysUsers.stream().map(SysUser::getUserId).collect(Collectors.toList()));
        }

    }


}
