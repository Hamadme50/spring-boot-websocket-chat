<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/content/lib/login/login.css?v=${cache}" >
    <link rel="icon" type="image/x-icon" href="/content/default/chaticon.png">

    <!-- Bootstrap5 CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css" integrity="sha384-r4NyP46KrjDleawBgD5tp8Y7UzmLA05oM1iAEQ17CSuDqnUK2+k9luXQOfXJCJ4I" crossorigin="anonymous">

    <title>Chat | Please Login In Chat Room</title>
    <!-- Optional JavaScript -->
    <!-- Popper.js first, then Bootstrap JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js" integrity="sha512-bLT0Qm9VnAYZDflyKcBaQ2gg0hSYNQrJ8RilYldYQ1FxQYoCLtUjuuRuZo+fjqhx/qtq/1itJ0C2ejDxltZVFg==" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    <script src="/content/lib/login/login.js?v=${cache}" ></script>
</head>
<body class="text-center chatroomcolor">
<div id="smallModal" class="smallModal">
    <div id="smallModalHtml" class="profileWidth shadow-lg">
        <div ><p style="text-align:center;padding: 0px;margin: 0px;">Your browser doesn't support cookies. Please allow cookies.</p>
            <p style="text-align:center;">Or <a href="?" class="links" target="_blank">Try to open the chat in a new window</a>.</p></div>
    </div>
</div>
<div class="vertical" style="display: none" id="chatroomlogin">
    <h1 class="fw-mediumbold chatname" id="chatname" ></h1>
    <p class="p mb-3 font-weight-normal chattopic" id="chattopic" ></p>
    <c:if test="${error != null}">
        <div class="alert alert-danger" role="alert" style="padding: 5px !important;">
                ${error}
        </div>
    </c:if>
    <form class="form-login" action="${chatroom}" method="post">
        <input type="text" autocomplete="off" onkeypress="return /^[a-zA-Z0-9_-]*$/i.test(event.key)" value="" minlength="3" name="nickname" id="nickname" class="form-control mb-2" maxlength="12" placeholder="Nickname" required autofocus>
        <input type="password" autocomplete="new-password" value="" name="password" id="password" class="form-control mb-2" maxlength="20" placeholder="Password" style="display: none" >
        <input type="email" autocomplete="off" value="" name="email" id="email" class="form-control mb-2" maxlength="80" placeholder="Email" style="display: none" >
        <div class="form-check form-switch centered" >
            <input class="form-check-input" type="checkbox" role="switch" id="ihavepassword">
            <label class="form-check-label" for="ihavepassword">I have a password</label>
        </div>

        <input type="hidden" id="chatroom" name="id" value="${chatroom}">
        <div style="margin-top: 10px;"></div>
        <button class="btn btn-lg btn-primary btn-block loginbutton" type="submit" >
            Login
        </button>
    </form>
    <form>
        <div style="margin-top: 5px;"></div>
        <span id="registerText" class="spanstyle">Don’t have an account? <input type="button" id="registerClick" class="font-weight-bold transparent" value="Register" /> </span>
        <span id="loginText" class="spanstyle" style="display: none" >have an account? <input type="button" id="loginClick" class="font-weight-bold transparent" value="Login" /> </span>
        <span class="spanstyle" > | <input type="button" id="forgotClick" class="font-weight-bold transparent" value="Forgot Password" /></span>
    </form>


</div>

<div class="vertical" id="error404" style="display: none;color: #7d8c95">
    <h1 class="fw-mediumbold chatname" > 404 Not Found</h1>
</div>

</body>
</html>