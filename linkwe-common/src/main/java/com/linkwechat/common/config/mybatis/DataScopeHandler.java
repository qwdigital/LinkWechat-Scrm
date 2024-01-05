package com.linkwechat.common.config.mybatis;

import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.entity.SysUser;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * @author danmo
 * @Description 数据权限处理逻辑
 * @date 2022/5/10 21:48
 **/
public interface DataScopeHandler {
    /**
     * 全部数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    default void setWhereForAll(DataScope dataScope, SysUser sysUser){
        // do nothing
    }




    /**
     * 自定数据权限(员工绑定部门)
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    String setWhereForSysUser(DataScope dataScope, SysUser sysUser);

    /**
     * 部门数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    String setWhereForDept(DataScope dataScope, SysUser sysUser);

    /**
     * 部门及以下数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    String setWhereForDeptAndChild(DataScope dataScope, SysUser sysUser);

    /**
     * 仅本人数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    String setWhereForSelf(DataScope dataScope, SysUser sysUser);
}
