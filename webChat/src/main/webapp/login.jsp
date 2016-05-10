<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link href="style.css" rel="stylesheet">
</head>
<body>
<header>
    <h1>Chat</h1>
</header>
<div class="login-form">
    <form method="post">
        <div>
            <label for="login-field">Username</label>
            <input id="login-field" name="login"></div>
        <div>
            <label for="pass-field">Password</label>
            <input id="pass-field" name="pass" type="password">
        </div>
        <div class="errorAuthentication"><c:out value="${requestScope.errorMsg}"/></div>
        <div class="buttons">
            <button id="Login" type="submit" formaction="/login">Log In</button>
            <button id="Registration" type="submit" formaction="/registration">Sign In</button>
        </div>
    </form>
</div>

<footer class="footer">
    <div class="container">
        <div class="ir-footer">
            <div>Chat</div>
            <div>&copy; 2016&thinsp;Yulia Galuzo FAMCS BSU</div>
        </div>
    </div>
</footer>

</body>
</html>
