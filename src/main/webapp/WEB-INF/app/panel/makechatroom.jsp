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
                <li class="nav-item">
                    <a href="/panel/network">
                        <i class="la la-globe"></i>
                        <p>Network</p>
                    </a>
                </li>
            </c:if>
            <li class="nav-item active">
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
<div class="main-panel" style="height: 100vh">
    <div class="content">
        <div class="container-fluid">

            <div class="row">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Create Chat Room For Your Website</div>
                        </div>
                        <div class="card-body">
                            <form id="newPost" action="newchatroom" method="post">
                                <div class="form-group">
                                    <label >Name</label>
                                    <input type="text" id="name" placeholder="Name of chatroom" name="name"  value="" maxlength="50" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label >Your Website URL</label>
                                    <input type="url" id="domain" placeholder="Link of your website" name="domain"  value="" maxlength="100" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label >Owner Nickname</label>
                                    <input type="text" id="nickname" placeholder="Nickname for your owner id" name="nickname"  value="" maxlength="15" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label >Owner Password</label>
                                    <input type="text" id="password" placeholder="Password for your owner id" name="password"  value="" maxlength="15" class="form-control" required>
                                </div>

                                <div class="card-action">
                                    <button class="btn btn-success">Create Chatroom</button>
                                </div>
                            </form>
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