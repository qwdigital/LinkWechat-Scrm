package com.linkwechat.common.aop;

import cn.hutool.core.util.ArrayUtil;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.DataScopeType;
import com.linkwechat.common.utils.DataScopeSqlUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                dataScopeFilterForSysUser(joinPoint, currentUser, controllerDataScope);
            }
        }
    }

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
            log.error("DataScopeType"+type);
            switch (type){
                case DATA_SCOPE_ALL:
                    sqlString=new StringBuilder();
                    return;
                case DATA_SCOPE_CUSTOM:
                    sqlString.append( DataScopeSqlUtils.setWhereForSysUser(controllerDataScope,user));
                    break;
                case DATA_SCOPE_DEPT:
                    sqlString.append(DataScopeSqlUtils.setWhereForDept(controllerDataScope, user));
                    break;
                case DATA_SCOPE_DEPT_AND_CHILD:
                    sqlString.append(DataScopeSqlUtils.setWhereForDeptAndChild(controllerDataScope, user));
                    break;
                case DATA_SCOPE_SELF:
                    sqlString.append(DataScopeSqlUtils.setWhereForSelf(controllerDataScope, user));
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
