<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-15
  Time: 21:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<main class="form-signin w-40 m-auto mt-lg-5">
    <form name='f' action="login" method='POST'>
<%--        <img class="mb-4" src="../assets/brand/bootstrap-logo.svg" alt="" width="72" height="57">--%>
        <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

        <div class="form-floating">
            <input type="email" class="form-control" name='username' id="floatingInput" placeholder="name@example.com">
            <label for="floatingInput">Email address</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" name='password' id="floatingPassword" placeholder="Password">
            <label for="floatingPassword">Password</label>
        </div>

        <div class="form-check text-start my-3">
            <input class="form-check-input" type="checkbox" value="remember-me" id="flexCheckDefault">
            <label class="form-check-label" for="flexCheckDefault">
                Remember me
            </label>
        </div>
        <button class="btn btn-primary w-100 py-2" type="submit">Sign in</button>
<%--        <p class="mt-5 mb-3 text-body-secondary">&copy; 2017–2023</p>--%>
    </form>
</main>
