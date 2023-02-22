package com.javaweb.oa.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * 什么情况下不能拦截
 *      用户访问index.jsp不能拦截
 *      用户已经登录不能拦截
 *      用户要去登录不能拦截
 *      WelcomeServlet不能拦截
 */
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 获取请求路径
        String servletPath = request.getServletPath();

        // 获取当前session
        HttpSession session = request.getSession(false);
        // 入口验证
        if ("/index.jsp".equals(servletPath) ||
                "/welcome".equals(servletPath) ||
                "/user/login".equals(servletPath) ||
                "/user/exit".equals(servletPath) ||
                (session != null && session.getAttribute("user") !=null)){
            // 继续往下走
            chain.doFilter(request, response);
        }else{
            // 跳转到登录界面
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
