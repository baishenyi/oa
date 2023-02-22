<%@ page contentType="text/html;charset=UTF-8" %>
<%--访问jsp的时候不生成session对象--%>
<%@page session="false" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>欢迎使用OA系统</title>
		<%--设置整个网页的基础路径--%>
		<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/ ">
	</head>
	<body>
		<h1>用户登录</h1>
		<hr>
		<%--前端页面发送请求--%>
		<form action="user/login" method="post">
			用户名:<input type="text" name="username"><br>
			密 码:<input type="password" name="password"><br>
			<input type="checkbox" name="f" value="1">十天内免登录<br>
			<input type="submit" value="login">

		</form>
	</body>
</html>