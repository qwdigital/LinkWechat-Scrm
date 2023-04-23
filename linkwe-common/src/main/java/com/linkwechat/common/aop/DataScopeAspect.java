package com.linkwechat.common.aop;

import cn.hutool.core.util.ArrayUtil;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.DataScopeType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 数据过滤处理
 *
 * @author leejoker
 */
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
//                dataScopeFilterForRole(joinPoint, currentUser, controllerDataScope);
                dataScopeFilterForSysUser(joinPoint, currentUser, controllerDataScope);
            }
        }
    }

//    /**
//     * 数据范围过滤(角色绑定数据权限)
//     *
//     * @param joinPoint 切点
//     * @param user      用户
//     * @param controllerDataScope 部门别名
//     */
//    public static void dataScopeFilterForRole(JoinPoint joinPoint, SysUser user, DataScope controllerDataScope) {
//        StringBuilder sqlString = new StringBuilder();
//
//        String type = controllerDataScope.type();
//
//        for (SysRole role : user.getRoles()) {
//            String dataScope = role.getDataScope();
//            if (DataScopeType.DATA_SCOPE_ALL.equals(dataScope)) {
//                sqlString = new StringBuilder();
//                break;
//            } else if (DataScopeType.DATA_SCOPE_CUSTOM.equals(dataScope)) {
//                if(type.equals("1")){
//                    sqlString.append(StringUtils.format(
//                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", controllerDataScope.deptAlias(),
//                            role.getRoleId()));
//                }else {
//                    DataColumn dataColumn = controllerDataScope.value()[0];
//                    sqlString.append(StringUtils.format(
//                            " or {}.{} in ( select distinct sud.{} from sys_role_dept srd inner join sys_user_dept sud on srd.dept_id= sud.dept_id and sud.del_flag = 0  where srd.role_id = {} )",
//                            dataColumn.alias(), dataColumn.name(),dataColumn.userid(), role.getRoleId()));
//                }
//            } else if (DataScopeType.DATA_SCOPE_DEPT.equals(dataScope)) {
//                if(type.equals("1")){
//                    sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", controllerDataScope.deptAlias(), user.getDeptId()));
//                }else {
//                    DataColumn dataColumn = controllerDataScope.value()[0];
//                    sqlString.append(StringUtils.format(" or {}.{} in ( select {} from sys_user_dept where dept_id= {}  and del_flag = 0 ) ",
//                            dataColumn.alias(), dataColumn.name(),dataColumn.userid(),user.getDept()));
//                }
//            } else if (DataScopeType.DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
//                if(type.equals("1")){
//                    sqlString.append(StringUtils.format(
//                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
//                            controllerDataScope.deptAlias(), user.getDeptId(), user.getDeptId()));
//                }else {
//                    DataColumn dataColumn = controllerDataScope.value()[0];
//                    sqlString.append(StringUtils.format(
//                            " or {}.{} in ( select distinct sud.{} from sys_dept sd inner join sys_user_dept sud on sd.dept_id = sud.dept_id and sud.del_flag = 0 where (sd.dept_id = {} or find_in_set( {} , sd.ancestors )) and sd.del_flag = 0 ) ",
//                            dataColumn.alias(), dataColumn.name(), dataColumn.userid(), user.getDeptId(), user.getDeptId()));
//                }
//            } else if (DataScopeType.DATA_SCOPE_SELF.equals(dataScope)) {
//                if(type.equals("1")){
//                    if (StringUtils.isNotBlank(controllerDataScope.userAlias())) {
//                        sqlString.append(StringUtils.format(" OR {}.user_id = {} ", controllerDataScope.userAlias(), user.getUserId()));
//                    } else {
//                        // 数据权限为仅本人且没有userAlias别名不查询任何数据
//                        sqlString.append(" OR 1=0 ");
//                    }
//                }else {
//                    DataColumn dataColumn = controllerDataScope.value()[0];
//                    sqlString.append(StringUtils.format(" or {}.{} in ( select {} from sys_user where user_id = {} and del_flag = 0 ) ",
//                            dataColumn.alias(), dataColumn.name(), dataColumn.userid(), user.getUserId()));
//                }
//            }
//        }
//
//        if (StringUtils.isNotBlank(sqlString.toString()) && joinPoint.getArgs().length > 0) {
//            Object params = joinPoint.getArgs()[0];
//            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
//                BaseEntity baseEntity = (BaseEntity) params;
//                baseEntity.getParams().put(DATA_SCOPE, " (" + sqlString.substring(4) + ")");
//            }
//        }
//    }


    /**
     * 数据范围过滤(员工绑定数据权限)
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param controllerDataScope 部门别名
     */
    public static void dataScopeFilterForSysUser(JoinPoint joinPoint, SysUser user, DataScope controllerDataScope){
        StringBuilder sqlString = new StringBuilder();

        if(null != user){
            DataScopeType type = DataScopeType.of(String.valueOf(user.getDataScope()));

            switch (type){
                case DATA_SCOPE_ALL:
                    sqlString = new StringBuilder();
                    return;
                case DATA_SCOPE_CUSTOM:
                    if(type.equals("1")){
                        sqlString.append(StringUtils.format(
                                " OR {}.dept_id IN ( SELECT dept_id FROM sys_user_manage_scop WHERE user_id = {} ) ", controllerDataScope.deptAlias(),
                                user.getUserId()));
                    }else {
                        if(ArrayUtil.isNotEmpty(controllerDataScope.value())){
                            DataColumn dataColumn = controllerDataScope.value()[0];
                            sqlString.append(StringUtils.format(
                                    " or {}.{} in ( select distinct sud.{} from sys_user_manage_scop srd inner join sys_user_dept sud on srd.dept_id= sud.dept_id and sud.del_flag = 0  where srd.user_id = {} )",
                                    dataColumn.alias(), dataColumn.name(),dataColumn.userid(), user.getUserId()));
                        }

                    }
                    break;
                case DATA_SCOPE_DEPT:
                    if(type.equals("1")){
                        sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", controllerDataScope.deptAlias(), user.getDeptId()));
                    }else {


                        if(StringUtils.isNotEmpty(user.getDeptIds())){
                            if(ArrayUtil.isNotEmpty(controllerDataScope.value())){
                                DataColumn dataColumn = controllerDataScope.value()[0];
                                sqlString.append(StringUtils.format(" or {}.{} in (  SELECT {} from sys_user_dept where dept_id in ({}) ) ",
                                        dataColumn.alias(), dataColumn.name(),dataColumn.userid(),user.getDeptIds()));
                            }


                        }else{
                            if(user.getDeptId() != null){
                                if(ArrayUtil.isNotEmpty(controllerDataScope.value())){
                                    DataColumn dataColumn = controllerDataScope.value()[0];
                                    sqlString.append(StringUtils.format(" or {}.{} in ( SELECT {} from sys_user_dept where dept_id in ({}) ) ",
                                            dataColumn.alias(), dataColumn.name(),dataColumn.userid(),user.getDeptId()));
                                }

                            }
                        }


                    }
                    break;
                case DATA_SCOPE_DEPT_AND_CHILD:
                    if(type.equals("1")){
                        sqlString.append(StringUtils.format(
                                " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                                controllerDataScope.deptAlias(), user.getDeptId(), user.getDeptId()));
                    }else {
                        if(ArrayUtil.isNotEmpty(controllerDataScope.value())){

                            DataColumn dataColumn = controllerDataScope.value()[0];
                            sqlString.append(StringUtils.format(
                                    " or {}.{} in ( select distinct sud.{} from sys_dept sd inner join sys_user_dept sud on sd.dept_id = sud.dept_id and sud.del_flag = 0 where (sd.dept_id = {} or find_in_set( {} , sd.ancestors )) and sd.del_flag = 0 ) ",
                                    dataColumn.alias(), dataColumn.name(), dataColumn.userid(), user.getDeptId(), user.getDeptId()));
                        }

                    }
                    break;
                case DATA_SCOPE_SELF:
                    if(type.equals("1")){
                        if (StringUtils.isNotBlank(controllerDataScope.userAlias())) {
                            sqlString.append(StringUtils.format(" OR {}.user_id = {} ", controllerDataScope.userAlias(), user.getUserId()));
                        } else {
                            // 数据权限为仅本人且没有userAlias别名不查询任何数据
                            sqlString.append(" OR 1=0 ");
                        }
                    }else {

                        if(ArrayUtil.isNotEmpty(controllerDataScope.value())){
                            DataColumn dataColumn = controllerDataScope.value()[0];
                            sqlString.append(StringUtils.format(" or {}.{} in ( select {} from sys_user where user_id = {} and del_flag = 0 ) ",
                                    dataColumn.alias(), dataColumn.name(), dataColumn.userid(), user.getUserId()));
                        }

                    }
                    break;
                default:
                    break;

            }



        }


        if (StringUtils.isNotBlank(sqlString.toString()) && joinPoint.getArgs().length > 0) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " (" + sqlString.substring(4) + ")");
            }
        }



    }


    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if(ArrayUtil.isNotEmpty(args)){
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, "");
            }
        }

    }
}
