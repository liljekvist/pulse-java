<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 00:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Dashboard</h1>
    </div>

    <h2>Start page</h2>
    Path: <%= request.getRequestURL().toString() %> <br>
    User: ${username} <br>
    Auth: ${role} <br>
</main>

