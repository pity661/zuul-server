package com.spring.cloud.zuulserver.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @program: zuul-server
 * @description:
 * @author: wenky
 * @email: huwenqi@panda-fintech.com
 * @create: 2020-09-22 09:52
 */
@Component
public class MyFilter extends ZuulFilter {
  private static Logger log = LoggerFactory.getLogger(MyFilter.class);
  /**
   * 返回一个字符串代表过滤器类型 pre：路由前 routing：路由时 post：路由后 error：发送错误调用
   *
   * @return
   */
  @Override
  public String filterType() {
    return FilterConstants.PRE_TYPE;
  }

  /**
   * 过滤顺序，值越小越早执行
   *
   * @return
   */
  @Override
  public int filterOrder() {
    return 0;
  }

  /**
   * 逻辑判断是否过滤 执行条件，判断过滤器是否被触发
   *
   * @return
   */
  @Override
  public boolean shouldFilter() {
    return true;
  }

  /**
   * 执行具体的过滤动作
   *
   * @return
   * @throws ZuulException
   */
  @Override
  public Object run() throws ZuulException {
    // 自定义实现访问权限功能
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
    Object accessToken = request.getParameter("token");
    if (accessToken == null) {
      log.warn("token is empty");
      ctx.setSendZuulResponse(false);
      ctx.setResponseStatusCode(401);
      try {
        // SendResponseFilter::shouldFilter
        ctx.getResponse().getWriter().write("token is empty");
      } catch (Exception e) {
      }
      return null;
    }
    log.info("ok");
    return null;
  }
}
