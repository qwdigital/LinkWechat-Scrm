package com.linkwechat.web.controller.system;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysMenu;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.domain.WeConfigParamInfo;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WxUser;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.framework.service.TokenService;
import com.linkwechat.service.*;
import com.linkwechat.web.domain.vo.CorpVo;
import com.linkwechat.web.domain.vo.UserVo;
import com.linkwechat.web.mapper.SysUserMapper;
import com.linkwechat.web.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "组织管理")
@Slf4j
public class SysUserController extends BaseController {
    @Resource
    private ISysUserService userService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysPostService postService;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysUserMapper sysUserMapper;


    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IWeSysFieldTemplateService iWeSysFieldTemplateService;


    @Autowired
    private IWeStrackStageService iWeStrackStageService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "用户列表")
    public TableDataInfo list(SysUser user) {

        if(user.isCheckIsRoot()&&iSysDeptService.isRoot(user.getDeptId())){ //判断是不是跟节点，如果是查询条件为查询所有部门下的包括本部门的数据
            user.setDeptId(null);
        }
        List<UserVo> userVos = userService.selectUserVoList(user, TableSupport.buildPageRequest());
        TableDataInfo dataTable
                = getDataTable(userVos);
        dataTable.setTotal(
                userService.selectCountUserDeptList(user)
        );
        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_MAIL_LIST)
        );//最近同步时间

        return dataTable;
    }


    /**
     * 获取所有员工
     *
     * @return
     */
    @GetMapping("/listAll")
    public AjaxResult<List<SysUser>> listAll(String userName) {
        return AjaxResult.success(userService.list(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getDelFlag, Constants.COMMON_STATE).like(StringUtils.isNotEmpty(userName), SysUser::getUserName, userName)
        ));
    }

    @GetMapping("/export")
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }


    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUserName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin())
                .collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleIdsByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PostMapping("/callback/add")
    public AjaxResult callBackAdd(@RequestBody SysUserQuery query) {
        userService.addUser(query);
        return AjaxResult.success();
    }

    /**
     * 修改用户
     */
    @PutMapping("/callback/edit")
    public AjaxResult callbackEdit(@RequestBody SysUserQuery query) {
        userService.updateUser(query);
        return AjaxResult.success();
    }

    /**
     * 回掉移除用户
     *
     * @param weUserIds
     * @return
     */
    @DeleteMapping("/callback/{userIds}")
    public AjaxResult callBackRemove(@PathVariable("userIds") List<String> weUserIds) {
        userService.leaveUser(weUserIds);
        return AjaxResult.success();
    }

    /**
     * 修改用户角色
     *
     * @param user
     * @return
     */
    @PutMapping("/editUserRole")
    public AjaxResult editUserRole(@Validated @RequestBody SysUserDTO user) {

        userService.editUserRole(user);

        return AjaxResult.success();
    }

    /**
     * 编辑数据权限
     *
     * @return
     */
    @PostMapping("/editDataScop")
    public AjaxResult editDataScop(@RequestBody SysUserDTO user) {
        userService.editDataScop(user);
        return AjaxResult.success();
    }

    /**
     * 删除用户
     */
    ////@PreAuthorize("@ss.hasPermi('system:user:remove')")
//    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable String[] userIds) {
        userService.leaveUser(ListUtil.toList(userIds));
        return AjaxResult.success();
    }


    /**
     * 回掉移除用户
     *
     * @param corpId
     * @param userIds
     * @return
     */
    @DeleteMapping("/callBackRemove/{corpId}/{userIds}")
    public AjaxResult callBackRemove(@PathVariable String corpId, @PathVariable String[] userIds) {

        userService.leaveUser(ListUtil.toList(userIds));

        return AjaxResult.success();

    }

    /**
     * 重置密码
     */
    @Deprecated
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUserName());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    ////@PreAuthorize("@ss.hasPermi('system:user:edit')")
//    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUserName());
        return toAjax(userService.updateUserStatus(user));
    }


    @GetMapping("/findCurrentLoginUser")
    public AjaxResult<LoginUser> findCurrentLoginUser(HttpServletRequest request) {
        return AjaxResult.success(tokenService.getLoginUser(request));
    }


    /**
     * 同步组织架构与成员
     *
     * @return
     */
    @PostMapping("/sync")
    public AjaxResult syncUserAndDept() {

        userService.syncUserAndDept();

        return AjaxResult.success();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getSysUser();
        //暂时忽略租户，之后统一处理
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);

        SysDept sysDept = new SysDept();
        sysDept.setParentId(0L);
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(sysDept);
        if (!CollectionUtils.isEmpty(sysDepts)) {
            user.setCompanyName(sysDepts.stream().findFirst().get().getDeptName());
        }
        sysDept = iSysDeptService.selectDeptById(user.getDeptId());

        if (null != sysDept) {
            user.setDeptName(sysDept.getDeptName());
        }

        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        CorpVo corpVo = weCorpAccount == null ? new CorpVo() : new CorpVo(weCorpAccount.getCorpId(), weCorpAccount.getCompanyName(),
                weCorpAccount.getAgentId());


        WeConfigParamInfo configParamInfo = new WeConfigParamInfo();

        if (null != weCorpAccount) {
            iWeSysFieldTemplateService.initBaseSysField();
            iWeStrackStageService.initStrackStage();
            if (StringUtils.isNotEmpty(corpVo.getAppId())
                    && StringUtils.isNotEmpty(corpVo.getSecret())) {
                configParamInfo.setWeAppParamFill(true);
            }
            if (StringUtils.isNotEmpty(weCorpAccount.getChatSecret())
                    && StringUtils.isNotEmpty(weCorpAccount.getFinancePrivateKey())) {
                configParamInfo.setChatParamFill(true);
            }

            if (StringUtils.isNotEmpty(weCorpAccount.getMerChantName())
                    && StringUtils.isNotEmpty(weCorpAccount.getMerChantNumber())
                    && StringUtils.isNotEmpty(weCorpAccount.getMerChantSecret())
                    && StringUtils.isNotEmpty(weCorpAccount.getCertP12Url())) {
                configParamInfo.setRedEnvelopesParamFile(true);
            }

        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("corpInfo", corpVo);
        ajax.put("configParamInfo", configParamInfo);
        return ajax;
    }

    @GetMapping("/getUserSensitiveInfo")
    public AjaxResult<WeUserDetailVo> getUserSensitiveInfo(@RequestParam("userTicket") String userTicket) {
        userService.getUserSensitiveInfo(userTicket);
        return AjaxResult.success();
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        // 用户信息
        Long userId = SecurityUtils.getUserId();
        //暂时忽略租户，之后统一处理
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    /**
     * 同步业务逻辑，mq调用
     *
     * @param msg
     */
    @PostMapping("/syncUserHandler")
    public AjaxResult syncUserHandler(@RequestBody JSONObject msg) {
        userService.syncUserHandler(msg);
        return AjaxResult.success();
    }

    @GetMapping("/info/{id}")
    public AjaxResult getUserInfoById(@PathVariable("id") Long userId) {
        SysUser user = sysUserMapper.selectUserById(userId);
        return AjaxResult.success(user);
    }

    @GetMapping("/getUserInfo/{weUserId}")
    public AjaxResult<SysUser> getInfo(@PathVariable("weUserId") String weUserId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysUser::getWeUserId, weUserId);
        SysUser user = sysUserMapper.selectOne(queryWrapper);
        return AjaxResult.success(user);
    }


    @PostMapping("listByQuery")
    public AjaxResult<SysUser> listByQuery(@RequestBody SysUser sysUser) {
        List<SysUser> sysUsers = userService.selectUserList(sysUser);
        return AjaxResult.success(sysUsers);
    }

    /**
     * 获取微信用户信息
     *
     * @return
     */
    @GetMapping("/getWxInfo")
    public AjaxResult<WxUser> getWxInfo() {
        WxLoginUser wxLoginUser = SecurityUtils.getWxLoginUser();
        WxUser customerInfo = wxUserService.getCustomerInfo(wxLoginUser.getOpenId(), wxLoginUser.getUnionId());
        customerInfo.setEnableFilePreview(linkWeChatConfig.isEnableFilePreview());
        return AjaxResult.success(customerInfo);
    }


    /**
     * 根据weUserIds，positions，deptIds批量查询
     *
     * @param weUserIds
     * @param positions
     * @param deptIds
     * @return
     */
    @GetMapping("/findAllSysUser")
    public AjaxResult<List<SysUser>> findAllSysUser(String weUserIds, String positions, String deptIds) {
        return AjaxResult.success(userService.findAllSysUser(weUserIds, positions, deptIds));
    }

    /**
     * 根据weuserid获取员工，如果没有则从企业微信端同步
     *
     * @param weuserId
     * @return
     */
    @GetMapping("/findOrSynchSysUser/{weuserId}")
    public AjaxResult<SysUser> findOrSynchSysUser(@PathVariable("weuserId") String weuserId) {
        return AjaxResult.success(
                userService.findOrSynchSysUser(weuserId)
        );
    }


    /**
     * 更新用户是否开启会话
     *
     * @param
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2022/09/19 18:02
     */
    @PutMapping("/update/open/chat")
    public AjaxResult<Boolean> updateUserIsOpenChat(@RequestBody SysUser sysUser) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysUser::getIsOpenChat, sysUser.getIsOpenChat());
        updateWrapper.lambda().eq(SysUser::getWeUserId, sysUser.getWeUserId());
        boolean update = userService.update(updateWrapper);
        return AjaxResult.success(update);
    }

    /**
     * 根据openUserId获取用户数据
     *
     * @param
     * @return {@link AjaxResult<SysUser>}
     * @author WangYX
     * @date 2022/09/21 11:04
     */
    @GetMapping("/getOneByOpenUserId")
    public AjaxResult<SysUser> getOneByOpenUserId(@RequestBody SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getOpenUserid, sysUser.getOpenUserid());
        queryWrapper.lambda().eq(SysUser::getDelFlag, 0);
        SysUser one = userService.getOne(queryWrapper);
        return AjaxResult.success(one);
    }

    /**
     * 更新用户是否开启客服
     *
     * @param
     * @return {@link AjaxResult}
     * @author damo
     * @date 2022/09/19 18:02
     */
    @PutMapping("/update/kf/status")
    public AjaxResult updateUserKfStatus(@RequestBody SysUser sysUser) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(SysUser::getKfStatus, sysUser.getKfStatus());
        updateWrapper.lambda().eq(SysUser::getWeUserId, sysUser.getWeUserId());
        userService.update(updateWrapper);
        return AjaxResult.success();
    }

    /**
     * 更新员工开启会话存档状态
     *
     * @param query
     * @return
     */
    @PutMapping("/update/chat/status")
    public AjaxResult updateUserChatStatus(@RequestBody SysUserQuery query) {
        userService.updateUserChatStatus(query);
        return AjaxResult.success();
    }

    /**
     * 通过企微员工ID获取员工信息
     *
     * @param query
     * @return
     */
    @PostMapping("/getUserListByWeUserIds")
    public AjaxResult<List<SysUserVo>> getUserListByWeUserIds(@Validated @RequestBody SysUserQuery query) {
        List<SysUserVo> sysUserList = userService.getUserListByWeUserIds(query);
        return AjaxResult.success(sysUserList);
    }


    /**
     * 根据职位等条件筛选员工
     *
     * @param weUserIds
     * @param deptIds
     * @param positions
     * @return
     */
    @GetMapping("/screenConditWeUser")
    public AjaxResult<List<String>> screenConditWeUser(String weUserIds, String deptIds, String positions) {


        return AjaxResult.success(
                userService.screenConditWeUser(weUserIds, deptIds, positions)
        );

    }


    /**
     * 批量构建离职员工
     *
     * @param sysUsers
     * @return
     */
    @PostMapping("/builderLeaveSysUser")
    public AjaxResult builderLeaveSysUser(@RequestBody SysUserQuery sysUsers) {

        userService.builderLeaveSysUser(sysUsers.getSysUsers());


        return AjaxResult.success();

    }

    /**
     * 更新用户动态日报开启状态
     *
     * @param openDaily 0开启，1关闭
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/26 10:04
     */
    @PutMapping("/update/open/daily")
    public AjaxResult updateOpenDaily(@Validated @RequestParam("openDaily") Integer openDaily) {
        LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate(SysUser.class);
        updateWrapper.eq(SysUser::getUserId, SecurityUtils.getLoginUser().getSysUser().getUserId());
        updateWrapper.set(SysUser::getOpenDaily, openDaily);
        userService.update(updateWrapper);
        return AjaxResult.success();
    }


    /**
     * 根据id查询指定员工
     * @param query
     * @return
     */
    @PostMapping("/findSysUser")
    public AjaxResult<List<SysUser>> findSysUser(@RequestBody SysUserQuery query){
        return AjaxResult.success(
                userService.list(new LambdaQueryWrapper<SysUser>()
                        .in(SysUser::getWeUserId,query.getWeUserIds()))
        );
    }


}
