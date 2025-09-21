<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<jsp:include page="header.jsp" />
<div class="sidebar">
    <div class="scrollbar-inner sidebar-wrapper">
        <ul class="nav">
            <li class="nav-item active">
                <a href="/panel/app">
                    <i class="la la-dashboard"></i>
                    <p>Dashboard</p>
                </a>
            </li>
        <c:if test="${data.owner == 'Y'}">
            <li class="nav-item">
                <a href="/panel/network">
                    <i class="la la-globe"></i>
                    <p>Network</p>
                </a>
            </li>
        </c:if>
            <li class="nav-item">
                <a href="/panel/chatroom">
                    <i class="la la-comments"></i>
                    <p>Chatroom</p>
                </a>
            </li>
            <li class="nav-item">
                <a href="/panel/settings">
                    <i class="la la-cogs"></i>
                    <p>Settings</p>
                </a>
            </li>
            <li class="nav-item">
                <a href="/panel/ranks">
                    <i class="la la-shield"></i>
                    <p>Ranks</p>
                </a>
            </li>
            <li class="nav-item">
                <a href="/panel/support">
                    <i class="la la-skype"></i>
                    <p>Support</p>
                </a>
            </li>

        </ul>
    </div>
</div>
<div class="main-panel" style="height:auto">
    <div class="content">
        <div class="container-fluid">
            <h4 class="page-title">Dashboard</h4>
            <div class="row">
                <div class="col-md-3">
                    <div class="card card-stats card-success">
                        <div class="card-body ">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-users"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">Users</p>
                                        <h4 class="card-title">${dashboard.users}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-stats card-warning">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-user-times"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">Muted</p>
                                        <h4 class="card-title">${dashboard.mutes}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-stats card-danger">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-user-times"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">Banned</p>
                                        <h4 class="card-title">${dashboard.banned}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-stats card-primary" style="background-color: #0c5460 !important;">
                        <div class="card-body ">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-shield"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">Admins</p>
                                        <h4 class="card-title">${dashboard.admins}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-stats card-info" style="background-color: #4abdb3 !important;">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-bookmark"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">Moderators</p>
                                        <h4 class="card-title">${dashboard.mods}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${data.paid == 'Y'}">
                <div class="col-md-3">
                    <div class="card card-stats card-default">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-certificate"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">Customized Users</p>
                                        <h4 class="card-title">${dashboard.customs}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>
                <div class="col-md-3">
                    <div class="card card-stats card-primary">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-5">
                                    <div class="icon-big text-center">
                                        <i class="la la-microphone"></i>
                                    </div>
                                </div>
                                <div class="col-7 d-flex align-items-center">
                                    <div class="numbers">
                                        <p class="card-category">RJ/DJ</p>
                                        <h4 class="card-title">${dashboard.rj}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <script>
        var sucessMsg = '${msg}';
        var errorMsg = '${error}';
        var online = '';
    </script>
<jsp:include page="footer.jsp" />