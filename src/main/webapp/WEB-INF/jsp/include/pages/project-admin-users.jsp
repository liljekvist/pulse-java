<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/jsp/custom-function.tld" prefix="html" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>

<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
      rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<link rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css"/>


<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Project Management</h1>
    </div>

    <form id="projectForm">
        <h1>Edit and add users to project</h1>
        <input type="text" id="id" name="id" value="${project.id}" hidden readonly><br><br>

        <select class="js-example-basic-multiple w-auto" name="user_ids" id="user_ids"
                multiple="multiple">
            <c:forEach items="${users}" var="user">
                <option value="${user.id}"
                        <c:if test="${html:containsUser(project.users, user.id)}">selected="selected"</c:if> >${user.email}</option>
            </c:forEach>
        </select>

        <input type="submit" id="submitButton" value="Submit">
    </form>
    <p id="status"></p>
</main>

<script>
  $(document).ready(function () {
    $('.js-example-basic-multiple').select2({
      theme: 'bootstrap-5',
      width: '50%'
    });
    let status = document.getElementById("status");
    $("#submitButton").click(function (e) {
      let data = {
        id: parseInt($("#id").val(), 10),
        userIds: $('#user_ids').val().map(function (x) {
          return parseInt(x, 10);
        })
      }
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/api/admin/project/users",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          window.location.href = "/admin/projects";
        },
        error: function (data) {
          console.log(data);
          status.innerText = "Error creating admin account";
        }
      });
    });
  });
</script>