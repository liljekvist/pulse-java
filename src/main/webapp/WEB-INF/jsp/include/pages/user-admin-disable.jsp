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
        <h1 class="h2">Disable user</h1>
    </div>

    <form id="projectForm">
        <label for="id">Id:</label>
        <input type="text" id="id" name="id" value="${editUser.id}" required readonly><br><br>
        <label for="firstname">First name:</label>
        <input type="text" id="firstname" name="firstname" value="${editUser.firstname}" required
               readonly><br><br>
        <label for="lastname">Last name:</label>
        <input type="text" id="lastname" name="lastname" value="${editUser.lastname}" required
               readonly><br><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${editUser.email}" required readonly><br><br>
        <label for="phonenr">Phone number:</label>
        <input type="text" id="phonenr" name="phonenr" value="${editUser.phonenr}" required
               readonly><br><br>
        <label for="credentialsExpired">Change password on next login:</label>
        <input type="checkbox" id="credentialsExpired" name="credentialsExpired"
        <c:if test="${editUser.credentialsExpired}"> checked </c:if>
               onclick="return false;"><br><br>


        <h3>Disabling a user is permanent. Are you sure you want to disable this user?</h3>
        <button type="submit" id="submitButton" class="btn btn-danger">Disable user</button>
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
        url: "/api/admin/user/disable/${editUser.id}",
        data: null,
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          console.log(data);
          window.location.href = "/admin/users";
        },
        error: function (data) {
          console.error(data);
          status.innerText = "Error editing user";
        }
      });
    });
  });
</script>