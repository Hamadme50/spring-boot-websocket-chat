<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<jsp:include page="header.jsp" />
<a data-config="commands=chat,call,add,userinfo,webchat;size=14;status=off;theme=logo;language=en;bgcolor=#2a92f3;hostname=www.skaip.org" id="skaip-buttons" href="http://www.skaip.org/">Skype</a><script src="//apps.skaip.org/buttons/widget/core.min.js" defer="defer"></script>
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
            <li class="nav-item active">
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

            <div class="row">
                <div class="col-md-6">
                    <div class="card" style="height: 300px;width: 100%;text-align: center">
                        <p style="font-size: 25px;margin-top: 100px;">Skype <a href="skype:live:.cid.687ef3466f6dfeda">ChatLinks.Net</a></p>
                        <p style="font-size: 14px;">ID :  live:.cid.687ef3466f6dfeda</p>
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
</script>
<jsp:include page="footer.jsp" />