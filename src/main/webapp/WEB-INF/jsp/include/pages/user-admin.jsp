<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">User Management</h1>
    </div>
    <a href="/admin/users/file" class="btn btn-primary">Import Users</a>
    <div class="table-responsive">
        <table class="table table-striped table-sm">
            <thead>
            <tr>
                <th>Id</th>
                <th>Firstname</th>
                <th>Lastname</th>
                <th>Email</th>
                <th>Role</th>
                <th>Disabled</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="_user">
                <tr>
                    <td>${_user.id}</td>
                    <td>${_user.firstname}</td>
                    <td>${_user.lastname}</td>
                    <td>${_user.email}</td>
                    <td>${_user.role.name}</td>
                    <td>${!_user.enabled}</td>
                    <td>
                        <a href="/admin/user/edit/${_user.id}" class="btn btn-primary">Edit</a>
                        <a href="/admin/user/disable/${_user.id}" class="btn btn-danger">Disable</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>
