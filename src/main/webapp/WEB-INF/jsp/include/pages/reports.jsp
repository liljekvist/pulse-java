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

    <h2>Your reports</h2>
    <div class="table-responsive small">
        <table class="table table-striped table-sm">
            <thead>
            <tr>
                <th scope="col">Status</th>
                <th scope="col">Date due</th>
                <th scope="col">Report name</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="text-success">Read</td>
                <td>1 Week ago</td>
                <td>Weekly report</td>
            </tr>
            <tr>
                <td class="text-warning">Submitted</td>
                <td>1 Day ago</td>
                <td>Weekly report</td>
            </tr>
            <tr>
                <td class="text-danger">Not Done</td>
                <td>2 Days</td>
                <td>Weekly report</td>
            </tr>
            <tr>
                <td class="text-danger">Not Done</td>
                <td>1 Week</td>
                <td>Weekly report</td>
            </tr>
            </tbody>
        </table>
    </div>
</main>
