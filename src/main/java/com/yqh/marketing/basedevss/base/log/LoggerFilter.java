package com.yqh.marketing.basedevss.base.log;

import org.apache.log4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggerFilter implements Filter{
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
        String ip = req.getRemoteAddr();
        String userName = (String)((HttpServletRequest)req).getSession().getAttribute("userName");//获取用户名
        MDC.put("ip", ip);
        MDC.put("userName", userName);
        chain.doFilter(req, res);
        MDC.remove("ip");
        MDC.remove("userName");

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
    @Override
    public void destroy() {
    }
}