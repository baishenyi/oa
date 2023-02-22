<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset='utf-8'>
		<title>部门列表</title>
		<%--设置整个网页的基础路径--%>
		<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/ ">
	</head>
	<body>
	<script type="text/javascript">
		function del(dno){
			// 弹出确认框,用户确认返回true，取消返回false
			if(window.confirm("确认删除？")){
				document.location.href = "${pageContext.request.contextPath}/dept/delete?deptno=" + dno;
			}
		}
	</script>
		<%--显示登录名--%>
		<h3>欢迎${user.username}，在线人数${onlinecount}人</h3>
		<a href="user/exit">退出系统</a>
		<h1 align='center'>部门列表</h1>
		<hr />
		<table border='1px' align='center' width='50%'>
			<tr>
				<th>序号</th>
				<th>部门编号</th>
				<th>部门名称</th>
				<th>操作</th>
			</tr>

			<c:forEach items="${deptList}" var="dept" varStatus="deptStatus">
				<tr>
					<td>${deptStatus.count}</td>
					<td>${dept.deptno}</td>
					<td>${dept.dname}</td>
					<td>
						<a href='javascript:void(0)' onclick='del(${dept.deptno})'>删除</a>
						<a href='dept/detail?f=edit&dno=${dept.deptno}'>修改</a>
						<a href='dept/detail?f=detail&dno=${dept.deptno}'>详情</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<hr >
		<a href='add.jsp'>新增部门</a>
	</body>
</html>
