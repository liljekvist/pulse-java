<%@ page import="se.bth.pulse.entity.Report" %>
<%@ page import="se.bth.pulse.entity.Report.Status" %><%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-10-12
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>
<link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
<script src="https://cdn.quilljs.com/1.3.6/quill.js"></script>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 ">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Report - ${report.name}</h1>
        <h4>Date due: ${report.dueDate}</h4>
    </div>
    <% Report report = (Report) request.getAttribute("report"); %>
    <button id="read-button"
            class="btn btn-primary <% if(report.getStatus() == Status.READ) { %> disabled <% } %>"><% if (
            report.getStatus() == Status.READ) { %> Marked as read <% } else {%> Mark as
        read <% } %></button>

    <div class="ql-container" style="height: 300px">
        <div id="editor" style="height: 300px"></div>
    </div>
    <h4 class="mt-5">Comments</h4>
    <c:forEach items="${report.comments}" var="_comment">
        <div id="comment-editor-${_comment.id}" style="height: 150px; margin-bottom: 20px"></div>
    </c:forEach>

    <div id="comment-editor-add" style="height: 150px"></div>
    <button id="submit-button" class="btn btn-primary">Submit comment</button>

</main>
<script>
  $(document).ready(function () {

    var quillComment = new Quill('#comment-editor-add', {
      readOnly: false,
      prefix: "qa",
      modules: {
        toolbar: true    // Snow includes toolbar by default
      },
      theme: 'snow'
    });

    console.log("ready2!");
    var quill = new Quill('#editor', {
      readOnly: true,
      modules: {
        toolbar: false    // Snow includes toolbar by default
      },
      theme: 'snow'
    });
    console.log("ready3!");
    quill.setContents(${report.content});

    $("#read-button").click(function (e) {
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/api/admin/report/read/${report.id}",
        data: null,
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          $("#read-button").addClass("disabled");
          $("#read-button").text("Marked as Read");
        },
        error: function (errMsg) {
          console.error(errMsg);
        }
      });
    });

    $("#submit-button").click(function (e) {

      let data = JSON.stringify(quillComment.getContents());

      data = data.replace(/<(?:.|\n)*?>/gm, ' ');
      e.preventDefault();

      $.ajax({
        type: "POST",
        url: "/api/admin/report/comment/${report.id}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: data,
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          location.reload();
        },
        error: function (errMsg) {
          console.error(errMsg);
        }
      });
    });

    <c:forEach items="${report.comments}" var="_comment">
    var quillComment${_comment.id} = new Quill('#comment-editor-${_comment.id}', {
      readOnly: true,
      prefix: "qe",
      modules: {
        toolbar: false    // Snow includes toolbar by default
      },
      theme: 'snow'
    });
    quillComment${_comment.id}.setContents(${_comment.content});
    </c:forEach>
  });
</script>

<style>
  .ql-container {
    height: calc(100% - 42px);
  }

</style>