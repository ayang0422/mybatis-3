package com.zheteng.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author yangpeng
 * @version 1.0.0
 * @date 2022年03月25日
 */
@Slf4j(topic = "123")
@Intercepts({
  @Signature(type = Executor.class, method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MyInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    log.info("-----intercept--before-----");
    Object proceed = invocation.proceed();
    log.info("-----intercept--after-----");
    return proceed;
  }

  @Override
  public Object plugin(Object target) {
    return Interceptor.super.plugin(target);
  }

  @Override
  public void setProperties(Properties properties) {
    Interceptor.super.setProperties(properties);
  }
}
