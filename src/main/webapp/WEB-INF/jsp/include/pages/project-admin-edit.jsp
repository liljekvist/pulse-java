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
<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Project Management</h1>
    </div>

    <form id="projectForm">
        <h1>Edit Project</h1>
        <label for="id">Id:</label>
        <input type="text" id="id" name="id" value="${project.id}" required readonly><br><br>
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="${project.name}" required><br><br>
        <label for="description">Description:</label>
        <input type="text" id="description" name="description" value="${project.description}" required><br><br>
        <label for="reportInterval">Report Interval:</label>
        <select name="reportInterval" id="reportInterval">
            <option <c:if test="${project.reportInterval eq 'DAILY'}">selected="selected"</c:if> value="DAILY">DAILY</option>
            <option <c:if test="${project.reportInterval eq 'WEEKLY'}">selected="selected"</c:if> value="WEEKLY">WEEKLY</option>
            <option <c:if test="${project.reportInterval eq 'BIWEEKLY'}">selected="selected"</c:if> value="BIWEEKLY">BIWEEKLY</option>
            <option <c:if test="${project.reportInterval eq 'MONTHLY'}">selected="selected"</c:if> value="MONTHLY">MONTHLY</option>
        </select><br><br>

        <label for="datefilter">Report Day:</label>
        <input type="text" name="datefilter" id="datefilter" value="" /><br><br>

        <input type="submit" id="submitButton" value="Submit">
    </form>
    <p id="status"></p>
</main>

<script>
  $(document).ready(function () {
    let startDate = Date.parse("${project.startDate}");
    let endDate = Date.parse("${project.endDate}");

    /////// FIXA DETTA DSAKJBDASKJNSADKJNADSADSJKADSJKADSHJKKHJ
    $('input[name="datefilter"]').daterangepicker({
      "startDate": startDate,
      "endDate": endDate,
      autoUpdateInput: true,
      locale: {
        cancelLabel: 'Clear'
      }
    });

    $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
      $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
      startDate = picker.startDate.format('MM/DD/YYYY');
      endDate = picker.endDate.format('MM/DD/YYYY');
    });

    $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });

    let status = document.getElementById("status");
    $("#submitButton").click(function (e) {
      let data = {
        id: $("#id").val(),
        name: $("#name").val(),
        description: $("#description").val(),
        reportInterval: $("#reportInterval").val(),
        reportDay: $("#reportDay").val(),
        startDate: startDate,
        endDate: endDate
      }
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/api/admin/project/edit",
        data: data,
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