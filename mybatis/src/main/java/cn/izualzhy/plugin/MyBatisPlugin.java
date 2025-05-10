package cn.izualzhy.plugin;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

@Log4j2
//定义拦截签名
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = { Connection.class, Integer.class }) })
public class MyBatisPlugin implements Interceptor {
    Properties properties = null;

    // 拦截方法逻辑
    // 这里的 Intercepts 的 type 指定了 StatementHandler.class，所以可以强制转换
    // 通过拦截，可以实现记录耗时、sql 打印、sql 修改等
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();
        Object param = boundSql.getParameterObject();
        log.info("plugin SQL: {}, PARAM: {}", sql, param);
        return invocation.proceed();
    }

    // 设置插件属性
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}