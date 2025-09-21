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
<div class="main-panel" style="height: auto">
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <h4 class="card-title">Chat Room Link</h4>
                            <a style="font-size: 12px;" href="https://www.chatlinks.net/chat/${chatRoomId}" target="_blank">https://www.chatlinks.net/chat/${chatRoomId}</a>
                            <p class="card-category">Please copy paste below code to your website</p>
                        </div>
                        <div class="card-body">
                                        <textarea id="mychatroom" readonly style="height: 15vh;" class="form-control" aria-label="Code"><iframe src="https://www.chatlinks.net/chat/${chatRoomId}" frameBorder="0" target="iframe_name" width="100%" height="500" title="Chat Room"></iframe>
                                        </textarea>
                            <button class="btn btn-default" onclick="copyit();" style="float: right;margin-top: 1px;">Copy Code</button>

                        </div>

                    </div>
                </div>
            </div>
        </div>
        <iframe src="https://www.chatlinks.net/chat/${chatRoomId}" width="100%" height="500" title="Chat Room" frameBorder="0" target="iframe_name"></iframe>
    </div>

</div>
</div>

<script>
    function copyit() {
        /* Get the text field */
        var copyText = document.getElementById("mychatroom");

        /* Select the text field */
        copyText.select();
        copyText.setSelectionRange(0, 99999); /* For mobile devices */

        /* Copy the text inside the text field */
        navigator.clipboard.writeText(copyText.value);

        /* Alert the copied text */
        var placementFrom = "top";
        var placementAlign = "center";
        var state = "success";
        var content = {};

        content.message = "Copied Sucessfully !";
        content.title = '';
        content.icon = 'la la-bell';

        content.url = '';
        content.target = '';

        $.notify(content,{
            type: state,
            placement: {
                from: placementFrom,
                align: placementAlign
            },
            time: 3000,
        });

    }
    function copyOwnDomainit() {
        /* Get the text field */
        var copyText = document.getElementById("mychatroomdomain");

        /* Select the text field */
        copyText.select();
        copyText.setSelectionRange(0, 99999); /* For mobile devices */

        /* Copy the text inside the text field */
        navigator.clipboard.writeText(copyText.value);

        /* Alert the copied text */
        var placementFrom = "top";
        var placementAlign = "center";
        var state = "success";
        var content = {};

        content.message = "Copied Sucessfully !";
        content.title = '';
        content.icon = 'la la-bell';

        content.url = '';
        content.target = '';

        $.notify(content,{
            type: state,
            placement: {
                from: placementFrom,
                align: placementAlign
            },
            time: 3000,
        });

    }
    var sucessMsg = '${msg}';
    var errorMsg = '${error}';
</script>
<jsp:include page="footer.jsp" />