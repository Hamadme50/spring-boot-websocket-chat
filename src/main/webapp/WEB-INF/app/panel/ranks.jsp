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
            <li class="nav-item">
                <a href="/panel/settings">
                    <i class="la la-cogs"></i>
                    <p>Settings</p>
                </a>
            </li>
            <li class="nav-item active">
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
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">
                            <div class="form-group">
                                <form id="selectPRank" action="selectPRank" method="post">
                                    <label for="rank">Select Rank</label>
                                    <select class="form-control form-control" name="rank" id="rank" >
                                        <option value="">Select Rank</option>
                                        <c:forEach items="${ranks}" var="rank">
                                            <option value="${rank.id}" ${rankId == rank.id  ? "selected" : ""}>${rank.name}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="card-action">
                                        <input  class="btn btn-primary" type="Submit" value="Edit">
                                    </div>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>

                <c:if test="${rankData != null}">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header">
                                <img src="${rankData.icon}" style="height: 40px;float: left" /> <div class="card-title" style="float: left; padding-left: 15px;margin-top: 7px;">${rankData.name}</div>
                            </div>
                            <div class="card-body">
                                <form id="updateRank" action="updateRank" method="post">
                                    <div class="form-group">
                                        <label >Rank Name</label>
                                        <input type="text" id="name" placeholder="Name of rank" name="name"  value="${rankData.name}" maxlength="20" class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label >Rank Icon</label>
                                        <input type="text" id="icon" placeholder="Icon Url" name="icon"  value="${rankData.icon}" maxlength="200" class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label >Can Change Username?</label>
                                        <select class="form-control input-square" value="${rankData.changeNick}" required id="changeNick" name="changeNick" >
                                            <option value="Y" ${rankData.changeNick == 'Y' ? "selected" : ""} >Yes</option>
                                            <option value="N" ${rankData.changeNick == 'N' ? "selected" : ""} >No</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label >Can Delete/Clear Main Chat Messages?</label>
                                        <select class="form-control input-square" value="${rankData.deleteMsg}" required id="deleteMsg" name="deleteMsg" >
                                            <option value="Y" ${rankData.deleteMsg == 'Y' ? "selected" : ""} >Yes</option>
                                            <option value="N" ${rankData.deleteMsg == 'N' ? "selected" : ""} >No</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label >Can Kick Users?</label>
                                        <select class="form-control input-square" value="${rankData.kick}" required id="kick" name="kick" >
                                            <option value="Y" ${rankData.kick == 'Y' ? "selected" : ""} >Yes</option>
                                            <option value="N" ${rankData.kick == 'N' ? "selected" : ""} >No</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label >Can Mute Users?</label>
                                        <select class="form-control input-square" value="${rankData.mute}" required id="mute" name="mute" >
                                            <option value="Y" ${rankData.mute == 'Y' ? "selected" : ""} >Yes</option>
                                            <option value="N" ${rankData.mute == 'N' ? "selected" : ""} >No</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label >Can See/Control Spammer Users?</label>
                                        <select class="form-control input-square" value="${rankData.spam}" required id="spam" name="spam" >
                                            <option value="Y" ${rankData.spam == 'Y' ? "selected" : ""} >Yes</option>
                                            <option value="N" ${rankData.spam == 'N' ? "selected" : ""} >No</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label >Can Ban Users?</label>
                                        <select class="form-control input-square" value="${rankData.ban}" required id="ban" name="ban" >
                                            <option value="Y" ${rankData.ban == 'Y' ? "selected" : ""} >Yes</option>
                                            <option value="N" ${rankData.ban == 'N' ? "selected" : ""} >No</option>
                                        </select>
                                    </div>
                                    <input type="hidden" id="id"  name="id"  value="${rankData.id}" class="form-control" required>
                                    <div class="card-action">
                                        <button class="btn btn-success">Save</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>

        </div>
    </div>
    <script>
        var sucessMsg = '${msg}';
        var errorMsg = '${error}';
    </script>
<jsp:include page="footer.jsp" />