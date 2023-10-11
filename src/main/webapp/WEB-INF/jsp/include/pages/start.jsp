<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 00:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% if (request.getParameter("role").contains("admin")) { %>

<style>
  /* Custom CSS to make square boxes */
  .square-card {
    padding: 20px;
    border: 1px solid #ccc;
    height: 0;
    padding-bottom: 100%; /* This sets the aspect ratio to 1:1 */
  }

  .square-card .card-body {
    height: 100%; /* Fills the entire height of the card */
  }
</style>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Admin Dashboard</h1>
    </div>

    <h2>Start page</h2>

</main>

<% } else { %>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Dashboard</h1>
    </div>

    <h2>Start page</h2>

</main>

<% } %>