<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>ChatLinks Panel</title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <link rel="stylesheet" href="/content/lib/panel/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i">
    <link rel="stylesheet" href="/content/lib/panel/assets/css/ready.css">
    <link rel="stylesheet" href="/content/lib/panel/assets/css/demo.css">
    <link rel="shortcut icon" type="image/png" href="/content/default/chatlinks_icon.png" />
    <link rel="icon" type="image/x-icon" href="/content/default/chatlinks_icon.png">
    <script src="/content/lib/panel/assets/js/core/jquery.3.2.1.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>
    <script src="/content/lib/panel/assets/js/core/popper.min.js"></script>
    <script src="/content/lib/panel/assets/js/core/bootstrap.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/chartist/chartist.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/chartist/plugin/chartist-plugin-tooltip.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/bootstrap-notify/bootstrap-notify.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/bootstrap-toggle/bootstrap-toggle.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/jquery-mapael/jquery.mapael.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/jquery-mapael/maps/world_countries.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/chart-circle/circles.min.js"></script>
    <script src="/content/lib/panel/assets/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>
    <script src="/content/lib/panel/assets/js/ready.min.js"></script>
    <script src="https://kit.fontawesome.com/65fc6f176c.js" crossorigin="anonymous"></script>
    <link href="/content/lib/other/Semantic/semantic.min.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div class="wrapper">
    <div class="main-header">
        <div class="logo-header">
            <a href="/panel/app" class="logo">
                <img src="/content/default/chatlinks.png" alt="" width="150" >
            </a>
            <button class="navbar-toggler sidenav-toggler ml-auto" type="button" data-toggle="collapse" data-target="collapse" aria-controls="sidebar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <button class="topbar-toggler more" style="padding-top: 14px;"><i class="la la-ellipsis-v"></i></button>
        </div>

            <nav class="navbar navbar-header navbar-expand-lg">
                <div class="container-fluid">

                    <ul class="navbar-nav topbar-nav ml-md-auto align-items-center">
                        <li class="nav-item dropdown">
                            <a class="dropdown-toggle profile-pic" data-toggle="dropdown" href="#" aria-expanded="false"> <img src="/content/default/user.png" alt="user-img" width="36" class="img-circle"> <span>${data.name}</span> </a>
                            <ul class="dropdown-menu dropdown-user">
                                <li>
                                    <div class="user-box">
                                        <div class="u-img"><img src="/content/default/user.png" alt="user"></div>
                                        <div class="u-text">
                                            <h4>${data.name}</h4>
                                            <p class="text-muted">${data.email}</p></div>
                                    </div>
                                </li>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="/panel/changepassword"><i class="ti-settings"></i> Change Password</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="/panel/logout"><i class="fa fa-power-off"></i> Logout</a>
                            </ul>
                            <!-- /.dropdown-user -->
                        </li>
                    </ul>
                </div>
            </nav>

    </div>