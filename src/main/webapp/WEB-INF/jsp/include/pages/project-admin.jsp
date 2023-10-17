<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Project Management</h1>
    </div>

    <a href="${pageContext.request.contextPath}/admin/project/add" class="btn btn-primary">Add Project</a>

    <div class="table-responsive">
        <table class="table table-striped table-sm">
            <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Description</th>
                <th>Report interval</th>
                <th>Start date</th>
                <th>End date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${projects}" var="_project">
                <tr>
                    <td>${_project.id}</td>
                    <td>${_project.name}</td>
                    <td>${_project.description}</td>
                    <td>${_project.reportInterval}</td>
                    <td>${_project.startDate}</td>
                    <td>${_project.endDate}</td>
                    <td>
                        <a href="/admin/project/users/${_project.id}" class="btn btn-primary">Users</a>
                        <a href="/admin/project/edit/${_project.id}" class="btn btn-primary">Edit</a>
                        <a href="/admin/project/delete/${_project.id}" class="btn btn-danger">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<script>
  $(document).ready(function () {
    let status = document.getElementById("status");
    $("#submitButton").click(function (e) {
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/api/admin/add-project",
        data: $("#projectForm").serialize(),
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
        },
        error: function (data) {
          status.innerText = "Error creating admin account";
        }
      });
    });
  });
</script>