<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-10
  Time: 18:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en" data-bs-theme="auto">
<head>
    <script src="../../public/js/color-modes.js"></script>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Pulse Dashboard</title>

<%--    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@docsearch/css@3">  --%>
    <link href="../../public/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../public/css/style.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="../../public/css/dashboard.css" rel="stylesheet">
</head>
<body>

<jsp:include page="./include/color-mode.jsp" />


<jsp:include page="./include/icons.jsp" />


<jsp:include page="./include/header.jsp" >
    <jsp:param name="role" value="${role}" />
    <jsp:param name="username" value="${username}" />
</jsp:include>


<div class="container-fluid">
    <div class="row">

        <jsp:include page="./include/sidebar.jsp">
            <jsp:param name="role" value="${role}" />
            <jsp:param name="content" value="${content}" />
        </jsp:include>


        <jsp:include page="include/pages/${content}" />

    </div>
</div>
<script src="../../public/js/bootstrap.bundle.min.js"></script>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.2.1/dist/chart.umd.min.js" integrity="sha384-gdQErvCNWvHQZj6XZM0dNsAoY4v+j5P1XDpNkcM3HJG1Yx04ecqIHk7+4VBOCHOG" crossorigin="anonymous"></script><script src="../../public/js/dashboard.js"></script></body>
</html>