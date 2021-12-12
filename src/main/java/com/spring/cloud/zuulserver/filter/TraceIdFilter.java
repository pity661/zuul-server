package com.spring.cloud.zuulserver.filter;

import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @program: zuul-server
 * @description:
 * @author: wenky
 * @email: huwenqi@panda-fintech.com
 * @create: 2021-04-22 17:44
 */
@Component
public class TraceIdFilter extends ZuulFilter {
  @Autowired private Tracer tracer;

  @Override
  public String filterType() {
    return FilterConstants.POST_TYPE;
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return Boolean.TRUE;
  }

  @Override
  public Object run() throws ZuulException {
    // 可以通过下面的代码获取tracerId，并将其设置到返回信息中
    RequestContext requestContext = RequestContext.getCurrentContext();
    requestContext
        .getResponse()
        .addHeader("scd-trace-id", this.tracer.currentSpan().context().traceIdString());
    return null;
  }
}
