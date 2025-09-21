<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<jsp:include page="header.jsp" />
<div class="sidebar">
    <div class="scrollbar-inner sidebar-wrapper">
        <ul class="nav">
            <li class="nav-item">
                <a href="/panel/app">
                    <i class="la la-dashboard"></i>
                    <p>Dashboard</p>
                </a>
            </li>
            <c:if test="${data.owner == 'Y'}">
                <li class="nav-item active">
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
            <h4 class="page-title">Global Network</h4>
            <div class="row">

<c:forEach items="${networks}" var="network">
                <div class="col-md-4" onclick="window.open('https://chatlinks.net/chat/${network.id}', '_blank');">
                    <div class="card card-stats"  style="background-color:${network.theme};color: white !important;">
                        <div class="card-body ">
                            <div class="row">
                                <div class="col-3">
                                    <div class="icon-big text-center">
                                        <i class="la la-wechat"></i>
                                    </div>
                                </div>
                                <div class="col-9 d-flex align-items-center">
                                    <div class="numbers" >
                                        <p class="card-category" style="color: white !important;" >${network.name}</p>
                                        <p class="card-category" style="color: white !important;" >${network.domain}</p>
                                        <h4 class="card-title" style="color: white !important;" >${network.id}</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
</c:forEach>
            </div>
        </div>
    </div>
    <script>
        var sucessMsg = '${msg}';
        var errorMsg = '${error}';
        var online = '';
    </script>
<jsp:include page="footer.jsp" />