package com.linkwechat.common.config.mybatis;

import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.utils.StringUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * @author sxw
 * @description
 * @date 2022/5/11 11:40
 **/
public class PearlDataScopeHandler implements DataScopeHandler {

    @Override
    public String setWhereForCustom(PlainSelect plainSelect, DataScope dataScope, SysUser sysUser, Long roleId) {
        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(
                    " or {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", dataScope.deptAlias(),
                    roleId));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            sqlPart.append(StringUtils.format(
                    " or {}.{} in ( select distinct su.{} from sys_role_dept srd inner join sys_user_dept sud on srd.dept_id= sud.dept_id and sud.del_flag = 0 LEFT JOIN sys_user su ON su.we_user_id=sud.we_user_id where srd.role_id = {} )",
                    dataColumn.alias(), dataColumn.name(),dataColumn.userid(), roleId));
        }
        return sqlPart.toString();
    }

    @Override
    public String setWhereForDept(PlainSelect plainSelect, DataScope dataScope, SysUser sysUser) {
        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(" or {}.dept_id = {} ", dataScope.deptAlias(), sysUser.getDeptId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            sqlPart.append(StringUtils.format(" or {}.{} in ( select {} from sys_user_dept where dept_id= {} and del_flag = 0 ) ",
                    dataColumn.alias(), dataColumn.name(),dataColumn.userid(),sysUser.getDept()));
        }
        return sqlPart.toString();
    }

    @Override
    public String setWhereForDeptAndChild(PlainSelect plainSelect, DataScope dataScope, SysUser sysUser) {
        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(
                    " or {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                    dataScope.deptAlias(), sysUser.getDeptId(), sysUser.getDeptId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            sqlPart.append(StringUtils.format(
                    " or {}.{} in ( select distinct sud.{} from sys_dept sd inner join sys_user_dept sud on sd.dept_id = sud.dept_id and sud.del_flag = 0 where (sd.dept_id = {} or find_in_set( {} , sd.ancestors )) and sd.del_flag = 0 ) ",
                    dataColumn.alias(), dataColumn.name(), dataColumn.userid(), sysUser.getDeptId(), sysUser.getDeptId()));
        }
        return sqlPart.toString();
    }

    @Override
    public String setWhereForSelf(PlainSelect plainSelect, DataScope dataScope, SysUser sysUser) {
        StringBuilder sqlPart = new StringBuilder();
        if(dataScope.type().equals("1")){
            sqlPart.append(StringUtils.format(" or {}.user_id = {} ", dataScope.userAlias(), sysUser.getUserId()));
        }else {
            DataColumn dataColumn = dataScope.value()[0];
            if(StringUtils.isEmpty( dataColumn.alias())){
                sqlPart.append(StringUtils.format(" or {} in ( select {} from sys_user where user_id = {} and del_flag = 0 ) ",
                         dataColumn.name(), dataColumn.userid(), sysUser.getUserId()));
            }else{
                sqlPart.append(StringUtils.format(" or {}.{} in ( select {} from sys_user where user_id = {} and del_flag = 0 ) ",
                        dataColumn.alias(), dataColumn.name(), dataColumn.userid(), sysUser.getUserId()));
            }

        }
        return sqlPart.toString();
    }

    public void setWhere(PlainSelect plainSelect, Expression expression) {
        Expression where = plainSelect.getWhere();
        if (where == null) {
            // 不存在 where 条件
            plainSelect.setWhere(new Parenthesis(expression));
        } else {
            // 存在 where 条件 and 处理
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), expression));
        }
    }
}
