var userRepo = [];
$( document ).ready(function() {

    $('#chat_warp').scroll(function() {
        var s = $('#chat_warp').scrollTop();
        var c = $('#chat_warp').innerHeight();
        var d = $('#chat_warp')[0].scrollHeight;
        if (s + c >= d - 600) {
            $("#scroll_Policy").val("1");
        }
        else {
            $("#scroll_Policy").val("0");
        }

    });

    $(document).on('click', '.appendMainEmo', function (e) {
        var code = e.target.getAttribute("data");
        var target = "#main_chat_input";
        var content = $(target).val();
        content = content + ' ' + code;
        $(target).val(content);
        setTimeout(function() { $(target).focus(); }, 0);
        var that = $(target);
        $(target).focus().val($(target).val());
        $("#smily_box").hide();
    });
    $(document).on('click', '.appendMainEmoPrivate', function (e) {
        var code = e.target.getAttribute("data");
        var target = "#pm_chat_input";
        var content = $(target).val();
        content = content + ' ' + code;
        $(target).val(content);
        setTimeout(function() { $(target).focus(); }, 0);
        var that = $(target);
        $(target).focus().val($(target).val());
        $("#smily_boxPrivate").hide();
    });

});
$(document).on('click', '.usernamechat', function () {
    $("#main_chat_input").val($("#main_chat_input").val() +' '+ this.innerText+ ' ');
    setTimeout(function() { $("#main_chat_input").focus(); }, 0);
    var that = $("#main_chat_input");
    $("#main_chat_input").focus().val($("#main_chat_input").val());

});
String.prototype.replaceAll = function (find, replace) {
    var str = this;
    return str.replace(new RegExp(find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), replace);
};
async function seenPrivateChat(fromUserId){
    $.post('/user/seenMsgs', {
        fromUserId: fromUserId,
        token: token
    }, function (data) {
        if(data != 0){
            $("#messageCount").text(data);
            $("#messageCount").show();
        }
        else{
            $("#messageCount").text("");
            $("#messageCount").hide();
        }
    });
}

function reCheckUnreadMsgs(){

    $.post('/user/checkUnreadMsgs', {
        token: token
    }, function (data) {
        if(data != 0){
            $("#messageCount").text(data);
            $("#messageCount").show();
        }
        else{
            $("#messageCount").text("");
            $("#messageCount").hide();
        }
    });
}
function clearMyMessages(){
    closeFullModal();
    $.post('/user/clearAllMsgs', {
        token: token
    }, function (data) {
        if(data == "Y"){
            showMsg("All Messages Cleared","ok");
            $("#messageCount").text("");
            $("#messageCount").hide();
        }


    });

}
function uploadMyDp(imageData){
    if(currentUserRank != 'GUEST'){
        $.post('/user/changeMyDP', {
            imageData :imageData,
            token: token
        }, function (data) {
            if(data != null && data != ''){
                token = data.token;
                showMsg("Dp Changed Successfully","OK");
                closeUserProfileEdit();
                refreshChat();
            }
            else{
                showMsg("You Don't have Permission","error");
            }
        });
    }
    else {
        showMsg("DP Not Allowed For Guest", "error");
    }


}
var loadImageFile = function () {
    var filterType = /^(?:image\/jpeg|image\/jpeg|image\/jpeg|image\/png|image\/PNG)$/i;
    var uploadImage = document.getElementById("upload-Image");

    //check and retuns the length of uploded file.
    if (uploadImage.files.length === 0) {
        return;
    }

    //Is Used for validate a valid file.
    var uploadFile = document.getElementById("upload-Image").files[0];
    if (!filterType.test(uploadFile.type)) {
        showMsg("Please select a valid image","error");
        return;
    }
    var uploadFileSize = document.getElementById("upload-Image").files[0].size;

    if(uploadFileSize >= 1000000){
        showMsg("Max Image Size Limit is 1 MB","error");
        return;
    }
    var fileReader = new FileReader();

    fileReader.onload = function (event) {
        var image = new Image();

        image.onload=function(){
            var canvas=document.createElement("canvas");
            var context=canvas.getContext("2d");
            canvas.width=image.width/4;
            canvas.height=image.height/4;
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

            uploadMyDp(canvas.toDataURL("image/png"));
        }
        image.src=event.target.result;
    };
    fileReader.readAsDataURL(uploadFile);
}
function changeMyDp(){
    $("#upload-Image").click();
}

function showMsg(msg,type){
    var check = $("#currentAlert").val();
    if(type != "" && check == "N") {
        $("#alertmsg_content").text(msg);
        if (type === "error") {
            $("#alertmsg").css("background-color", "#cc2900");
        }
        else {
            $("#alertmsg").css("background-color", "#009900");
        }
        $('#alertmsg').fadeIn('slow');
        $("#alertmsg").show();
        $("#currentAlert").val("Y");
        setTimeout(function () {
            $('#alertmsg').fadeIn('slow');
            $("#alertmsg_content").text("");
            $("#currentAlert").val("N");
            $("#alertmsg").hide();
        },3500);
    }

}
function openWriteOption(){
    $("#writeOptionModal").toggle();
    $("#smily_box").hide();
}
function openWriteOptionPrivate(){
    $("#writeOptionModal").hide();
    $("#smily_box").hide();
    $("#writeOptionModalPrivate").toggle();
    $("#smily_boxPrivate").hide();
}
function openUserList() {
    var width = $(window).width();
    if (width < 800) {
        if ($("#chat_users").is(':hidden')) {
            $("#mainchat").hide();
            $("#chat_users").show();
        }
        else {
            $("#chat_users").hide();
            $("#mainchat").show();
        }
    }
}
function scrollPmToBottom() {
    $('#privateModalhtml').scrollTop($('#privateModalhtml')[0].scrollHeight);

}
function closeMenu(){
    $("#profile_drop_menu").hide();
}
function smallModal(html){
    $("#smallModalHtml").html(html);
    $("#smallModal").show();
}
function closeSmallModal(){
    $("#smallModalHtml").html("");
    $("#smallModal").hide();
}
function userProfile(userData){
    $("#profile_username").text(escapeHTML(userData.userName));
    if(userData.tweet != ""){
        $("#profile_tweet").text(escapeHTML(userData.tweet));
    }
    $("#profileDp").attr("src",'https://chat-links.com'+userData.dp);
    $("#profileRankIcon").attr("src",userData.rankIcon);
    var userRankName = escapeHTML(userData.rankName);
    if(currentUserRank == 'OWNER'){
        userRankName = userRankName + ' <i onclick="changeUserRankDialog(\''+userData.rankId+'\',\''+userData.userId+'\')" class="fa-solid fa-pen-to-square" style="padding-left:8px;" ></i>';
    }
    $("#profileRankName").html(userRankName);
    var messageHtml = '<i class="fas fa-envelope" onclick="openPrivateChat(\'' +userData.userId+ '\',\'' +escapeHTML(userData.userName)+ '\');" style="font-size: 24px;color: white;vertical-align: middle;padding-right: 10px;" aria-hidden="true"></i>';
    $("#profileMessage").html(messageHtml)
    var html  = '';
    if(userData.status != undefined && userData.status != null && userData.status != ""){
        var label = 'Not Available';
        if(userData.status == "O"){
            label = '<p style="color: green; font-weight: bold">Online</p>';
        }
        if(userData.status == "M"){
            label = '<p style="color: red; font-weight: bold">Muted</p>';
        }
        if(userData.status == "S"){
            label = '<p style="color: darkgray; font-weight: bold">Stay';
        }
        if(userData.status == "A"){
            label = '<p style="color: orange; font-weight: bold">Away</p>';
        }
        if(userData.status == "B"){
            label = '<p style="color: red; font-weight: bold">Busy</p>';
        }
        if(userData.status == "E"){
            label = '<p style="color: chocolate; font-weight: bold">Eating</p>';
        }
        if(userData.status == "G"){
            label = '<p style="color: #0c5460; font-weight: bold">Gaming</p>';
        }
        if(userData.ban == "Y"){
            label = '<p style="color: red; font-weight: bold">Banned</p>';
        }
        if(userData.spam == "Y"){
            label = '<p style="color: red; font-weight: bold">Spammer</p>';
        }
        if(userData.status == "R"){
            label = '<p style="color: green; font-weight: bold">RJ/DJ</p>';
        }


        var detailHtml = '<tr>\n' +
            '                                <th>Status</th>\n' +
            '                                <td>'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(userData.gender != undefined && userData.gender != null && userData.gender != ""){
        var label = 'Rather not say';
        if(userData.gender == "M"){
            label = 'Male';
        }
        if(userData.gender == "F"){
            label = 'Female';
        }
        if(userData.gender == "O"){
            label = 'Other';
        }
        var detailHtml = '<tr>\n' +
            '                                <th>Gender</th>\n' +
            '                                <td>'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(userData.joinDate != undefined && userData.joinDate != null && userData.joinDate != ""){
        var label = 'Not Available';
        var  joinDate =  new Date(userData.joinDate);
        var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        joinDate = joinDate.toLocaleDateString("en-US", options);
        label = joinDate;
        var detailHtml = '<tr>\n' +
            '                                <th>Joining Date</th>\n' +
            '                                <td>'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(userData.about != undefined && userData.about != null && userData.about != ""){
        var label = escapeHTML(userData.about);
        $("#profileAbout").text(label);
    }
    if(userData.birthDate != undefined){
        var label = 'Not Available';
        if(userData.birthDate != "" && userData.birthDate != null){
            var years = new Date(new Date() - new Date(userData.birthDate)).getFullYear() - 1970;
            if(years != 0){
                label = (years - 1) + ' Years';
            }

        }
        var detailHtml = '<tr>\n' +
            '                                <th>Age</th>\n' +
            '                                <td>'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(userData.rj != undefined && userData.rj != null && userData.rj != ""){

        var label = 'No';
        if(userData.rj == "Y"){
            label = 'Yes';
        }
        if(userData.rj == "N"){
            label = 'No';
        }
        if(currentUserRank == 'OWNER'){
            label = label + '<i class="fa-solid fa-pen-to-square" style="padding-left:8px;" ></i>';
        }
        var detailHtml = '<tr>\n' +
            '                                <th>RJ/DJ</th>\n' +
            '                                <td onclick="makeRJDJDialog(\''+userData.rj+'\',\''+userData.userId+'\')" >'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if( userData.points !== ""){
        var label = '<i style="color: orange;font-size: 14px;padding-right: 4px;" class="fa-solid fa-coins"></i> ' +userData.points;

        var detailHtml = '<tr>\n' +
            '                                <th>Points</th>\n' +
            '                                <td>'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }

    if(currentUserRankId < userData.rankId){
        html = html + adminOptions(userData);
    }

    $("#profileDetails").append(html);
    $("#profileCard").show();
}
function adminOptions(userData){
    var html = '';
    if(canBan == "Y"){
        var label = '<div class="ui mini label" onclick="getUserIP(\''+userData.userName+'\')" style="cursor: pointer">Get</div>';

        var detailHtml = '<tr>\n' +
            '                                <th>IP</th>\n' +
            '                                <td id="profileIP">'+label+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(canSpam == "Y"){
        var value = '';
        if(userData.spam == "Y"){
            value = 'checked';
        }
        var control = '<div onclick="spamUserDialog(\'' +userData.userId+ '\',\'' +userData.userName+ '\')" class="ui toggle checkbox '+value+' spamCheckbox">\n' +
            '      <input type="checkbox" id="spamStatus" tabindex="0" '+value+' class="hidden spamCheckbox"><label></label>\n' +
            '    </div>';

        var detailHtml = '<tr>\n' +
            '                                <th>Spammer</th>\n' +
            '                                <td >'+control+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(canKick == "Y"){
        var control = '<div  onclick="kickUserDialog(\'' +userData.userId+ '\',\'' +userData.userName+ '\')" class="ui toggle checkbox kickCheckbox">\n' +
            '      <input type="checkbox" tabindex="0"  class="hidden kickCheckbox"><label></label>\n' +
            '    </div>';

        var detailHtml = '<tr>\n' +
            '                                <th>Kick</th>\n' +
            '                                <td >'+control+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(canMute == "Y"){
        var value = '';
        if(userData.status == "M"){
            value = 'checked';
        }
        var control = '<div onclick="muteUserDialog(\'' +userData.userId+ '\',\'' +userData.userName+ '\')" class="ui toggle checkbox '+value+' muteCheckbox">\n' +
            '      <input id="muteStatus" type="checkbox" tabindex="0" '+value+' class="hidden muteCheckbox"><label></label>\n' +
            '    </div>';

        var detailHtml = '<tr>\n' +
            '                                <th>Mute</th>\n' +
            '                                <td >'+control+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    if(canBan == "Y"){
        var value = '';
        if(userData.ban == "Y"){
            value = 'checked';
        }
        var control = '<div onclick="banUserDialog(\'' +userData.userId+ '\',\'' +userData.userName+ '\')" class="ui toggle checkbox '+value+' banCheckbox">\n' +
            '      <input id="banStatus" type="checkbox" tabindex="0" '+value+' class="hidden banCheckbox"><label></label>\n' +
            '    </div>';

        var detailHtml = '<tr>\n' +
            '                                <th>Ban</th>\n' +
            '                                <td >'+control+'</td>\n' +
            '                            </tr>';
        html = html + detailHtml;
    }
    return html
}
function openUserProfile(userId){
    if(userId in userRepo){
        var userData = userRepo[userId];
        userProfile(userData);
    }
    else{
        $.post('/user/openUserProfile', {
            userId: userId,
            token: token
        }, function (data) {
            if(data != null && data.userId != null){
                userProfile(data);
            }
            else{
                showMsg("User Not Found","error");
            }

        });
    }

}
function manageUsers(){
    if(currentUserRank == "OWNER"){
        closeMenu();

        $.post('/user/manageUsers', {
            token: token
        }, function (data) {
            var dashboard = '<div class="ui container" style="margin-top: 10px;" ><div class="ui labels">\n' +
                '  <a onclick="showUsers()" class="ui green label">Users<div class="detail">'+data.users+'</div></a>\n' +
                '  <a onclick="showMutedUsers()" class="ui orange label">Muted<div class="detail">'+data.mutes+'</div></a>\n' +
                '  <a onclick="showBannedUsers()" class="ui red label">Banned<div class="detail">'+data.banned+'</div></a>\n' +
                '  <a onclick="showRJUsers()" class="ui blue label">RJ/DJ<div class="detail">'+data.rj+'</div></a>\n' +
                '</div></div>';


            var html = '<div class="ui form" style="padding: 14px;">\n' +
                '<div class="field">\n' +
                '    <label>Search Username</label>\n' +
                '    <input type="text" id="searchValue" placeholder="Username">\n' +
                '  </div>\n' +
                '<button onclick="findUser()" class="ui button primary">Search</button>\n' +
                '<div id="searchResult" style="margin-top: 15px;"></div>'
            '  </div>';

            html = dashboard + html;
            openFullModal("Manage Users",html);
        });

    }
}
function ipBanned(){
    if(canBan == "Y"){
        closeMenu();
        $.post('/user/manageips', {
            token: token
        }, function (data) {
            var html = '<div class="ui form" style="padding: 14px;"><div class="ui middle aligned list">';
            if(data != null && data.length != 0){
                for (var i = 0; i<data.length ; i++){
                    html = html + '<div id="banned_'+data[i].id+'" class="item">\n' +
                        '  <i class="fa-solid fa-globe ui avatar image" style="font-size: 25px;color: red"></i>\n' +
                        '    <div class="content">\n' +
                        '      <div class="header">'+data[i].ip+'<i onclick="removeIp(\'' +data[i].id+ '\')" class="fa-solid fa-circle-xmark clIcons" style="font-size: 20px;position: absolute;right: 0px;"></i></div>\n' +
                        '      <div class="description">'+data[i].username+'</div>\n' +
                        '    </div>\n' +
                        '  </div>\n';
                }
            }
            else{
                html = html + '<div style="text-align: center;" ><i class="fa-solid fa-circle-exclamation"></i> <span> No record found </span></div>';
            }

            html = html + '</div></div>';
            openFullModal("Manage IP Banned",html);
        });

    }
}
function removeIp(id){
    if(canBan == "Y"){
        $.post('/user/removeIp', {
            id:id,
            token: token
        }, function (data) {
            if(data == "Y"){
                $("#banned_"+id).remove();
            }
        });
    }
}
function findUser(){
    if(currentUserRank == "OWNER"){
        var search = $("#searchValue").val();
        if(search != null && search != ""){
            $.post('/user/findUser', {
                search :search,
                token: token
            }, function (data) {
                if(data != null){
                    var html = '';
                    for(var i = 0; i < data.length ; i++ ){
                        html = html + renderSearchUser(data[i]);
                    }
                    if(html == ''){
                        html = '<div style="text-align: center;" ><i class="fa-solid fa-circle-exclamation"></i> <span> No record found </span></div>';
                    }
                    html = html + '<br>';
                    $("#searchResult").html(html);
                }
            });
        }
    }
}
function showMutedUsers(){
    if(currentUserRank == "OWNER"){
        $.post('/user/showMutedUsers', {
            token: token
        }, function (data) {
            if(data != null){
                var html = '';
                for(var i = 0; i < data.length ; i++ ){
                    html = html + renderSearchUser(data[i]);
                }
                if(html == ''){
                    html = '<div style="text-align: center;" ><i class="fa-solid fa-circle-exclamation"></i> <span> No record found </span></div>';
                }
                html = html + '<br>';
                $("#searchResult").html(html);
            }
        });
    }
}
function showUsers(){
    if(currentUserRank == "OWNER"){
        $.post('/user/showUsers', {
            token: token
        }, function (data) {
            if(data != null){
                var html = '';
                for(var i = 0; i < data.length ; i++ ){
                    html = html + renderSearchUser(data[i]);
                }
                if(html == ''){
                    html = '<div style="text-align: center;" ><i class="fa-solid fa-circle-exclamation"></i> <span> No record found </span></div>';
                }
                html = html + '<br>';
                $("#searchResult").html(html);
            }
        });
    }
}
function showBannedUsers(){
    if(currentUserRank == "OWNER"){
        $.post('/user/showBannedUsers', {
            token: token
        }, function (data) {
            if(data != null){
                var html = '';
                for(var i = 0; i < data.length ; i++ ){
                    html = html + renderSearchUser(data[i]);
                }
                if(html == ''){
                    html = '<div style="text-align: center;" ><i class="fa-solid fa-circle-exclamation"></i> <span> No record found </span></div>';
                }
                html = html + '<br>';
                $("#searchResult").html(html);
            }
        });
    }
}
function showRJUsers(){
    if(currentUserRank == "OWNER"){
        $.post('/user/showRJUsers', {
            token: token
        }, function (data) {
            if(data != null){
                var html = '';
                for(var i = 0; i < data.length ; i++ ){
                    html = html + renderSearchUser(data[i]);
                }
                if(html == ''){
                    html = '<div style="text-align: center;" ><i class="fa-solid fa-circle-exclamation"></i> <span> No record found </span></div>';
                }
                html = html + '<br>';
                $("#searchResult").html(html);
            }
        });
    }
}
function renderSearchUser(data){

    var nameColor = '';
    if(data.nameColor != "" && data.nameColor != null){
        nameColor = 'style="color: '+data.nameColor+'"';
    }

    var html = '<div  onclick="openUser(\'' +data.id+ '\');"  class="borderuserlist" >' +
        '<div    class="userssort list_element list_item user_lm_box">\n' +
        '<div class="user_lm_avatar" ><img class="avsex avatar_userlist glob_av" style="border-color: #03add8" src="https://chat-links.com' + data.dp + '"></div>\n' +
        '<div class="user_lm_data"><div '+nameColor+' class="username bellips marginusername" >' + data.userName + '</div></div>\n' +
        '<div class="user_lm_icon icrank"><i class="fa-solid fa-magnifying-glass clIcons" style="font-size: 25px;"></i></div>\n' +
        '</div>\n' +
        '</div>';
    return html;
}
function openUser(userId){

    if(currentUserRank == "OWNER" && userId != null){
        $.post('/user/openUser', {
            userId :userId,
            token: token
        }, function (data) {
            if(data != null){
                if(data != null){
                    userProfile(data);
                }
            }
        });
    }
}
function openUserProfileEdit(userId){
    if(userId in userRepo){
        closeMenu();

        var userData = userRepo[userId];
        $("#profile_usernameEdit").text(userData.userName);
        if(userData.tweet != ""){
            $("#profile_tweetEdit").text(escapeHTML(userData.tweet));
        }
        $("#profileDpEdit").attr("src",userData.dp);
        $("#profileRankIconEdit").attr("src",userData.rankIcon);
        $("#profileRankNameEdit").text(userData.rankName);
        var html  = '';
        if(userData.status != undefined && userData.status != null && userData.status != ""){
            var label = '<p style="color: grey; font-weight: bold"> Not Available';
            if(userData.status == "O"){
                label = '<p style="color: green; font-weight: bold">Online';
            }
            if(userData.status == "M"){
                label = '<p style="color: red; font-weight: bold">Muted';
            }
            if(userData.status == "S"){
                label = '<p style="color: darkgray; font-weight: bold">Stay';
            }
            if(userData.status == "A"){
                label = '<p style="color: orange; font-weight: bold">Away';
            }
            if(userData.status == "B"){
                label = '<p style="color: red; font-weight: bold">Busy';
            }
            if(userData.status == "E"){
                label = '<p style="color: chocolate; font-weight: bold">Eating';
            }
            if(userData.status == "G"){
                label = '<p style="color: #0c5460; font-weight: bold">Gaming';
            }
            if(userData.ban == "Y"){
                label = '<p style="color: red; font-weight: bold">Banned';
            }
            if(userData.spam == "Y"){
                label = '<p style="color: green; font-weight: bold">Online';
            }
            if(userData.status == "R"){
                label = '<p style="color: green; font-weight: bold">RJ/DJ';
            }


            var detailHtml = '<tr>\n' +
                '                                <th>Status</th>\n' +
                '                                <td onclick="changeStatusDialog()" >'+label+' <i class="fa-solid fa-pen-to-square" style="padding-left:4px;color: black !important;" ></i></p></td>\n' +
                '                            </tr>';
            html = html + detailHtml;
        }
        if(userData.gender != undefined && userData.gender != null && userData.gender != ""){
            var label = 'Rather not say';
            if(userData.gender == "M"){
                label = 'Male';
            }
            if(userData.gender == "F"){
                label = 'Female';
            }
            if(userData.gender == "O"){
                label = 'Other';
            }
            var detailHtml = '<tr>\n' +
                '                                <th>Gender</th>\n' +
                '                                <td onclick="changeGenderDialog()" >'+label+' <i class="fa-solid fa-pen-to-square" style="padding-left:4px;" ></i></td>\n' +
                '                            </tr>';
            html = html + detailHtml;
        }
        if(userData.joinDate != undefined && userData.joinDate != null && userData.joinDate != ""){
            var label = 'Not Available';
            var  joinDate =  new Date(userData.joinDate);
            var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
            joinDate = joinDate.toLocaleDateString("en-US", options);
            label = joinDate;
            var detailHtml = '<tr>\n' +
                '                                <th>Joining Date</th>\n' +
                '                                <td>'+label+'</td>\n' +
                '                            </tr>';
            html = html + detailHtml;
        }
        if(userData.about != undefined && userData.about != null && userData.about != ""){
            var label = escapeHTML(userData.about);
            $("#profileAboutEdit").text(label);
        }
        if(userData.birthDate != undefined){
            var label = 'Not Available';
            if(userData.birthDate != "" && userData.birthDate != null){
                var years = new Date(new Date() - new Date(userData.birthDate)).getFullYear() - 1970;
                if(years != 0){
                    label = (years - 1) + ' Years';
                }

            }
            var detailHtml = '<tr>\n' +
                '                                <th>Age</th>\n' +
                '                                <td onclick="changeDOBDialog()" >'+label+' <i class="fa-solid fa-pen-to-square" style="padding-left:4px;" ></i></td>\n' +
                '                            </tr>';
            html = html + detailHtml;
        }
        if(userData.rj != undefined && userData.rj != null && userData.rj != ""){
            var label = 'No';
            if(userData.rj == "Y"){
                label = 'Yes';
            }
            if(userData.rj == "N"){
                label = 'No';
            }
            var detailHtml = '<tr>\n' +
                '                                <th>RJ/DJ</th>\n' +
                '                                <td>'+label+'</td>\n' +
                '                            </tr>';
            html = html + detailHtml;
        }
        if( userData.points !== ""){
            var label = '<i style="color: orange;font-size: 14px;padding-right: 4px;" class="fa-solid fa-coins"></i> ' +userData.points;

            var detailHtml = '<tr>\n' +
                '                                <th>Points</th>\n' +
                '                                <td>'+label+'</td>\n' +
                '                            </tr>';
            html = html + detailHtml;
        }
        $("#profileDetailsEdit").append(html);
        $("#profileCardEdit").show();
    }

}
function changeAboutDialog(){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change About Me</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>About Me</label>\n' +
        '<textarea type="text" maxlength="150"  id="myAboutMe" name="myAboutMe"></textarea>\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="changeAbout()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}
function changeAbout(){
    if(currentUserRank != 'GUEST'){
        var about = $("#myAboutMe").val();
        if(about != null && about != undefined && about != ""){
            $.post('/user/changeMyAbout', {
                about :about,
                token: token
            }, function (data) {
                if(data != null){
                    token = data;
                    showMsg("Changed Successfully","OK");
                    closeSmallModal();
                    reconnectChat();
                    closeUserProfileEdit();
                }
                else{
                    showMsg("You Don't Have Permission","error");
                }
            });
        }
        else{
            showMsg("Invaild Value Please Try Again","error");
        }
    }
    else {
        showMsg("Not Allowed For Guest", "error");
    }

}
function changeDOBDialog(){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change Your Date of Birth</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Date of Birth</label>\n' +
        '<input type="date" max="2012-12-31" min="1970-12-31" id="mybirthday" name="mybirthday">\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="changeDOB()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}
function changeDOB(){

    var dob = $("#mybirthday").val();
    dob = Date.parse(dob);
    if(dob != null && dob != undefined && dob != ""){
        $.post('/user/changeDOB', {
            dob :dob,
            token: token
        }, function (data) {
            if(data != null){
                token = data;
                showMsg("Changed Successfully","OK");
                closeSmallModal();
                reconnectChat();
                closeUserProfileEdit();
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });
    }
    else{
        showMsg("Invaild Value Please Try Again","error");
    }
}
function changeStatusDialog(){
    var rjhtml = '';
    if(currentUserRJ == 'Y'){
        rjhtml = '<option value="R">RJ/DJ</option>\n';
    }
    var staytml = '';
    if(currentUserRank != "GUEST" && currentUserRank != "MEMBER"){
        staytml = '<option value="S">Stay</option>\n';
    }
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change Your Status</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Status</label>\n' +
        '    <select value="N" id="myStatusInput" >\n' +
        '      <option value="" selected >Select Status</option>\n' +
        '      <option value="O">Online</option>\n' + staytml +'\n'+ rjhtml +
        '      \n<option value="A">Away</option>\n' +
        '      <option value="B">Busy</option>\n' +
        '      <option value="E">Eating</option>\n' +
        '      <option value="G">Gaming</option>\n' +
        '    </select>\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="changeMyStatus()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}
function changeUserRankDialog(rankId,userId){

    if(currentUserRank == 'OWNER') {

        $.post('/user/getRankList', {
            token: token
        }, function (data) {
            if(data != null){
                var rankOptionHtml = '';

                for(var i = 0; i < data.length ; i++){
                    var option = '';
                    if(rankId == data[i].id){
                        option = '<option value="'+data[i].id+'" selected >'+data[i].name+'</option>\n';
                    }
                    else{
                        option = '<option value="'+data[i].id+'" >'+data[i].name+'</option>';
                    }

                    rankOptionHtml = rankOptionHtml + option;
                }

                var html = '<form onsubmit="return false" class="ui form">\n' +
                    '  <h4 class="ui dividing header">Change User Rank</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
                    '<div class="field">\n' +
                    '    <label>Rank</label>\n' +
                    '    <select value="'+rankId+'" id="changeRankValue" >\n' +
                    ' '+rankOptionHtml+'\n'+
                    '    </select>\n' +
                    '  </div>\n' +
                    '   <button class="ui button primary" onclick="changeUserRank(\'' +userId+ '\')" type="button" >Change</button>\n' +
                    '</form>';
                smallModal(html);
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });


    }
}
function changeUserRank(userId){

    if(currentUserRank == 'OWNER'){
        var value =  $("#changeRankValue").val();
        if(value != null && value != undefined && value != "" && userId != null && userId != ""){
            $.post('/user/changeUserRank', {
                value :value,
                userId :userId,
                token: token
            }, function (data) {
                if(data == "Y"){
                    showMsg("Changed Successfully","OK");
                    closeSmallModal();
                    closeUserProfile();
                }
                else{
                    showMsg("You Don't Have Permission","error");
                }
            });
        }
        else{
            showMsg("Invaild Value Please Try Again","error");
        }
    }
    else {
        showMsg("Not Allowed", "error");
    }

}
function makeRJDJDialog(value,userId){

    if(currentUserRank == 'OWNER') {
        var optionYes = '';
        var optionNo = '';
        if(value == "Y"){
            optionYes = '<option value="Y" selected >Yes</option>';
        }
        else{
            optionYes = '<option value="Y" >Yes</option>';
        }
        if(value == "N"){
            optionNo = '<option value="N" selected >No</option>';
        }
        else{
            optionNo = '<option value="N" >No</option>';
        }

        var html = '<form onsubmit="return false" class="ui form">\n' +
            '  <h4 class="ui dividing header">Change RJ/DJ</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
            '<div class="field">\n' +
            '    <label>RJ/DJ</label>\n' +
            '    <select value="'+value+'" id="changeRJValue" >\n' +
            ' '+optionYes+'\n'+
            ' '+optionNo+'\n'+
            '    </select>\n' +
            '  </div>\n' +
            '   <button class="ui button primary" onclick="changeRJ(\'' +userId+ '\')" type="button" >Change</button>\n' +
            '</form>';
        smallModal(html);
    }
}
function changeRJ(userId){

    if(currentUserRank == 'OWNER'){
        var value =  $("#changeRJValue").val();
        if(value != null && value != undefined && value != "" && userId != null && userId != ""){
            $.post('/user/changeRJ', {
                value :value,
                userId :userId,
                token: token
            }, function (data) {
                if(data == "Y"){
                    showMsg("Changed Successfully","OK");
                    closeSmallModal();
                    closeUserProfile();
                }
                else{
                    showMsg("You Don't Have Permission","error");
                }
            });
        }
        else{
            showMsg("Invaild Value Please Try Again","error");
        }
    }
    else {
        showMsg("Not Allowed", "error");
    }

}
function changeMyStatus(){
    if(currentUserRank != 'GUEST'){
        var status = $("#myStatusInput").val();
        if(status != null && status != undefined && status != ""){
            $.post('/user/changeMyStatus', {
                status :status,
                token: token
            }, function (data) {
                if(data != null){
                    token = data;
                    showMsg("Changed Successfully","OK");
                    closeSmallModal();
                    reconnectChat();
                    closeUserProfileEdit();
                }
                else{
                    showMsg("You Don't Have Permission","error");
                }
            });
        }
        else{
            showMsg("Invaild Value Please Try Again","error");
        }
    }
    else {
        showMsg("Not Allowed For Guest", "error");
    }

}
function changeGenderDialog(){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change Your Gender</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Gender</label>\n' +
        '    <select value="N" id="myGenderInput" >\n' +
        '      <option value="" selected >Select Gender</option>\n' +
        '      <option value="N">Rather not say</option>\n' +
        '      <option value="M">Male</option>\n' +
        '      <option value="F">Female</option>\n' +
        '      <option value="O">Other</option>\n' +
        '    </select>\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="changeMyGender()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}
function writeMenuDialog(){
    var boldoptionYes = '';
    if(currentUserBold == 'Y'){
        boldoptionYes = 'selected';
    }
    var boldoptionNo = '';
    if(currentUserBold == 'N'){
        boldoptionNo = 'selected';
    }
    $("#writeOptionModal").hide();
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Customize Writing</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Text Bold</label>\n' +
        '    <select value="'+currentUserBold+'" id="mybold" >\n' +
        '      <option value="N" '+boldoptionYes+' >No</option>\n' +
        '      <option value="Y" '+boldoptionNo+' >Yes</option>\n' +
        '    </select>\n' +
        '  </div>\n' +
        '<div class="field">\n' +
        '    <label>Text Color</label>\n' +
        '    <input type="color" value="'+currentTextColor+'" id="myTextColor" />\n' +
        '  </div>\n' +
        '  <br> <button class="ui button primary" onclick="changeMyWriting()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
    if(currentUserBold == "Y"){
        $('#mybold').prop('selectedIndex', 1);
    }
    else{
        $('#mybold').prop('selectedIndex', 0);
    }

}
function changeMyWriting(){
    var bold = $("#mybold").val();
    var color = $("#myTextColor").val();
    if(bold != null && bold != "" && color != null && color != ""){
        $.post('/user/changeMyWriting', {
            bold :bold,
            color :color,
            token: token
        }, function (data) {
            if(data != null){
                token = data;
                showMsg("Changed Successfully","OK");
                closeSmallModal();
                currentTextColor = color;
                currentUserBold = bold;
                reconnectChat();
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });
    }
    else{
        showMsg("Invaild Value Please Try Again","error");
    }
}
function changeMyGender(){
    var gender = $("#myGenderInput").val();
    if(gender != null && gender != undefined && gender != ""){
        $.post('/user/changeMyGender', {
            gender :gender,
            token: token
        }, function (data) {
            if(data != null){
                token = data;
                showMsg("Changed Successfully","OK");
                closeSmallModal();
                reconnectChat();
                closeUserProfileEdit();
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });
    }
    else{
        showMsg("Invaild Value Please Try Again","error");
    }
}
function changeUserNameDialog(){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change Your Username</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Username</label>\n' +
        '    <input  onkeypress="return /^[a-zA-Z0-9_-]*$/i.test(event.key)" type="text" required id="changeUserNameInput" maxlength="12" placeholder="Enter new username">\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="changeUserName()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}
function changeTweetDialog(){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change Your Tweet</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Tweet</label>\n' +
        '    <input  onkeypress="return /^[a-zA-Z0-9_-\\s]*$/i.test(event.key)" type="text" required id="changeTweetInput" maxlength="40" placeholder="Enter new tweet">\n' +
        '  </div>\n' +
        '   <button class="ui button primary" onclick="changeTweet()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}
function processChatImage(){
    if(currentUserRank != "GUEST" && currentUserRank != "MEMBER" ){
        $("#uploadChatImage").click();
    }
    else{
        showMsg("You don't have permission","error");
    }
}
function processChatImagePrivate(){
    if(currentUserRank != "GUEST" && currentUserRank != "MEMBER" ){
        $("#uploadChatImagePrivate").click();
    }
    else{
        showMsg("You don't have permission","error");
    }
}
function changePassword(){
    if(currentUserRank != 'GUEST'){
        closeMenu();
        var html = '<form onsubmit="return false" class="ui form">\n' +
            '  <h4 class="ui dividing header">Change Your Password</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
            '<div class="field">\n' +
            '    <label>New Password</label>\n' +
            '    <input type="text" required id="changePasswordInput" maxlength="20" placeholder="Enter new password">\n' +
            '  </div>\n' +
            '   <button class="ui button primary" onclick="changeMyPassword()" type="button" >Change</button>\n' +
            '</form>';
        smallModal(html);
    }

}
function changeMyPassword(){
    var password = $("#changePasswordInput").val();
    if(password != null && password != undefined && password != ""){
        $.post('/user/changeMyPassword', {
            password :password,
            token: token
        }, function (data) {
            if(data == "Y"){
                showMsg("Changed Successfully","OK");
                closeSmallModal();
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });
    }
    else{
        showMsg("Invaild Value Please Try Again","error");
    }
}
function changeUserNameColorDialog(){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Change Your Username Color</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div class="field">\n' +
        '    <label>Color</label>\n' +
        '    <input  type="color" required id="myColor">\n' +
        '  </div>\n' +
        '   <br><button class="ui button primary" onclick="changeUserNameColor()" type="button" >Change</button>\n' +
        '</form>';
    smallModal(html);
}

function changeTweet(){
    var tweet = $("#changeTweetInput").val();
    if(tweet != null && tweet != undefined && tweet != ""){
        $.post('/user/changeTweet', {
            tweet :tweet,
            token: token
        }, function (data) {
            if(data != null){
                token = data;
                $("#mytweet").html(tweet + '</span> <i onclick="changeTweetDialog()" class="fa-solid fa-pen-to-square" style="padding-left: 8px;"></i>');
                showMsg("Changed Successfully","OK");
                closeSmallModal();
                reconnectChat();
                closeUserProfileEdit();
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });
    }
    else{
        showMsg("Invaild Value Please Try Again","error");
    }
}
function changeUserNameColor(){
    var color = $("#myColor").val();
    if(color != null && color != undefined && color != ""){
        $.post('/user/changeMyUserNameColor', {
            color :color,
            token: token
        }, function (data) {
            if(data != null){
                token = data;
                showMsg("Changed Successfully","OK");
                closeSmallModal();
                reconnectChat();
                closeUserProfileEdit();
            }
            else{
                showMsg("You Don't Have Permission","error");
            }
        });
    }
    else{
        showMsg("Invaild Value Please Try Again","error");
    }
}

function changeUserName(){
    if(currentUserRank  != 'GUEST') {
        var username = $("#changeUserNameInput").val();
        if (username != null && username != undefined && username != "" && username.length >= 3) {
            $.post('/user/changeUserName', {
                username: username,
                token: token
            }, function (data) {
                if (data == "Y") {
                    showMsg("Changed Successfully Please Login Again", "OK");
                    closeSmallModal();
                    logout();
                }
                if (data == "B") {
                    showMsg("You Don't Have Permission", "error");
                    closeSmallModal();
                } else {
                    showMsg("Username Already Taken", "error");
                }
            });
        } else {
            showMsg("Invaild Username Please Try Again", "error");
        }
    }
    else{
        showMsg("Not Allowed For guest", "error");
    }
}
function getUserIP(username){
    if(canBan == "Y"){
        $.post('/user/getUserIP', {
            username :username,
            token: token
        }, function (data) {
            if(data != null){
                var label = '<div onclick="copyToClipboard(\''+data+'\')" style="width: 132px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"><i style="color: #005cbf;font-size: 14px;padding-right: 5px;" class="fa-solid fa-globe"></i>' + data + '</div>';
                $("#profileIP").html(label);
            }
        });
    }
}
function copyToClipboard(text) {
    var dummy = document.createElement("textarea");
    // to avoid breaking orgain page when copying more words
    // cant copy when adding below this code
    // dummy.style.display = 'none'
    document.body.appendChild(dummy);
    //Be careful if you use texarea. setAttribute('value', value), which works with "input" does not work with "textarea". – Eduard
    dummy.value = text;
    dummy.select();
    document.execCommand("copy");
    document.body.removeChild(dummy);
    showMsg("Successfully Copied To Clipboard","ok");
}
function closeUserProfile(){
    $("#profileDp").attr("src","");
    $("#profile_username").text("");
    $("#profile_tweet").text("");
    $("#profileAbout").text("");
    $("#profileDetails").html("");
    $("#profileCard").hide();
}
function closeUserProfileEdit(){
    $("#profileDpEdit").attr("src","");
    $("#profile_usernameEdit").text("");
    $("#profile_tweetEdit").text("");
    $("#profileAboutEdit").text("");
    $("#profileDetailsEdit").html("");
    $("#profileCardEdit").hide();
}

function profileMenu(){
    $("#profile_drop_menu").toggle();
}

function logout(){
    refreshChat();
}

function kickPage(){
    $.post('/user/logout', {
        token: token
    }, function (data) {
        window.location.href = "/user/kicked";
    });
}

function openFullModal(title,html){
    $("#fullmodal").show();
    $("#fullModalhtml").html(html);
    $("#fullmodaltitle").text(title);
}
function closeFullModal(){
    $("#fullmodal").hide();
    $("#fullModalhtml").html("");
    $("#fullmodaltitle").text("");
}


function openPrivateModal(title,userId,html){
    $("#privatemodal").show();
    $("#privateModalhtml").html(html);
    $("#privatemodaltitle").text(title);
    $("#pmUserId").val(userId);
}
function closePrivateModal(){
    $("#privatemodal").hide();
    $("#privateModalhtml").html("");
    $("#privatemodaltitle").text("");
    $("#pmUserId").val("");
}

function openPrivateChat(id,name){
    closeFullModal();
    closeUserProfile();
    $.post('/user/getPrivateData', {
        fromUserId:id,
        token: token
    }, function (data) {
        if(data != null) {
            var html = renderPrivateChatMsgList(data);
            openPrivateModal(name,id,html);
        }
        else{
            openPrivateModal(name,id,'');
        }

        seenPrivateChat(id).then(function(seen) {
            reCheckUnreadMsgs();
            scrollPmToBottom();
        })
    });
}

function openMessages(){

    $.post('/user/getMsgs', {
        token: token
    }, function (data) {

        if(data == null ){
            var mainHtml = '<div style="padding: 10px;"><div class="msgslist mozscroll shadow-sm colorAlerts" style="border-radius: 10px 10px;text-align: center"><i class="fa-solid fa-envelope" style="font-size: 40px;padding-bottom: 5px;"></i><br><span>No Messages</span></div></div>';
            openFullModal("Messages",mainHtml);
        }
        else{
            if(data.length == 0 ){
                var mainHtml = '<div style="padding: 10px;"><div class="msgslist mozscroll shadow-sm colorAlerts" style="border-radius: 10px 10px;text-align: center"><i class="fa-solid fa-envelope" style="font-size: 40px;padding-bottom: 5px;"></i><br><span>No Messages</span></div></div>';
                openFullModal("Messages",mainHtml);
            }
            else{
                var msgshtml = '<div onclick="clearMyMessages()" style="float: right;padding-bottom: 10px;padding-top: 5px;"><a class="ui red label">Clear</a></div>';
                for(var i = 0 ; i<data.length; i++){

                    var seenhtml = '';
                    if(data[i].seen == "N"){
                        seenhtml = '<a id="'+data[i].fromUserId+'-msgcount" style="font-size: 8px;padding-right: 4px;" class="ui red label msgcounts" >NEW</a>';
                    }

                    var genHtml = '<div class="msgslist mozscroll shadow-sm" style="border-radius: 10px 10px;">\n' +
                        '<div  onclick="openPrivateChat(\'' + data[i].fromUserId + '\' , \'' + data[i].fromUserName + '\');" class="userssort user_lm_box ">\n' +
                        '<div class="user_lm_avatar"><img class="avsex avatar_userlist glob_av" style="border-color: #03add8" src="https://chat-links.com' + data[i].fromDp + '"></div>\n' +
                        '<div class="user_lm_data"><div class="username bellips" style="margin: 0 0 0px !important;color: '+ data[i].fromNameColor+';">'+ data[i].fromUserName +'</div></div>\n' +
                        '<div class="user_lm_icon icrank">'+seenhtml+'</div>\n' +
                        '</div></div>'
                    msgshtml = msgshtml + genHtml;
                }
                var mainHtml = '<div style="padding-left: 15px;padding-right: 15px;">' + msgshtml + '</div>';
                openFullModal("Messages",mainHtml);
            }

        }
    });




}
function openNotifications(){
    openFullModal("Notifications","");
}

function renderChatMsg (message){
    var getProfile = '';
    if(message.userId != null){
        getProfile = 'onclick="openUserProfile(\'' +message.userId+ '\')"';
    }
    //message.content = message.content.replace(currentUserName, '<span class="myNotice">' + currentUserName + '</span>');
    message.content = message.content.replace(currentUserName, '<a class="ui green label" style="padding: 5px !important;">' + currentUserName + '</a>');
    var del = '';
    if (canDelete === "Y") {
        del = '<i class="fa fa-times" onclick="deleteChat(\'' + message.id + '\')" style="font-size: 12px;" ></i>';
    }
    else{
        del = '<i class="fa-solid fa-flag" onclick="reportChat(\'' + message.id + '\')" style="font-size: 12px;" ></i>';
    }
    var textBold = '';
    if(message.bold == "Y"){
        textBold = ';font-weight: bold;'
    }
    if(message.image != undefined && message.image != null && message.image != ''){
        message.content =  message.content + ' <img onclick="viewImage(\'' + message.image + '\')" style="height:60px;border-radius: 8px;padding-left: 5px;" src="'+message.image+'" />';
    }
    var chatMsg = ' <li id="' + message.id + '" class="ch_logs left clearfix"><span class="float-left" >\n' +
        '                            <img '+getProfile+' style="height: 35px;width: 35px" src="https://chat-links.com' + message.dp + '" alt="User Avatar" class="rounded-circle" />\n' +
        '                        </span>\n' +
        '                    <div class="chat-body clearfix">\n' +
        '                        <div class="header">\n' +
        '                            <strong class="usernamechat" style="color: '+message.nameColor+'">' + message.userName + '</strong> <small style="color: lightgray;padding: 5px;" class="float-right">\n' +
        '                            ' + del + '</small><span id="' + message.id + '_claim" class="hideit claimit float-right"></span>\n' +
        '                        </div>\n' +
        '                        <p style="color: '+message.textColor+' '+textBold+'" >' + message.content + '</p>' +
        '                    </div>\n' +
        '                </li>'

    $("#chat_warp").append(chatMsg);
    logsControl();
    scrollToBottom();
}
function getViewImage(element){
    viewImage(element.src);
}
function viewImage(imageData){
    var html = '<form onsubmit="return false" class="ui form">\n' +
        '  <h4 class="ui dividing header">Image</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<img src="'+imageData+'" width="100%" />'
    '</form>';
    smallModal(html);
}
function renderJoinMessage(message){
    var getProfile = '';
    if(message.user.userName != currentUserName){
        getProfile = 'onclick="openUserProfile(\'' +message.user.userId+ '\')"';
    }
    var del = '';
    if (canDelete === "Y") {
        del = '<i class="fa fa-times" onclick="deleteChat(\'' + message.id + '\')" style="font-size: 12px;" ></i>';
    }
    var html = '<li id="' + message.id + '" class="ch_logs chatnotify" style="text-align: center"><i class="fa-regular fa-bell bellicon"></i><span class="notice" '+getProfile+' > '+ escapeHTML(message.user.userName)+' has joined! </span><small style="color: lightgray;padding-right: 5px;" class="float-right ">' + del + '</small></li>';
    $("#chat_warp").append(html);
    logsControl();
    scrollToBottom();
}
function renderMyJoinMessage(userName){
    var html = '<li  class="ch_logs chatnotify" style="text-align: center"><i class="fa-regular fa-bell bellicon"></i><span class="notice" > '+ userName+' has joined! </span><small style="color: lightgray;padding-right: 5px;" class="float-right "></small></li>';
    $("#chat_warp").append(html);
    logsControl();
    scrollToBottom();
}
function mainChatNotification(msg){
    var html = '<li  class="ch_logs chatnotify" style="text-align: center"><i class="fa-regular fa-bell bellicon"></i><span class="notice" > '+msg+'</span><small style="color: lightgray;padding-right: 5px;" class="float-right "></small></li>';
    $("#chat_warp").append(html);
    logsControl();
    scrollToBottom();
}
function renderChatMsgSpam (message){
    if (message != null && message.content != null && message.content != "") {
        var getProfile = '';
        if(message.userName != currentUserName){
            getProfile = 'onclick="openUserProfile(\'' +message.userName+ '\')"';
        }
        message.content = message.content.replace(currentUserName, '<span class="myNotice">' + currentUserName + '</span>');
        var del = '';
        if (canDelete === "Y") {
            del = '<i class="fa fa-times" onclick="deleteChat(\'' + message.id + '\')" style="font-size: 12px;" ></i>';
        }
        else{
            del = '<i class="fa-solid fa-flag" onclick="reportChat(\'' + message.id + '\')" style="font-size: 12px;" ></i>';
        }
        var textBold = '';
        if(message.bold == "Y"){
            textBold = ';font-weight: bold;'
        }
        var chatMsg = ' <li id="' + message.id + '" class="ch_logs left clearfix"><span class="float-left" >\n' +
            '                            <img '+getProfile+' style="height: 35px;width: 35px" src="https://chat-links.com' + message.dp + '" alt="User Avatar" class="rounded-circle" />\n' +
            '                        </span>\n' +
            '                    <div class="chat-body clearfix">\n' +
            '                        <div class="header">\n' +
            '                            <strong class="usernamechat" style="color: '+message.nameColor+'">' + message.userName + ' <i style="color: red;padding-left: 3px;" class="fa-solid fa-ban"></i></strong> <small style="color: lightgray;padding: 5px;" class="float-right ">\n' +
            '                            ' + del + '</small>\n' +
            '                        </div>\n' +
            '                        <p style="color: '+message.textColor+' '+textBold+'" >' + message.content + '</p>' +
            '                    </div>\n' +
            '                </li>'

        $("#chat_warp").append(chatMsg);
        scrollToBottom();
    }
}

function renderPrivateChatMsg (message){
    if ((message != null && message.content != null && message.content != "") || (message.image != null && message.image != '')) {
        var targetUser = $("#pmUserId").val();
        if(message.fromUserId == targetUser){
            seenPrivateChat(message.fromUserId);
            var chatMsg = makePrivateChatMsg(message);
            $("#privateModalhtml").append(chatMsg);
            scrollPmToBottom();

        }
    }
}
function renderSendPrivateChatMsg (message){
    if(message.image != null && message.image != ''){
        message.content =message.content + ' <img onclick="viewImage(\'' + message.image + '\')" style="height:60px;border-radius: 8px;padding-left: 5px;" src="'+message.image+'" />';
    }
    var targetUser = $("#pmUserId").val();
    var chatMsg = makePrivateChatMsg(message);
    $("#privateModalhtml").append(chatMsg);
}
function composePrivateMsg (chatMessage){
    chatMessage.fromUserId = parseInt(currentUserId);
    chatMessage.fromUserName = currentUserName;
    chatMessage.fromDp = currentUserDp;
    chatMessage.fromNameColor = currentNameColor;
    chatMessage.fromTextColor = currentTextColor;
    chatMessage.fromTextFont = currentUserFont;
    chatMessage.chatRoomId =  parseInt(chatRoomId);
    chatMessage.seen = "Y";
    chatMessage.toUserId = parseInt(chatMessage.toUserId);
    chatMessage.msgType = "CHAT";
    chatMessage.msgDate = new Date();

    return chatMessage;
}
function renderPrivateChatMsgList (message){

    if (message != null) {
        var chatMsgs = '';
        for(var i = 0; i< message.length; i++){
            chatMsgs = chatMsgs + makePrivateChatMsg(message[i]);
        }
        return chatMsgs;
    }
}
function makePrivateChatMsg (message){
    message.content =  processEmojis(message.content);
    var getProfile = '';
    var imageSrc = '';

    if(message.image != null && message.image != ''){
        imageSrc = '<p style="color: #005a7d;"> <img onclick="viewImage(\''+message.image+'\')" style="height:60px;border-radius: 8px;padding-left: 5px;" src="'+message.image+'"></p> ';
    }


    if(message.fromUserName != currentUserName){
        getProfile = 'onclick="openUserProfile(\'' +message.fromUserName+ '\')"';
    }
    var chatMsg = '     <li class="pm_logs left clearfix"><span class="float-left" >\n' +
        '                            <img '+getProfile+' style="height: 35px;width: 35px" src="https://chat-links.com' + message.fromDp + '" alt="User Avatar" class="rounded-circle" />\n' +
        '                        </span>\n' +
        '                    <div class="chat-body clearfix">\n' +
        '                        <div class="header">\n' +
        '                            <strong class="usernamechat" style="color: '+message.fromNameColor+';" >' + message.fromUserName + '</strong>\n' +
        '                        </div>\n' +
        '                        <p style="color: '+message.fromTextColor+';" >' + imageSrc +  message.content + '</p>' +
        '                    </div>\n' +
        '                </li>';

    return chatMsg;
}

function scrollToBottom() {
    try {
        var t = $('#chat_warp');
        if ($("#scroll_Policy").val() === "1") {
            t.scrollTop(t.prop("scrollHeight"));
        }

    }
    catch (e) {
    }
}

function getUserList(){
    $.post('/user/getUserList', {
        token: token
    }, function (data) {
        loadUserList(data);
    });
}

function loadUserList(data){
    if(data != null && data.length != 0){
        for(var i = 0; i < data.length; i++){
            $('#user_'+data[i].userId).remove();
            renderUser(data[i]);
        }
    }

}
function removeUserList(id){
    $("#user_"+id).remove();
    // delete userRepo[id];
}
function removeUserRJList(id){
    $("#user_"+id).remove();
    if($("#RJ").html() != ""){
        $("#currentrj").show();
    }
    else{
        $("#currentrj").hide();
    }
    // delete userRepo[id];
}
function addUserList(data){
    if(data != null ){
        $('#user_'+data.userId).remove();
        renderUser(data);
    }

}
function openUserOptions(id){
    if(id !== currentUserName){
        $("#"+id+"-option").slideToggle( "fast" );
    }

}
function renderUserModrenStyle(data){
    var tweet = '';
    if (data.tweet != '' && data.tweet != null) {
        tweet = '<div class="bustate bellips" style="font-size: 10px; margin: 0 0 0px; !important;color: '+currentTextColor+';">' + data.tweet + '</div>';
    }
    userRepo[data.userId] = data;

    var nameColor = '';
    if(data.nameColor != "" && data.nameColor != null){
        nameColor = 'style="color: '+data.nameColor+'"';
    }

    var html = '<div onclick="openUserProfile(\'' +data.userId+ '\')" id="user_' + data.userId + '"  class="borderuserlist" >' +
        '<div    class="userssort list_element list_item user_lm_box">\n' +
        '<div class="user_lm_avatar" ><img class="avsex avatar_userlist glob_av" style="border-color: #03add8" src="https://chat-links.com' + data.dp + '"></div>\n' +
        '<div class="user_lm_data"><div '+nameColor+' class="username bellips marginusername" >' + data.userName + '</div>' + tweet + '</div>\n' +
        '<div class="user_lm_icon icrank"><img style="height: 25px !important;" src="' + data.rankIcon + '"/></div>\n' +
        '</div>';
    if(data.status == 'R'){
        $("#RJ:last").append(html);

    }
    else{
        $("#" + data.rankCode+":last").append(html);
    }
    if($("#RJ").html() != ""){
        $("#currentrj").show();
    }
    else{
        $("#currentrj").hide();
    }
}
function renderUserIrcStyle(data){
    var tweet = '';
    if (data.tweet != '' && data.tweet != null) {
        tweet = '<div class="bustate bellips" style="font-size: 10px; margin: 0 0 0px; !important;color: '+currentTextColor+';">' + data.tweet + '</div>';
    }
    userRepo[data.userId] = data;

    var nameColor = '';
    if(data.nameColor != "" && data.nameColor != null){
        nameColor = 'style="color: '+data.nameColor+'"';
    }

    var html = '<div onclick="openUserProfile(\'' +data.userId+ '\')" id="user_' + data.userId + '"  class="borderuserlist" >' +
        '<div    class="userssort list_element list_item user_lm_box">\n' +
        '<div class="user_lm_avatar" ><img class="avsex avatar_userlist glob_av" style="border-color: #03add8" src="https://chat-links.com' + data.dp + '"></div>\n' +
        '<div class="user_lm_data"><h1 '+nameColor+' class="username bellips marginusername" >' + data.userName + '</h1></div>\n' +
        '<div class="user_lm_icon icrank"><img style="height: 20px;width: 20px;padding-bottom: 2px;" class="icrankimg" src="' + data.rankIcon + '"/></div>\n' +
        '</div>';
    if(data.status == 'R'){
        $("#RJ:last").append(html);

    }
    else{
        $("#" + data.rankCode+":last").append(html);
    }
    if($("#RJ").html() != ""){
        $("#currentrj").show();
    }
    else{
        $("#currentrj").hide();
    }
}
function renderUser(data){
    if(currentDesign == "I"){
        renderUserIrcStyle(data);
    }
    else{
        renderUserModrenStyle(data);
    }

}

function banUserDialog(userId, userName){

    if($("#banStatus").is(':checked')){
        unBanUser(userId,userName);
    }
    else{
        banUser(userId,userName);
    }

}

function banUser(userId, userName){
    if(userId != ""){
        $.post('/user/banUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                $(".banCheckbox").prop("checked" ,true);
                showMsg(userName+" Banned","ok");
            }
            else{
                showMsg("Unable to Ban","error");
            }
        });
    }
}
function unBanUser(userId, userName){
    if(userId != ""){
        $.post('/user/unBanUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                $(".banCheckbox").prop("checked" ,false);
                showMsg(userName+" UNBANNED","ok");
            }
            else{
                showMsg("Unable to UNBANNED","error");
            }
        });
    }
}

function muteUserDialog(userId, userName){

    if($("#muteStatus").is(':checked')){
        unMuteUser(userId,userName);
    }
    else{
        muteUser(userId,userName);
    }

}
function muteUser(userId, userName){
    if(userId != ""){
        $.post('/user/muteUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                $(".muteCheckbox").prop("checked" ,true);
                showMsg(userName+" Muted","ok");
            }
            else{
                showMsg("Unable to Mute","error");
            }
        });
    }
}
function unMuteUser(userId, userName){
    if(userId != ""){
        $.post('/user/unMuteUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                $(".muteCheckbox").prop("checked" ,false);
                showMsg(userName+" UNMUTED","ok");
            }
            else{
                showMsg("Unable to UNMUTED","error");
            }
        });
    }
}
function kickUserDialog(userId, userName){
    $.confirm({
        title: 'Kick '+userName ,
        theme: 'supervan',
        closeIcon: true,
        animation: 'scale',
        type: 'orange',
        content: 'Are you sure want to Kick?',
        buttons: {
            Yes: function () {
                kickUser(userId,userName);
                $(".kickCheckbox").prop("checked" ,true);
            },
            No: function () {

            }
        }
    });

}
function spamUserDialog(userId, userName){
    if($("#spamStatus").is(':checked')){
        unSpamUser(userId,userName);
    }
    else{
        spamUser(userId,userName);
    }


}
function kickUser(userId, userName){
    if(userId != ""){
        $.post('/user/kickUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                showMsg(userName+" Kicked","ok");
            }
            else{
                showMsg("Unable to kick","error");
            }
        });
    }
}
function spamUser(userId, userName){
    if(userId != ""){
        $.post('/user/spamUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                $(".spamCheckbox").prop("checked" ,true);
                showMsg(userName+" Set Spammer","ok");
            }
            else{
                showMsg("Unable Set Spammer","error");
            }
        });
    }
}
function unSpamUser(userId, userName){
    if(userId != ""){
        $.post('/user/unSpamUser', {
            userId:userId,
            token: token
        }, function (data) {
            if(data === "Y"){
                $(".spamCheckbox").prop("checked" ,false);
                showMsg(userName+" Set Not Spammer","ok");
            }
            else{
                showMsg("Unable Set Not Spammer","error");
            }
        });
    }
}

function escapeHTML(str) {
    var p = document.createElement("p");
    p.appendChild(document.createTextNode(str));
    return p.innerHTML;
}
function toogleRadio(){
    if(radioSwitch){
        stopRadio();
        $("#radioIcon").removeClass("fa-volume-high");
        $("#radioIcon").addClass("fa-radio");
        radioSwitch = false;
    }
    else{
        if(radioLink != ''){
            playRadio();
            $("#radioIcon").removeClass("fa-radio");
            $("#radioIcon").addClass("fa-volume-high");
            radioSwitch = true;
        }
        else{
            showMsg("Radio Link is Not Set","error");
        }

    }

}

function playRadio(){
    var radio = document.querySelector("#radio_player");
    radio.volume = 0.9;
    radio.setAttribute("src", radioLink);
    radio.play();
}

function stopRadio() {
    var radio = document.querySelector("#radio_player");
    radio.pause();
    radio.currentTime = 0;
    radio.setAttribute("src", "");
}

logsControl = function () {
    try {
        var countLog = $('.ch_logs').length;
        if ($("#scroll_Policy").val() == "1") {
            var countLimit = 80;
            var countDiff = countLog - countLimit;
            if (countDiff > 0 && countDiff % 2 === 0) {
                $('#chat_warp').find('.ch_logs:lt(' + countDiff + ')').remove();
            }
        }
        if (countLog > 200) {
            var countLimit = 80;
            var countDiff = countLog - countLimit;
            if (countDiff > 0 && countDiff % 2 === 0) {
                $('#chat_warp').find('.ch_logs:lt(' + countDiff + ')').remove();
            }
            $("#scroll_Policy").val("1");

        }
    }
    catch (e) {
        console.log(e);
    }
}
pmLogsControl = function () {

    var countLog = $('.pm_logs').length;
    var countLimit = 60;
    var countDiff = countLog - countLimit;
    if (countDiff > 0 && countDiff % 2 === 0) {
        $('#privateModalhtml').find('.pm_logs:lt(' + countDiff + ')').remove();
    }
}
function processEmojis(str){
    var result = str.split(" ").filter(function(n) {
        if(/:/.test(n)) return n;
    });
    for(var i=0; i< result.length; i++){
        if(result[i] in emo){
            str = str.replace(result[i],emo[result[i]]);
        }
        if(result[i] in emo2) {
            str = str.replace(result[i], emo2[result[i]]);
        }
        if(result[i] in emo3) {
            str = str.replace(result[i], emo3[result[i]]);
        }
    }
    return str;
}

function openEmojis(type){
    if(type == 3){
        var emojis = '';

        for(var key in emo3){
            var emo =emo3[key];
            emo = emo.replaceAll("emo_chat","emoticon appendMainEmo");
            emojis = emojis + emo;
        }
        $("#emo_content").html(emojis);
        $("#writeOptionModal").hide();
        $("#smily_box").show();

    }
    if(type == 2){
        var emojis = '';

        for(var key in emo2){
            var emo =emo2[key];
            emo = emo.replaceAll("emo_chat","emoticon appendMainEmo");
            emojis = emojis + emo;
        }
        $("#emo_content").html(emojis);
        $("#writeOptionModal").hide();
        $("#smily_box").show();

    }
}
function openEmojisPrivate(type){
    if(type == 3){
        var emojis = '';

        for(var key in emo3){
            var emo =emo3[key];
            emo = emo.replaceAll("emo_chat","emoticon appendMainEmoPrivate");
            emojis = emojis + emo;
        }
        $("#emo_contentPrivate").html(emojis);
        $("#writeOptionModalPrivate").hide();
        $("#smily_boxPrivate").show();

    }
    if(type == 2){
        var emojis = '';

        for(var key in emo2){
            var emo =emo2[key];
            emo = emo.replaceAll("emo_chat","emoticon appendMainEmoPrivate");
            emojis = emojis + emo;
        }
        $("#emo_contentPrivate").html(emojis);
        $("#writeOptionModalPrivate").hide();
        $("#smily_boxPrivate").show();

    }
}
function refreshChat(){
    discountChat();
    $( "#refresh" ).submit();
}
function logoutChat(){
    discountChat();
    $("#mytoken").val("logout");
    $( "#refresh" ).submit();
}

function chatHistory(){
    $.post('/user/getChatHistory', {
        token: token
    }, function (data) {
        if(data != null && data.length != 0){
             data = data.reverse();
            for(var i = 0; i < data.length; i++ ){
                data[i].id = data[i].chatId;
                if(data[i].content != null && data[i].content != ''){
                    data[i].content = processEmojis(data[i].content);
                }
                renderChatMsg(data[i]);
            }
        }
        renderMyJoinMessage(currentUserName);
        connect();
    });
}

function clearAllChat(){
    if(canDelete == "Y"){
        $.post('/user/clearAllChat', {
            token: token
        }, function (data) {
        });
    }
}
function gnupolicy(){
    var html = '<div  class="ui form">\n' +
        '  <h4 class="ui dividing header">Information</h4><i onclick="closeSmallModal()" class="fas fa-times-circle clIcons" style="position: absolute;font-size: 24px;top: -11px;right: -11px;" aria-hidden="true"></i> \n' +
        '<div>' +
        '<div style="text-align: center"><i class="fa-solid fa-triangle-exclamation clIcons" style="font-size: 70px;"></i><br>' +
        '<h3>Do Not Spam / Abuse</h3>' +
        '</div>' +
        '<div style="text-align: left;padding-top: 10px;">' +
        '<span style="text-align: left">To improve our chat room app we use UGC Policy to control abusive chat.</span>' +
        '<br><br><span style="text-align: left;">Our automatic abuse detection system can mark your message as spam and block or mute your account immediately.</span>' +
        '</div>' +
        '' +
        '</div>';
    smallModal(html);
}