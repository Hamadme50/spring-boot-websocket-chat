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
            <li class="nav-item">
                <a href="/panel/chatroom">
                    <i class="la la-comments"></i>
                    <p>Chatroom</p>
                </a>
            </li>
            <li class="nav-item active">
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

            <div class="row">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Chat Room Settings</div>
                        </div>
                        <div class="card-body">
                            <form id="newPost" action="updateSettings" method="post">
                                <div class="form-group">
                                    <label >Chat Room Name</label>
                                    <input type="text" id="name" placeholder="Name of chatroom" name="name"  value="${chatroom.name}" maxlength="50" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label >Your Website URL</label>
                                    <input type="url" id="domain" placeholder="Link of your website" name="domain"  value="${chatroom.domain}" maxlength="100" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label >Theme</label>
                                    <input type="color" id="theme"  name="theme"  value="${chatroom.theme}" class="form-control" style="width: 60px;height: 40px;" required>
                                </div>
                                <div class="form-group">
                                    <label >Design</label>
                                    <select class="form-control input-square" value="${chatroom.design}" required id="design" name="design" >
                                        <option value="M" ${chatroom.design == 'M' ? "selected" : ""} >Modren Style</option>
                                        <option value="I" ${chatroom.design == 'I' ? "selected" : ""}  >IRC Style</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label >Topic (Chat Login Page Text)</label>
                                    <textarea type="text" id="topic" placeholder="Topic of your chat room" name="topic" maxlength="200" class="form-control" >${chatroom.topic}</textarea>
                                </div>
                                <div class="form-group">
                                    <label >Radio URL</label>
                                    <input type="url" id="radio" placeholder="Link of radio" name="radio"  value="${chatroom.radio}" maxlength="100" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label >AntiSpam Engine </label>
                                    <p style="font-size: 11px;color: grey;">
                                        if you turn on AntiSpam Engine every newly created user account on this chat will be mark as spammer and if he/she try to send message his/her message will not show to all chat room users it will only show to those people whom rank opton is set to Control Spam.
                                    </p>
                                    <select class="form-control input-square" value="${chatroom.antiSpam}" required id="antiSpam" name="antiSpam" >
                                        <option value="Y" ${chatroom.antiSpam == 'Y' ? "selected" : ""} >ON (Chat room Under Spam Attack)</option>
                                        <option value="N" ${chatroom.antiSpam == 'N' ? "selected" : ""}  >OFF (Recommended)</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label >New Registration </label>
                                    <select class="form-control input-square" value="${chatroom.register}" required id="register" name="register" >
                                        <option value="Y" ${chatroom.register == 'Y' ? "selected" : ""} >ON (Recommended)</option>
                                        <option value="N" ${chatroom.register == 'N' ? "selected" : ""}  >OFF</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label >Allow Guest </label>
                                    <select class="form-control input-square" value="${chatroom.guest}" required id="guest" name="guest" >
                                        <option value="Y" ${chatroom.guest == 'Y' ? "selected" : ""} >ON (Recommended)</option>
                                        <option value="N" ${chatroom.guest == 'N' ? "selected" : ""}  >OFF</option>
                                    </select>
                                </div>

                                <div class="card-action">
                                    <button class="btn btn-success">Save</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-category">Features</div>
                        </div>
                        <div class="card-body">
                            <table class="table table-typo">
                                <tbody>
                                <tr>
                                    <td>
                                        <p>DDOS Protection</p>

                                    </td>
                                    <td><p class="text-success">Enabled</p></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Bandwidth</p>

                                    </td>
                                    <td><p class="text-success">Unlimted</p></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p>Chat Room Users</p>

                                    </td>
                                    <td><p class="text-success">Unlimted</p></td>
                                </tr>

                                </tbody>
                            </table>
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