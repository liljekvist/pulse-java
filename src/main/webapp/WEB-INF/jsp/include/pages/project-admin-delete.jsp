<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Delete project</h1>
    </div>

    <form id="projectForm">
        <label for="id">Id:</label>
        <input type="text" id="id" name="id" value="${project.id}" required readonly><br><br>
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="${project.name}" required><br><br>
        <label for="description">Description:</label>
        <input type="text" id="description" name="description" value="${project.description}" required><br><br>


        <h3>Deleting a project is a destructive and permanent action. Are you sure you want to disable this Project?</h3>
        <button type="submit" id="submitButton" class="btn btn-danger">Disable project</button>
    </form>
    <p id="status"></p>
</main>

<script>

  $(document).ready(function () {

    let status = document.getElementById("status");
    $("#submitButton").click(function (e) {
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/api/admin/project/delete/${project.id}",
        data: null,
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          window.location.href = "/admin/projects";
        },
        error: function (data) {
          console.error(data);
          status.innerText = "Error editing project";
        }
      });
    });
  });
</script>