package com.linkwechat.common.config.mybatis;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.DataScopeType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class DataScopeInterceptor extends JsqlParserSupport implements InnerInterceptor {

    private PearlDataScopeHandler pearlDataScopeHandler;

    public DataScopeInterceptor(PearlDataScopeHandler pearlDataScopeHandler) {
        this.pearlDataScopeHandler = pearlDataScopeHandler;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) {
        DataScope dataScope = null;
        try {
            dataScope = getDataScope(ms);
        } catch (ClassNotFoundException e) {
            log.error("获取注解失败:", e);
            throw ExceptionUtils.mpe("获取注解失败", e);
        }
        //没有注解直接放行
        if (dataScope == null) {
            return;
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        String originalSql = boundSql.getSql();

        try {
            Statement statement = CCJSqlParserUtil.parse(originalSql);

            if (statement instanceof Select) {
                Select select = (Select) statement;
                // 解析SQL
                this.processSelect(select, dataScope);
                final String parserSQL = statement.toString();
                mpBs.sql(parserSQL);
                if (log.isDebugEnabled()) {
                    log.debug("parser sql: " + parserSQL);
                }
            }
        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e, originalSql);
        }
    }

    /**
     * 通过反射获取mapper方法是否加了数据拦截注解
     */
    private DataScope getDataScope(MappedStatement sm) throws ClassNotFoundException {
        DataScope dataScope = null;
        String id = sm.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        final Class<?> cls = Class.forName(className);
        final Method[] methods = cls.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.isAnnotationPresent(DataScope.class)) {
                dataScope = method.getAnnotation(DataScope.class);
                break;
            }
        }
        return dataScope;
    }

    protected void processSelect(Select select, DataScope dataScope) {
        processSelectBody(select.getSelectBody(), dataScope);
        List<WithItem> withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach(withItem -> processSelectBody(withItem, dataScope));
        }
    }

    protected void processSelectBody(SelectBody selectBody, DataScope dataScope) {
        if (selectBody == null) {
            return;
        }
        if (selectBody instanceof PlainSelect) {
//            processPlainSelect((PlainSelect) selectBody, dataScope);
            processPlainSelectForSysUser((PlainSelect) selectBody, dataScope);
        } else if (selectBody instanceof WithItem) {
            // With关键字
            WithItem withItem = (WithItem) selectBody;
            /**
             * jsqlparser 4.3版本 使用 {@code withItem.getSubSelect().getSelectBody())} 代替 {@code withItem.getSelectBody()}
             */
            processSelectBody(withItem.getSelectBody(), dataScope);
        } else {
            // 集合操作 UNION(并集) MINUS(差集)
            SetOperationList operationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = operationList.getSelects();
            if (CollectionUtils.isNotEmpty(selectBodyList)) {
                selectBodyList.forEach(item -> processSelectBody(item, dataScope));
            }
        }
    }


    /**
     * 处理 PlainSelect(数据权限与用户绑定)
     */
    protected void processPlainSelectForSysUser(PlainSelect plainSelect, DataScope dataScope) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (dataScope == null || loginUser ==null) {
            return;
        }

        SysUser sysUser = loginUser.getSysUser();



        StringBuilder sqlString = new StringBuilder();

        if(null != sysUser){
            DataScopeType type = DataScopeType.of(String.valueOf(sysUser.getDataScope()));

            switch (type){
                case DATA_SCOPE_ALL:
                    pearlDataScopeHandler.setWhereForAll(dataScope, sysUser);
                    return;
                case DATA_SCOPE_CUSTOM:
                    sqlString.append(pearlDataScopeHandler.setWhereForSysUser(dataScope, sysUser));
                    break;
                case DATA_SCOPE_DEPT:
                    sqlString.append(pearlDataScopeHandler.setWhereForDept(dataScope, sysUser));
                    break;
                case DATA_SCOPE_DEPT_AND_CHILD:
                    sqlString.append(pearlDataScopeHandler.setWhereForDeptAndChild(dataScope, sysUser));
                    break;
                case DATA_SCOPE_SELF:
                    sqlString.append(pearlDataScopeHandler.setWhereForSelf(dataScope, sysUser));
                    break;
                default:
                    break;

            }

        }

        try {
            System.out.println("sqlString===="+sqlString);
            if(StringUtils.isNotEmpty(sqlString)){
                Expression expression = CCJSqlParserUtil.parseCondExpression(" (" + sqlString.substring(4) + ")");
                pearlDataScopeHandler.setWhere(plainSelect,expression);
            }
        } catch (JSQLParserException e) {
            log.error("Failed to process, Error SQL:"+e);
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e);
        }



    }

}