<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>

<main class="form-signin w-40 m-auto mt-lg-5">
    <form id="accountForm">
        <h1>Add admin account</h1>
        <!-- Username, password and confirm password with text to tell the user if the passwords dont match-->
        <label for="email">Email:</label>
        <input type="text" id="email" name="email" required><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>
        <input type="submit" id="submitButton" value="Submit">
    </form>

    <p id="status"></p>

    <form id="finishForm">
        <h1>Finish setup</h1>
        <!-- Username, password and confirm password with text to tell the user if the passwords dont match-->
        <input type="submit" id="submitButtonFinish" value="Submit">

    </form>
</main>

<script>
  //submit form without redirect
  $(document).ready(function () {
    const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/,
        '$1');
    let status = document.getElementById("status");
    $("#submitButton").click(function (e) {
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/api/setup/configure_admin_account",
        data: $("#accountForm").serialize(),
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          status.innerText = "Admin account created successfully";
        },
        error: function (data) {
          status.innerText = "Error creating admin account";
        }
      });
    });
    $("#submitButtonFinish").click(function (e) {
      e.preventDefault();
      $.ajax({
        type: "GET",
        url: "/api/setup/configure_settings",
        data: $("#finishForm").serialize(),
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          //redirect to login page
          window.location.href = "/login";
        },
        error: function (data) {
          status.innerText = "Error finishing setup";
        }
      });
    });
  });
</script>