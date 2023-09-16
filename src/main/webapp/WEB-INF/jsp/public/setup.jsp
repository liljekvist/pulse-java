<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Setup</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

</head>
<body>
<form id="accountForm" action="/api/setup/configure_admin_account" method="post">
    <h1>Add admin account</h1>
    <!-- Username, password and confirm password with text to tell the user if the passwords dont match-->
    <label for="email">Email:</label>
    <input type="text" id="email" name="email" required><br><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>
    <input type="submit" id="submitButton" value="Submit">

</form>

<form id="finishForm" action="/api/setup/configure_settings"  method="get">
    <h1>Finish setup</h1>
    <!-- Username, password and confirm password with text to tell the user if the passwords dont match-->
    <input type="submit" id="submitButtonFinish" value="Submit">

</form>
</body>
</html>
<script>
    //submit form without redirect
    $(document).ready(function() {
        const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
        $("#submitButton").click(function(e) {
            e.preventDefault();
            $.ajax({
                type: "POST",
                url: "/api/setup/configure_admin_account",
                data: $("#accountForm").serialize(),
                headers: {
                    'X-XSRF-TOKEN': csrfToken
                },
                success: function(data)
                {
                    alert(data);
                }
            });
        });
        $("#submitButtonFinish").click(function(e) {
            e.preventDefault();
            $.ajax({
                type: "GET",
                url: "/api/setup/configure_settings",
                data: $("#finishForm").serialize(),
                headers: {
                    'X-XSRF-TOKEN': csrfToken
                },
                success: function(data)
                {
                    //redirect to login page
                    window.location.href = "/login";
                }
            });
        });
    });
</script>