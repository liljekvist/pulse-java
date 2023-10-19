<%--
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

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Report - ${report.name}</h1>
        <h4>Date due: ${report.dueDate}</h4>
    </div>

    <div id="editor"></div>

    <button id="save-button" class="btn btn-primary">Save</button>

    <h4 class="mt-5">Comments:</h4>

    <c:forEach items="${report.comments}" var="_comment">
        <div id="comment-editor-${_comment.id}" style="height: 150px; margin-bottom: 20px"></div>
    </c:forEach>


</main>

<script>

  $(document).ready(function () {

    var quill = new Quill('#editor', {
      theme: 'snow'
    });

    quill.setContents(${report.content});

    $("#save-button").click(function (e) {

      let quillContent = JSON.stringify(quill.getContents());

      quillContent = quillContent.replace(/<(?:.|\n)*?>/gm, ' ');

      let data = {
        id: ${report.id},
        content: quillContent
      }
      e.preventDefault();

      $.ajax({
        type: "POST",
        url: "/api/public/report/",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          window.location.href = "/reports";
        },
        error: function (errMsg) {
          console.error(errMsg);
        }
      });

    })

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
