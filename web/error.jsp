<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <%--设置整个网页的基础路径--%>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/ ">
</head>
<body>
出错了,<input type='button' value='返回上一页' onclick='window.history.back()'/>
</body>
</html>