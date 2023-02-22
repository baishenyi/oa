package com.javaweb.oa.web.action;

import com.javaweb.oa.bean.User;
import com.javaweb.oa.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet({"/user/login","/user/exit"})
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/user/login".equals(servletPath)) {
            doLogin(request,response);
        }else if ("/user/exit".equals(servletPath)){
            doExit(request,response);
        }
    }

    protected void doExit (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取session对象，销毁session
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 从session域中删除user对象
            session.removeAttribute("user");
            // 手动销毁session对象
            session.invalidate();
            // 手动销毁cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null){
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    if ("username".equals(name) || "password".equals(name)) {
                        // 这个cookie要销毁
                        // 设置cookie的有效期为0，表示删除该cookie
                        cookie.setMaxAge(0);
                        // 设置一下cookie的路径
                        cookie.setPath(request.getContextPath());
                        // 响应cookie给浏览器,浏览器会将之前的cookie覆盖
                        response.addCookie(cookie);
                    }
                }
            }
            // 跳转到登录页面
            response.sendRedirect(request.getContextPath());
        }
    }
    protected void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = false;
        // 验证用户名和密码
        // 获取用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 连接数据库验证用户名和密码
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "select * from t_user where username = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                // 登录成功
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        // 登录成功/失败
        if (success) {
            // 获取session对象（必须获取到session）
            // session对象一定不为null
            //request.getSession().setAttribute("username",username);
            User user = new User(username, password);
            request.getSession().setAttribute("user",user);

            // 登录成功，且用户选择了十天免登录功能
            String f = request.getParameter("f");
            if("1".equals(f)){
                // 创建cookie对象存储登录名和密码
                Cookie cookie1 = new Cookie("username",username);
                Cookie cookie2 = new Cookie("password",password);
                // 设置Cookie的有效期十天
                cookie1.setMaxAge(60 * 60 * 24 * 10);
                cookie2.setMaxAge(60 * 60 * 24 * 10);
                // 设置Cookie的path（只要访问这个应用，浏览器就携带这两个cookie）
                cookie1.setPath(request.getContextPath());
                cookie2.setPath(request.getContextPath());
                // 响应cookie给浏览器
                response.addCookie(cookie1);
                response.addCookie(cookie2);

            }

            // 成功,跳转到用户列表页面
            response.sendRedirect(request.getContextPath() + "/dept/list");
        } else{
            // 失败，跳转到登录页面

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
