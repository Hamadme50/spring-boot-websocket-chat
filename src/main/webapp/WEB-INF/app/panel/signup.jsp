<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ChatLinks | Sign Up Create Free Chat Room</title>
    <link rel="icon" type="image/x-icon" href="/content/default/chatlinks_icon.png">
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
     <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="/content/lib/panel/signup.css" >

</head>
<body >

<main class="form-signin">
    <form  action="/panel/register" method="post">
        <img class="mb-4" src="/content/default/chatlinks.png" alt="" width="300" >
        <div class="text-center">
            <h1 class="h3 mb-3 fw-normal">Please Sign Up</h1>
            <p class="p mb-3 fw-normal">Create Free Chat Room For Your Website</p>
        </div>
        <c:if test="${error != null}">
            <div class="alert alert-danger" role="alert">
            ${error}
            </div>
        </c:if>


        <div class="form-floating">
            <label for="name">Your Name</label>
            <input type="text" class="form-control" id="name" name="name" maxlength="30" placeholder="Name" required>
        </div>
        <div class="form-floating">
            <label for="email">Email address</label>
            <input type="email" class="form-control" id="email" name="email" maxlength="80" placeholder="name@gmail.com" required>
        </div>
        <div class="form-floating">
            <label for="password">Password</label>
            <input type="password" class="form-control" maxlength="20" name="password" id="password" placeholder="Password" required>
        </div>

        <button class="w-100 btn btn-lg btn-primary" type="submit">Sign Up</button>
    </form>
</main>


</body>
</html>
