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

    <form id="projectForm">
        <h1>Add Project</h1>
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required><br><br>
        <label for="description">Description:</label>
        <input type="text" id="description" name="description" required><br><br>
        <label for="reportInterval">Report Interval:</label>
        <select name="reportInterval" id="reportInterval">
            <option value="DAILY">DAILY</option>
            <option value="WEEKLY">WEEKLY</option>
            <option value="BIWEEKLY">BIWEEKLY</option>
            <option value="MONTHLY">MONTHLY</option>
        </select><br><br>
        <label for="reportDay">Report Day:</label>
        <select name="reportDay" id="reportDay">
            <option value="MONDAY">MONDAY</option>
            <option value="TUESDAY">TUESDAY</option>
            <option value="WEDNESDAY">WEDNESDAY</option>
            <option value="THURSDAY">THURSDAY</option>
            <option value="FRIDAY">FRIDAY</option>
            <option value="SATURDAY">SATURDAY</option>
            <option value="SUNDAY">SUNDAY</option>
        </select><br><br>
        <input type="submit" id="submitButton" value="Submit">
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
        url: "/api/admin/project/add",
        data: $("#projectForm").serialize(),
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          window.location.href = "/admin/projects";
        },
        error: function (data) {
          status.innerText = "Error creating admin account";
        }
      });
    });
  });
</script>