package com.linkwechat.common.utils;

import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.entity.SysUser;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * 数据权限sql构造
 */
public class DataScopeSqlUtils {

//    /**
//     * 自定数据权限(角色绑定数据权限场景sql构建)
//     *
//     * @param dataScope   数据范围注解
//     * @param roleId
//     * @throws JSQLParserException SQL解析异常
//     */
//    public  static String setWhereForRole(DataScope dataScope,Long roleId){
//        StringBuilder sqlPart = new StringBuilder();
//        if(dataScope.type().equals("1")){
//            sqlPart.append(StringUtils.format(
//                    " or {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", dataScope.deptAlias(),
//                    roleId));
//        }else {
//            DataColumn dataColumn = dataScope.value()[0];
//            if(StringUtils.isNotEmpty(dataColumn.alias())){
//                sqlPart.append(StringUtils.format(
//                        " or {}.{} in ( select distinct su.{} from sys_role_dept srd inner join sys_user_dept sud on srd.dept_id= sud.dept_id and sud.del_flag = 0 LEFT JOIN sys_user su ON su.we_user_id=sud.we_user_id where srd.role_id = {} )",
//                        dataColumn.alias(), dataColumn.name(),dataColumn.userid(), roleId));
//            }else{
//
//                sqlPart.append(StringUtils.format(
//                        " or {} in ( select distinct su.{} from sys_role_dept srd inner join sys_user_dept sud on srd.dept_id= sud.dept_id and sud.del_flag = 0 LEFT JOIN sys_user su ON su.we_user_id=sud.we_user_id where srd.role_id = {} )",
//                        dataColumn.name(),dataColumn.userid(), roleId));
//
//            }
//        }
//        return sqlPart.toString();
//    }


    /**
     * 自定数据权限
     * @param dataScope
     * @param sysUser
     * @return
     */
    public static String setWhereForSysUser(DataScope dataScope, SysUser sysUser){

        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(
                    " or {}.dept_id IN ( SELECT dept_id FROM sys_user_manage_scop WHERE user_id = {} ) ", dataScope.deptAlias(),
                    sysUser.getUserId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            if(StringUtils.isNotEmpty(dataColumn.alias())){
                sqlPart.append(StringUtils.format(
                        " or {}.{} in ( SELECT DISTINCT {} FROM sys_user_dept WHERE del_flag=0 AND dept_id IN (SELECT dept_id FROM sys_user_manage_scop where user_id={}) )",
                        dataColumn.alias(), dataColumn.name(),dataColumn.userid(), sysUser.getUserId()));
            }else{

                sqlPart.append(StringUtils.format(
                        " or {} in ( SELECT DISTINCT {} FROM sys_user_dept WHERE del_flag=0 AND dept_id IN (SELECT dept_id FROM sys_user_manage_scop where user_id={}) )",
                        dataColumn.name(),dataColumn.userid(), sysUser.getUserId()));

            }
        }
        return sqlPart.toString();
    }



    /**
     * 本部门数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    public static String setWhereForDept(DataScope dataScope, SysUser sysUser){

        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(" or {}.dept_id = {} ", dataScope.deptAlias(), sysUser.getDeptId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            if(StringUtils.isNotEmpty(dataColumn.alias())){
                 sqlPart.append(StringUtils.format(" or {}.{} in ( SELECT {} from sys_user_dept where dept_id in ({}) ) ",
                        dataColumn.alias(), dataColumn.name(),dataColumn.userid(),StringUtils.isEmpty(sysUser.getDeptIds())?sysUser.getDeptId():sysUser.getDeptIds()));

            }else{
                sqlPart.append(StringUtils.format(" or {} in ( SELECT {} from sys_user_dept where dept_id in ({}) ) ",
                            dataColumn.name(),dataColumn.userid(),StringUtils.isEmpty(sysUser.getDeptIds())?sysUser.getDeptId():sysUser.getDeptIds()));
            }


        }
        return sqlPart.toString();

    }



    /**
     * 部门及以下数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    public static String setWhereForDeptAndChild(DataScope dataScope, SysUser sysUser){

        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(
                    " or {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                    dataScope.deptAlias(), sysUser.getDeptId(), sysUser.getDeptId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            if(StringUtils.isNotEmpty(dataColumn.alias())){
                sqlPart.append(StringUtils.format(" or {}.{} in ( SELECT sud.{} FROM sys_user_dept sud WHERE sud.dept_id IN (SELECT dept_id FROM sys_dept WHERE FIND_IN_SET({},ancestors) or dept_id={} ) ) ",
                        dataColumn.alias(), dataColumn.name(),dataColumn.userid(),sysUser.getDeptId(),sysUser.getDeptId()));
            }else{
                sqlPart.append(StringUtils.format(" or {} in ( SELECT sud.{} FROM sys_user_dept sud WHERE sud.dept_id IN (SELECT dept_id FROM sys_dept WHERE FIND_IN_SET({},ancestors)  or dept_id={} ) ) ",
                        dataColumn.name(),dataColumn.userid(),sysUser.getDeptId(),sysUser.getDeptId()));
            }
        }
        return sqlPart.toString();
    }



    /**
     * 仅本人数据权限
     *
     * @param dataScope   数据范围注解
     * @param sysUser
     * @throws JSQLParserException SQL解析异常
     */
    public static String setWhereForSelf(DataScope dataScope, SysUser sysUser){

        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(" AND {}.user_id = {} ", dataScope.userAlias(), sysUser.getUserId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            if(StringUtils.isNotEmpty( dataColumn.alias())){
                sqlPart.append(StringUtils.format(" or {}.{} in ( select {} from sys_user where user_id = {} and del_flag = 0 ) ",
                        dataColumn.alias(), dataColumn.name(), dataColumn.userid(), sysUser.getUserId()));
            }else{
                sqlPart.append(StringUtils.format(" or {} in ( select {} from sys_user where user_id = {} and del_flag = 0 ) ",
                        dataColumn.name(), dataColumn.userid(), sysUser.getUserId()));
            }

        }
        return sqlPart.toString();
    }

}
