<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <form name='f' id="passwordForm" action="change-password" method='POST'>
        <h1 class="h3 mb-3 fw-normal">Please change password before you continue</h1>

        <div class="form-floating">
            <input type="password" class="form-control" name='username' id="password"
                   placeholder="Password">
            <label for="floatingInput">New password</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" name='password' id="confirmPassword"
                   placeholder="Password">
            <label for="floatingPassword">Confirm new password</label>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <p id="status"></p>

        <input class="btn btn-primary w-100 py-2" type="button" id="submit">Sign in</input>
    </form>
</main>

<script>

  $(document).ready(function () {
    let status = document.getElementById("status");
    $("#submit").click(function (e) {
      // check if passwords match
        if ($("#password").val() !== $("#confirmPassword").val()) {
            status.innerText = "Passwords do not match";
            return;
        }
      e.preventDefault();
      $.ajax({
        type: "POST",
        url: "/change-password",
        data: $("#passwordForm").serialize(),
        headers: {
          '${_csrf.headerName}': '${_csrf.token}'
        },
        success: function (data) {
          window.location.href = "/";
        },
        error: function (data) {
          status.innerText = "Error changing password";
        }
      });
    });
  });

</script>
