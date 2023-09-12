<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-10
  Time: 18:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
    <jsp:include page="./include/header.jsp" >
        <jsp:param name="role" value="${role}" />
    </jsp:include>
</head>
<body>
User: ${username} <br>
Auth: ${role}
</body>
</html>
