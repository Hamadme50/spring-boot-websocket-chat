'use strict';

var stompClient = null;
var retry = 0;
var reported = 0;
let isConnected = false;

var spamCounter = [];
function connect() {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.debug = false;
        stompClient.connect({'X-Authorization': token}, onConnected, onError);
}

var openPage = 0;
function onConnected() {
    if (!isConnected) {
        stompClient.subscribe('/topic/' + chatRoomId, onMessageReceived);
        stompClient.subscribe('/queue/' + currentUserId, onPmReceived);
        stompClient.send("/app/chat.addUser",
            {'X-Authorization': token},
            JSON.stringify({ sender: currentUserName, type: 'JOIN' })
        );
        isConnected = true; // Mark as connected
    }
    reCheckUnreadMsgs();
}

function reconnectChat() {
    isConnected = false; // Reset the flag
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
            connect();
        });
    } else {
        connect();
    }
}


function onError(error) {
    setTimeout(() => {
        reconnectChat();
    }, 3000); // Wait for 3 seconds before reconnecting
}
function discountChat() {
    stompClient.disconnect();
}
$( document ).ready(function() {

    $('#main_chat_input').keyup(function (e) {
        if ($(".main_chat_input:focus") && (e.keyCode === 13)) {
            sendMessage(e);
        }
    });

    $('#pm_chat_input').keyup(function (e) {
        if ($(".pm_chat_input:focus") && (e.keyCode === 13)) {
            sendPrivateMessage(e);
        }
    });

    $(window).on("beforeunload", function() {
        discountChat();
    })
    gnupolicy();
    chatHistory();

});
Array.prototype.allValuesSame = function() {

    for(var i = 1; i < this.length; i++)
    {
        if(this[i] !== this[0])
            return false;
    }

    return true;
}
function sendMessage(event) {
    var messageContent = $("#main_chat_input").val();
    if($("#user_"+currentUserId).html() == undefined ||  $("#user_"+currentUserId).html() == '' ||  $("#user_"+currentUserId).html() == null ){
        getUserList();
    }
    if(messageContent != null && messageContent != ""){
        messageContent = escapeHTML(messageContent);
    }

    var count = (messageContent.match(/:/g) || []).length;
    if (count > 6) {
        messageContent = '';
        showMsg("Only 3 Emojis Per Message Allowed", "error");
        event.preventDefault()
    }

    if (messageContent != '' && stompClient) {
        if(currentUserRank == "GUEST" || currentUserRank == "MEMBER") {
            spamCounter.push(messageContent);
        }
        if(spamCounter.length > 5){
            spamCounter.shift();
        }
        if(spamCounter.allValuesSame() && spamCounter.length >= 4){
            showMsg("Please do not Spam", "error");
            $("#main_chat_input").val("");
        }
        if(spamCounter.allValuesSame() && spamCounter.length >= 5){
            $.post('/user/spamProtection', {
                token: token
            }, function (data) {
                if(data != null){
                    token = data;
                    $("#main_chat_input").val("");
                    $("#main_chat_input").prop("disabled",true);
                    $("#pm_chat_input").prop("disabled",true);
                    reconnectChat();
                }
            });
        }
        else{
            if(messageContent === "/clear"){
                clearAllChat();
                $("#main_chat_input").val("");
            }
            else if(messageContent.substring(0,5) === "/kick" && canKick == "Y"){
                var targetUser = messageContent.substring(6,80);
                var user = userRepo[targetUser];
                if(user != null){
                    kickUser(user.userId,user.userName);
                    $("#main_chat_input").val("");
                }
                else{
                    showMsg("User not found / online" ,"error");
                }

            }
            else if(messageContent.substring(0,5) === "/mute" && canMute == "Y"){
                var targetUser = messageContent.substring(6,80);
                var user = userRepo[targetUser];
                if(user != null){
                    muteUser(user.userId,user.userName);
                    $("#main_chat_input").val("");
                }
                else{
                    showMsg("User not found / online" ,"error");
                }

            }
            else if(messageContent.substring(0,4) === "/ban" && canBan == "Y"){
                var targetUser = messageContent.substring(5,80);
                var user = userRepo[targetUser];
                if(user != null){
                    banUser(user.userId,user.userName);
                    $("#main_chat_input").val("");
                }
                else{
                    showMsg("User not found / online" ,"error");
                }

            }
            else if(messageContent.substring(0,5) === "/spam" && canSpam == "Y"){
                var targetUser = messageContent.substring(6,80);
                var user = userRepo[targetUser];
                if(user != null){
                    spamUser(user.userId,user.userName);
                    $("#main_chat_input").val("");
                }
                else{
                    showMsg("User not found / online" ,"error");
                }

            }
            else{
                var chatMessage = {
                    type: "CHAT",
                    content: messageContent
                };
                stompClient.send("/app/chat.sendMessage", {'X-Authorization': token}, JSON.stringify(chatMessage));
                $("#main_chat_input").val("");
                scrollToBottom();
                $("#smily_box").hide();
            }

        }

    }
}

function sendPrivateMessage(event) {
    var messageContent = $("#pm_chat_input").val();
    if(messageContent != null && messageContent != ""){
        messageContent = escapeHTML(messageContent);
    }

    var toUserId = $("#pmUserId").val();

    var count = (messageContent.match(/:/g) || []).length;
    if (count > 6) {
        messageContent = '';
        console.log("Only 3 Emojis Per Message Allowed", "error");
        event.preventDefault()
    }

    if (messageContent != '' && toUserId != '' && stompClient) {
        if(currentUserRank == "GUEST" || currentUserRank == "MEMBER") {
            spamCounter.push(messageContent);
        }
        if(spamCounter.length > 5){
            spamCounter.shift();
        }
        if(spamCounter.allValuesSame() && spamCounter.length >= 4){
            showMsg("Please do not Spam", "error");
            $("#pm_chat_input").val("");
        }
        if(spamCounter.allValuesSame() && spamCounter.length >= 5){
            $.post('/user/spamProtection', {
                token: token
            }, function (data) {
                if(data != null){
                    token = data;
                    $("#pm_chat_input").val("");
                    $("#main_chat_input").prop("disabled",true);
                    $("#pm_chat_input").prop("disabled",true);
                    reconnectChat();
                }
            });
        }
        else{
            var chatMessage = {
                toUserId: toUserId,
                content: messageContent
            };
            stompClient.send("/app/chat.sendPm", {'X-Authorization': token}, JSON.stringify(chatMessage));
            $("#pm_chat_input").val("");
            chatMessage = composePrivateMsg(chatMessage);
            renderSendPrivateChatMsg(chatMessage);
            pmLogsControl();
            scrollPmToBottom();
        }

    }
}

function deleteChat(id){
    if(canDelete == "Y"){
        var chatMessage = {
            type: "DELETE",
            content: id
        };
        stompClient.send("/app/chat.sendMessage", {'X-Authorization': token}, JSON.stringify(chatMessage));
    }
    else{
        showMsg("You Don't Have Permission","error")
    }
}
function reportChat(id){
    if(reported < 3) {
        var chatMessage = {
            type: "REPORT",
            content: id
        };
        stompClient.send("/app/chat.sendMessage", {'X-Authorization': token}, JSON.stringify(chatMessage));
        $("#chat_warp #" + id).css("background-color", "#fff3f2");
        showMsg("Reported As Spam Content", "ok")
        reported = reported +1;
        $("#"+id).remove();
    }
    else{
        showMsg("Please wait you already reported 3", "error");
    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.content != null && message.content != "") {
        message.content = escapeHTML(message.content);
    }

    if(message.type === "SAMEUSER"){
        if(message.userId === parseInt(currentUserId)){
            discountChat();
            window.location.href="about:blank";
        }
    }
    if(message.type === "CHAT"){
        message.content =  processEmojis(message.content);
        renderChatMsg(message);

    }
    if(message.type === "CLEAR"){
        $("#chat_warp").html("");
        mainChatNotification(message.userName+" has cleared the chat !")
    }
    if(message.type === "IMAGE"){
        message.content =  processEmojis(message.content);
        renderChatMsg(message);
    }
    if(message.type === "JOIN"){
        addUserList(message.user);
    }
    if(message.type === "JOINMSG"){
        renderJoinMessage(message);
    }
    if(message.type === "LEAVE"){
        if(message.userStatus == 'R'){
            removeUserRJList(message.userId);
        }
        else{
            removeUserList(message.userId);
        }

    }
    if(message.type === "DELETE"){
        $("#chat_warp #"+message.content).remove();
    }
    if(message.type === "REPORT"){
        if(canDelete === 'Y'){
            $("#chat_warp #"+message.id).css("background-color","#fff3f2");
            $("#"+message.id+"_claim").html('<i class="fa-solid fa-flag claimit"  ></i> '+escapeHTML(message.content));
            $("#"+message.id+"_claim").show();
        }
    }
    if(message.type === "SPAM"){
        if(canSpam == "Y"){
            renderChatMsgSpam(message);
        }
        else{
            if(currentUserName == message.userName){
                renderChatMsg(message);
            }
        }
    }


}
function onPmReceived(payload) {

    var message = JSON.parse(payload.body);

    if(message.content != null && message.content != ""){
        message.content = escapeHTML(message.content);
    }
    if(message.msgType === "GETUSERS"){
        getUserList();
    }
    if(message.msgType === "CHAT"){
        if(message.fromUserId != currentUserId){
            renderPrivateChatMsg(message);
            reCheckUnreadMsgs();
        }


    }
    if(message.msgType === "IMAGE"){
        if(message.fromUserId != currentUserId) {
            if (message.image != null && message.image != '') {
                message.content = message.content + ' <img onclick="viewImage(\'' + message.image + '\')" style="height:60px;border-radius: 8px;padding-left: 5px;" src="' + message.image + '" />';
            }

            renderPrivateChatMsg(message);
            reCheckUnreadMsgs();
        }

    }
    if(message.msgType === "KICK"){
        kickPage();
    }
    if(message.msgType === "MUTE"){
        updateMute();
    }
    if(message.msgType === "UNMUTE"){
        updateUnMute();
    }
    if(message.msgType === "BAN"){
        updateBan();
    }
    if(message.msgType === "SPAMMER"){
        updateSpam();
    }
    if(message.msgType === "UNSPAMMER"){
        updateUnSpam();
    }
    if(message.msgType === "RECONNECT"){
        reconnectChat();
    }
    if(message.msgType === "UPDATERJ"){
        updateRJ(message.content);
    }
    if(message.msgType === "RELOAD"){
        updateSession();
    }

}
function updateSession(){
    $.post('/user/updateSession', {
        token: token
    }, function (data) {
        if(data != null){
            token = data;
            reconnectChat();
        }
    });
}
function updateRJ(value){
    $.post('/user/updateRJ', {
        value:value,
        token: token
    }, function (data) {
        if(data != null){
            token = data;
            currentUserRJ = value;
            reconnectChat();
        }
    });
}
function updateMute(){
    $.post('/user/updateMute', {
        token: token
    }, function (data) {
        if(data != null){
            token = data;
            reconnectChat();
            $("#main_chat_input").prop("disabled",true);
            $("#pm_chat_input").prop("disabled",true);
        }

    });
}
function updateUnMute(){
    $.post('/user/updateUnMute', {
        token: token
    }, function (data) {
        if(data != null){
            token = data;
            reconnectChat();
            $("#main_chat_input").prop("disabled",false);
            $("#pm_chat_input").prop("disabled",false);
        }

    });
}
function updateSpam(){
    $.post('/user/updateSpam', {
        token: token
    }, function (data) {
        if(data != null){
            token = data;
            reconnectChat();
        }
    });
}
function updateUnSpam(){
    $.post('/user/updateUnSpam', {
        token: token
    }, function (data) {
        if(data != null){
            token = data;
            reconnectChat();
        }

    });
}
function updateBan(){
    $.post('/user/updateBan', {
        token: token
    }, function (data) {
        logout();
    });
}

function beautyLogs(){
    $(".ch_logs").removeClass("log2");
    $(".ch_logs:even").addClass("log2");
}


setInterval(function (){
    reported = 0;
},(30 * 1000));

function uploadImage(){
    if(currentUserRank != "GUEST" && currentUserRank != "MEMBER" ){
        var html = '<form onsubmit="return false" class="ui form">\n' +
            '  <h4 class="ui dividing header">Upload Image Chat</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
            '<div class="field">\n' +

            '   <button class="ui button primary" onclick="changeMyStatus()" type="button" >Change</button>\n' +
            '</form>';
        smallModal(html);
    }
    else{
        showMsg("You don't have permission","error");
    }
}
var loadChatImageFile = function () {
    var filterType = /^(?:image\/jpeg|image\/jpeg|image\/jpeg|image\/png|image\/PNG)$/i;
    var uploadImage = document.getElementById("uploadChatImage");

    //check and retuns the length of uploded file.
    if (uploadImage.files.length === 0) {
        return;
    }

    //Is Used for validate a valid file.
    var uploadFile = document.getElementById("uploadChatImage").files[0];
    if (!filterType.test(uploadFile.type)) {
        showMsg("Please select a valid image","error");
        return;
    }
    var uploadFileSize = document.getElementById("uploadChatImage").files[0].size;

    if(uploadFileSize >= 3000000){
        showMsg("Max Image Size Limit is 3 MB","error");
        return;
    }
    var fileReader = new FileReader();

    fileReader.onload = function (event) {
        var image = new Image();

        image.onload=function(){
            var canvas=document.createElement("canvas");
            var context=canvas.getContext("2d");
            canvas.width=image.width/2;
            canvas.height=image.height/2;
            context.drawImage(image,
                0,
                0,
                image.width,
                image.height,
                0,
                0,
                canvas.width,
                canvas.height
            );

            openChatImage(canvas.toDataURL("image/png"));
        }
        image.src=event.target.result;
    };
    fileReader.readAsDataURL(uploadFile);
}
var loadChatImageFilePrivate = function () {
    var filterType = /^(?:image\/jpeg|image\/jpeg|image\/jpeg|image\/png|image\/PNG)$/i;
    var uploadImage = document.getElementById("uploadChatImagePrivate");

    //check and retuns the length of uploded file.
    if (uploadImage.files.length === 0) {
        return;
    }

    //Is Used for validate a valid file.
    var uploadFile = document.getElementById("uploadChatImagePrivate").files[0];
    if (!filterType.test(uploadFile.type)) {
        showMsg("Please select a valid image","error");
        return;
    }
    var uploadFileSize = document.getElementById("uploadChatImagePrivate").files[0].size;

    if(uploadFileSize >= 3000000){
        showMsg("Max Image Size Limit is 3 MB","error");
        return;
    }
    var fileReader = new FileReader();

    fileReader.onload = function (event) {
        var image = new Image();

        image.onload=function(){
            var canvas=document.createElement("canvas");
            var context=canvas.getContext("2d");
            canvas.width=image.width/2;
            canvas.height=image.height/2;
            context.drawImage(image,
                0,
                0,
                image.width,
                image.height,
                0,
                0,
                canvas.width,
                canvas.height
            );

            openChatImagePrivate(canvas.toDataURL("image/png"));
        }
        image.src=event.target.result;
    };
    fileReader.readAsDataURL(uploadFile);
}
function openChatImage(imageData){
    closeSmallModal();
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Send Image</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        ' <img id="chatImgSrc" style="width: 100%;text-align: center" src="'+imageData+'" />\n' +
        '    <label style="padding-top: 10px;">Message</label>\n' +
        '    <input type="text" autocomplete="off" id="imageMessage" maxlength="100" placeholder="Type message">\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="sendChatImage()" type="button" >Send</button>\n' +
        '</form>';
    smallModal(html);
}
function openChatImagePrivate(imageData){
    closeSmallModal();
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Send Image</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        ' <img id="chatImgSrc" style="width: 100%;text-align: center" src="'+imageData+'" />\n' +
        '    <label style="padding-top: 10px;">Message</label>\n' +
        '    <input type="text" id="imageMessage" autocomplete="off" maxlength="100" placeholder="Type message">\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="sendPrivateChatImage()" type="button" >Send</button>\n' +
        '</form>';
    smallModal(html);
}

function sendChatImage(){

    var imageData = document.getElementById("chatImgSrc").src;
    var messageContent = $("#imageMessage").val();
    if(imageData != null && imageData != ""){
        var chatMessage = {
            type: "IMAGE",
            content: messageContent,
            image : imageData
        };
        stompClient.send("/app/chat.sendMessage", {'X-Authorization': token}, JSON.stringify(chatMessage));
        closeSmallModal();
        scrollToBottom();
    }

}
function sendPrivateChatImage(){
    var messageContent = $("#pm_chat_input").val();
    if(messageContent != null && messageContent != ""){
        messageContent = escapeHTML(messageContent);
    }

    var toUserId = $("#pmUserId").val();


    if ( toUserId != '' && stompClient) {
        var imageData = document.getElementById("chatImgSrc").src;
        var messageContent = $("#imageMessage").val();
        if (imageData != null && imageData != "") {
            var chatMessage = {
                toUserId: toUserId,
                content: messageContent,
                msgType: "IMAGE",
                image: imageData,
            };
            stompClient.send("/app/chat.sendPm", {'X-Authorization': token}, JSON.stringify(chatMessage));
            $("#pm_chat_input").val("");
            chatMessage = composePrivateMsg(chatMessage);
            renderSendPrivateChatMsg(chatMessage);
            pmLogsControl();
            scrollPmToBottom();
            closeSmallModal();
        }
    }

}
setTimeout(function (){
    if($("#user_"+currentUserId).html() == undefined ||  $("#user_"+currentUserId).html() == '' ||  $("#user_"+currentUserId).html() == null ){
        getUserList();
    }
},9000);
