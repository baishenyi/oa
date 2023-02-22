package com.javaweb.oa.web.action;

import com.javaweb.oa.bean.Dept;
import com.javaweb.oa.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 模板类
@WebServlet({"/dept/list", "/dept/sava", "/dept/edit", "/dept/detail", "/dept/delete", "/dept/modify"})
// 模糊匹配
// 只要请求路径是"/dept"开始的，都走这个Servlet
//@WebServlet("/dept/*")
public class DeptServlet extends HttpServlet {

    // 模板方法
    // 重写service方法

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取Servlet path
        String servletPath = request.getServletPath();
        if ("/dept/list".equals(servletPath)) {
            doList(request,response);
        }else if ("/dept/sava".equals(servletPath)) {
            doSava(request,response);
        }else if ("/dept/detail".equals(servletPath)) {
            doDetail(request,response);
        }else if ("/dept/delete".equals(servletPath)) {
            doDel(request,response);
        }else if ("/dept/modify".equals(servletPath)) {
            doModify(request,response);
        }
    }

    /**
     * 连接数据库，查询所有的部门信息，将部门信息收集，跳转到jsp做页面展示
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 准备一个容器，用来存储部门
        List<Dept> depts = new ArrayList();

        // 连接数据库，查询所有部门
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // 获取连接
            conn = DBUtil.getConnection();
            // 获取预编译的数据库操作对象
            String sql = "select deptno,dname,loc from dept";
            ps = conn.prepareStatement(sql);
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理结果集
            while (rs.next()){
                // 从rs中取出数据
                String deptno = rs.getString("deptno");
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");

                // 将以上数据封装成对象
                Dept dept = new Dept();
                dept.setDeptno(deptno);
                dept.setDname(dname);
                dept.setLoc(loc);

                // 将部门对象放到list集合当中
                depts.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, rs);
        }

        // 将集合放到请求域当中
        request.setAttribute("deptList", depts);

        // 转发
        request.getRequestDispatcher("/list.jsp").forward(request, response);

    }
    private void doSava(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取部门的信息
        request.setCharacterEncoding("UTF-8");
        String deptno = request.getParameter("deptno");
        String dname = request.getParameter("dname");
        String loc = request.getParameter("loc");

        // 连接数据库执行insert语句
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "insert into dept(deptno, dname, loc) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, deptno);
            ps.setString(2, dname);
            ps.setString(3, loc);
            count = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
        if (count == 1) {
            // 保存成功跳转到列表页面
            response.sendRedirect(request.getContextPath() + "/dept/list");
        }else {
            // 保存失败跳转到error页面
            // 重定向
            response.sendRedirect(request.getContextPath() + "/error.jsp");

        }
    }

    private void doDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 创建部门对象
        Dept dept = new Dept();
        // 获取部门编号
        String dno = request.getParameter("dno");

        // 连接数据库，根据部门编号查询部门信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // 获取连接
        try {
            conn = DBUtil.getConnection();
            String sql = "select dname,loc from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,dno);
            rs = ps.executeQuery();
            // 这个结果集一定只有一条记录

            if (rs.next()){
                String dname = rs.getString("dname");
                String loc = rs.getString("loc");

                // 封装对象
                dept.setDeptno(dno);
                dept.setDname(dname);
                dept.setLoc(loc);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, rs);
        }

        // 放到request域中
        request.setAttribute("dept", dept);
        // 转发
        //request.getRequestDispatcher("/detail.jsp").forward(request,response);
        /*String f = request.getParameter("f");
        if ("m".equals(f)) {
            // 转发到修改页面
            request.getRequestDispatcher("/edit.jsp").forward(request,response);
        } else if ("d".equals(f)){
            // 转发到详情页面
            request.getRequestDispatcher("/detail.jsp").forward(request,response);
        }*/
        String forward = "/" + request.getParameter("f") + ".jsp";
        request.getRequestDispatcher(forward).forward(request,response);
    }
    private void doDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据部门编号，删除部门
        // 获取部门编号
        String deptno = request.getParameter("deptno");

        // 连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();

            String sql = "delete from dept where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,deptno);
            count = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }

        // 判断删除成功与否
        if (count == 1) {
            // 删除成功
            // 跳转到部门列表页面
            // 重定向
            response.sendRedirect(request.getContextPath() + "/dept/list");
        }else {
            // 删除失败
            // 重定向
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
    private void doModify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 解决请求体的中文乱码问题
        request.setCharacterEncoding("UTF-8");

        // 获取表单上的数据
        String deptno = request.getParameter("deptno");
        String dname = request.getParameter("dname");
        String loc = request.getParameter("loc");

        // 连接数据库执行更新语句
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "update dept set dname = ?,loc = ? where deptno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,dname);
            ps.setString(2,loc);
            ps.setString(3,deptno);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }

        if (count == 1) {
            // 更新成功
            // request.getRequestDispatcher("/dept/list").forward(request, response);
            // 重定向
            response.sendRedirect(request.getContextPath() + "/dept/list");
        }else {
            // 更新失败
            //request.getRequestDispatcher("/error.jsp").forward(request, response);
            // 重定向
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
