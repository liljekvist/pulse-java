<%@ page import="java.util.Date" %>
<%@ page import="se.bth.pulse.entity.Report" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="se.bth.pulse.entity.Report.Status" %><%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 00:44
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Dashboard</h1>
    </div>

    <h2>Your reports</h2>
    <div class="table-responsive small">
        <table class="table table-striped table-hover table-sm">
            <thead>
            <tr>
                <th scope="col">Status</th>
                <th scope="col">Date due</th>
                <th scope="col">Report name</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Report> reports = (List<Report>) request.getAttribute("reports");
                for (Report report : reports) {
                    java.time.LocalDateTime currentTime = java.time.LocalDateTime.now();
                    Date dueDate = report.getDueDate();
                    LocalDateTime ldt = LocalDateTime.ofInstant(dueDate.toInstant(), ZoneId.systemDefault());
                    java.time.Duration timeDifference = java.time.Duration.between(currentTime, ldt);
                    String statusColorClass;

                    if( report.getStatus() == Status.SUBMITTED ){
                        statusColorClass = "text-info";
                    } else if(report.getStatus() == Status.READ){
                        statusColorClass = "text-primary";
                    }
                    else {
                        if (timeDifference.toDays() < 0) {
                            statusColorClass = "text-danger";
                        } else if (timeDifference.toDays() == 0) {
                            statusColorClass = "text-warning";
                        } else {
                            statusColorClass = "text-success";
                        }
                    }
            %>

            <tr class="clickable-row" data-href="${pageContext.request.contextPath}/report/<%= report.getId() %>">
                <td class="<%= statusColorClass %>">
                    <%= report.getStatus() %>
                </td>
                <td>
                    <%= report.getDueDate() %>
                </td>
                <td>
                    <%= report.getName() %>
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</main>

<script>

  jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
      window.location = $(this).data("href");
    });
  });

</script>

<style>

  [data-href] { cursor: pointer; }

</style>
