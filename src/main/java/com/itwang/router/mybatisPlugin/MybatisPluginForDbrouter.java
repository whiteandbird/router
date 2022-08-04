package com.itwang.router.mybatisPlugin;

import com.itwang.router.annotation.DbStrategy;
import com.itwang.router.controller.context.DbConfigContext;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 需要拦截mybatis
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
public class MybatisPluginForDbrouter implements Interceptor {

    private Pattern PATTERN= Pattern.compile("(from|into|update)[\\s]{1,}(\\w{1,})", Pattern.CASE_INSENSITIVE);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截方法
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        Class<?> sqlClass = Class.forName(className);

        DbStrategy dbStrategy = sqlClass.getAnnotation(DbStrategy.class);
        if(null == dbStrategy || !dbStrategy.splitTable()){
            return invocation.proceed();
        }

        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        Matcher matcher = PATTERN.matcher(sql);
        String tableName = null;
        if(matcher.find()){
            tableName = matcher.group().trim();
        }
        assert null != tableName;
        String newSql = sql.replaceAll(tableName, tableName + "_" + DbConfigContext.getTbIdx());
        Field sqlField = boundSql.getClass().getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql, newSql);
        sqlField.setAccessible(false);

        return invocation.proceed();

    }
}
