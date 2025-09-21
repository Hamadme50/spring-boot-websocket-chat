<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="icon" type="image/x-icon" href="/content/default/chaticon.png">
    <link  href="/content/lib/app/irc.css?v=${cache}" type="text/css" rel="stylesheet" >
    <link  href="/content/lib/app/theme.css?v=${cache}" type="text/css" rel="stylesheet" >
    <!-- Bootstrap CSS -->
    <link href="/content/lib/other/bootstrap/bootstrap.min.css" type="text/css" rel="stylesheet" >
    <link href="/content/lib/other/Semantic/semantic.min.css?v=${cache}" type="text/css" rel="stylesheet" />

    <title>Chatroom</title>
    <style>
        #chat_header{
            background-color: ${chatroom.theme} !important;
        }
        #mainInpurArea{
            border-top: thin solid ${chatroom.theme} !important;
            border-bottom: thin solid ${chatroom.theme} !important;
        }
        #priavteInputArea{
            border-top: thin solid ${chatroom.theme} !important;
            border-bottom: thin solid ${chatroom.theme} !important;
        }
        .themeBorderBottom{
            border-bottom: thin solid ${chatroom.theme} !important;
        }
        .themeAllBorderBottom{
            border: thin solid ${chatroom.theme} !important;
        }
        .chatRoomTheme{
            background-color: ${chatroom.theme} !important;
        }
        .chatRoomTheme{
            background-color: linear-gradient(white auto,${chatroom.theme}); !important;
        }
    </style>
</head>
<body>
<div id="fullmodal" class="shadow-lg modalWidth themeBorderBottom" style="display:none;z-index: 5;position: fixed; width: 400px;background-color: white;right: 0;top: 0px;height: 100%">
    <div id="fullModalHeader" class="chatRoomTheme" style="width: 100%;height: 40px;filter: brightness(1.2)">
        <b id="fullmodaltitle" style="color: white;float: left;padding-top: 10px;padding-left: 17px;font-size: 18px;"></b>
        <div style="float: right;padding-top: 6px;" onclick="closeFullModal()">
            <i class="fas fa-times-circle" style="font-size: 24px;color: white;vertical-align: middle;padding-right: 10px;"></i>
        </div>
        <div style="clear: both"></div>
    </div>
    <div id="fullModalhtml" style="padding: 2px;overflow: auto;height: 100%">

    </div>
</div>

<div id="privatemodal" class="shadow-lg modalWidth" style="display:none;z-index: 20;position: fixed; width: 400px;background-color: white;right: 0;bottom: 0px;height: auto">
    <div id="privateModalHeader" class="chatRoomTheme" style="width: 100%;height: 40px;filter: brightness(1.2)">
        <b id="privatemodaltitle" style="color: white;float: left;padding-top: 10px;padding-left: 17px;font-size: 18px;"></b>
        <div style="float: right;padding-top: 6px;" onclick="closePrivateModal()">
            <i class="fas fa-times-circle" style="font-size: 24px;color: white;vertical-align: middle;padding-right: 10px;"></i>
        </div>
        <div style="clear: both"></div>
    </div>
    <div id="privateModalhtml" class="chat mozscroll" style="padding: 2px;overflow: auto;">
    </div>
    <div id="smily_boxPrivate" class="border shadow-sm">
        <div style="height: 215px;overflow: hidden;overflow-y: auto; padding: 5px;" id="emo_contentPrivate"> <p style="text-align: center;margin-top: 50px;">Loading...</p></div>
    </div>
    <div class="shadow-sm writeOption" id="writeOptionModalPrivate" >
        <div style="padding: 13px;">
            <div style="float: left;" onclick="writeMenuDialog()">
                <i class="fa-solid fa-paintbrush clIcons" style="font-size: 25px;"></i>
            </div>
            <div style="float: left;padding-left: 20px;" onclick="openEmojisPrivate('3')">
                <i class="fa-solid fa-face-smile clIcons" style="font-size: 25px;"></i>
            </div>
            <div style="float: left;padding-left: 20px;" onclick="openEmojisPrivate('2')">
                <i class="fa-solid fa-star clIcons" style="font-size: 25px;"></i>
            </div>

            <div style="float: left;padding-left: 20px;" >
                <i class="fa-regular fa-image clIcons" style="font-size: 25px;" onclick="processChatImagePrivate()" ></i>
                <input id="uploadChatImagePrivate" style="display: none" type="file" onchange="loadChatImageFilePrivate();" />
            </div>

        </div>

    </div>
    <div id="privateModalFooter" style="height: 40px;">
        <div id="priavteInputArea" style="height: 40px;width: 100%;" >
            <table style="width:100%;text-align: center;">
                <tr>
                    <td class="clIconBrush"  onclick="openWriteOptionPrivate();" style="width: 30px;padding-top: 7px;">
                        <i class="fa-solid fa-paperclip" style="font-size: 24px;padding-left: 10px; "></i>
                    </td>
                    <td style="padding-left: 8px;padding-top: 3px;">
                        <input id="pm_chat_input" width="100%" class="mainChatInput"  style="text-indent: 13px;font-size: 14px;" spellcheck="true" autocomplete="off" maxlength="600" placeholder="Type Something ..." ${user.status == 'M' ? 'disabled' : ''} oncontextmenu="return false" oncopy="return false" ondrag="return false" ondrop="return false" onpaste="return false" >
                    </td>
                    <td style="padding-top: 7px;width: 30px;" class="clIconSend" onclick="sendPrivateMessage();">
                        <i id="send_pm_msg"  class="fas fa-location-arrow"style="font-size: 26px;padding-right: 10px;padding-left: 10px;"></i>
                        <input type="hidden" id="pmUserId" value="" />
                    </td>
                </tr>
            </table>

        </div>
    </div>

</div>
<div id="profileCard" class="profileModal">
    <div  style="z-index: 15; left: 0;right: 0;margin: auto;background-color: white" class="profileWidth shadow-lg mozscroll">
        <div class="container">
            <div class="row" style="background-color: rgba(0,0,0,0.2);">
                <div class="static" style="width: 100%;">
                    <div style="float: right;padding-top: 10px;" onclick="closeUserProfile()">
                        <i class="fas fa-times-circle" style="font-size: 24px;color: white;vertical-align: middle;padding-right: 10px;" aria-hidden="true"></i>
                    </div>
                    <div style="float: right;padding-top: 10px;padding-right: 10px;" id="profileMessage" >

                    </div>
                    <div class="profile-card chatRoomTheme">
                        <img id="profileDp" onclick="getViewImage(this)" src="" alt="user" class="profile-photo">
                        <h5 class="text-white profilrusername"><a id="profile_username"></a></h5>
                        <div class="bustate bellips profiletweet" style="margin-top: 5px;" id="profile_tweet" ></div>
                    </div><!--profile card ends-->
                    <div class="profileBackgroud" >
                        <div class="col-sm-9 col-xl-12 col-xxl-9" style="padding-top: 10px;">
                            <strong>About me</strong>
                            <p id="profileAbout"></p>
                        </div>
                        <div style="padding-left: 12px;width: 100%;padding-top: 5px;">
                            <table style="width: 100%;margin-bottom: 9px !important;">
                                <tr style="width: 100%;">
                                    <td style="width: 17%;">
                                        <img id="profileRankIcon" src="" height="40px;">
                                    </td>
                                    <td style="width:83%">
                                        <h5 id="profileRankName"></h5>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="container" style="padding-bottom: 10px;">
                            <table class="table table-sm mt-2 mb-4" style="margin-bottom: 0px !important;">
                                <tbody id="profileDetails">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</div>

<div id="smallModal" class="smallModal">
    <div id="smallModalHtml" style="z-index: 22; left: 0;right: 0;margin: auto;background-color: white;padding: 20px;border-radius: 10px;border-color: transparent" class="profileWidth shadow-lg mozscroll">

    </div>
</div>

<div id="profileCardEdit" class="profileModal">
    <div  style="z-index: 15; left: 0;right: 0;margin: auto;background-color: white" class="profileWidth shadow-lg mozscroll">
        <div class="container">
            <div class="row" style="background-color: rgba(0,0,0,0.2);">
                <div class="static" style="width: 100%;">
                    <div style="float: right;padding-top: 10px;" onclick="closeUserProfileEdit()">
                        <i class="fas fa-times-circle" style="font-size: 24px;color: white;vertical-align: middle;padding-right: 10px;" aria-hidden="true"></i>
                    </div>
                    <div class="profile-card chatRoomTheme">
                        <i class="fa-solid fa-camera" onclick="changeMyDp()" style="position: absolute;margin-top: 35px; margin-left: 23px;font-size: 25px;z-index: 16"></i>
                        <img id="profileDpEdit"  onclick="getViewImage(this)"  src="" alt="user" class="profile-photo" onclick="changeMyDp()">
                        <input id="upload-Image" style="display: none" type="file" onchange="loadImageFile();" />
                        <h5  class="text-white profilrusername"><a onclick="changeUserNameDialog()" id="profile_usernameEdit"></a>  <i onclick="changeUserNameDialog()" class="fa-solid fa-pen-to-square" style="padding-left: 8px;"></i>   <i onclick="changeUserNameColorDialog()" class="fa-solid fa-palette" style="padding-left: 8px;"></i></h5>
                        <div class="bustate bellips profiletweet" style="margin-top: 5px;" ><span id="mytweet">${user.tweet}</span> <i onclick="changeTweetDialog()" class="fa-solid fa-pen-to-square" style="padding-left: 8px;"></i></div>
                    </div><!--profile card ends-->
                    <div class="profileBackgroud" >
                        <div class="col-sm-9 col-xl-12 col-xxl-9" style="padding-top: 10px;">
                            <strong>About me</strong> <i onclick="changeAboutDialog()" class="fa-solid fa-pen-to-square" style="padding-left: 10px;"></i>
                            <p id="profileAboutEdit"></p>
                        </div>
                        <div style="padding-left: 12px;width: 100%;padding-top: 5px;">
                            <table style="width: 100%;margin-bottom: 9px !important;">
                                <tr style="width: 100%;">
                                    <td style="width: 17%;">
                                        <img id="profileRankIconEdit" src="" height="40px;">
                                    </td>
                                    <td style="width:83%">
                                        <h5 id="profileRankNameEdit"></h5>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="container" style="padding-bottom: 10px;">
                            <table class="table table-sm mt-2 mb-4" style="margin-bottom: 0px !important;">
                                <tbody id="profileDetailsEdit">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</div>

<div id="chat_header">

    <div onclick="refreshChat()" style="padding: 6px; width: 40px;float: right;margin-right: 10px;" >
        <i for="submit-form" class="fa-solid fa-rotate-right iconscolor"  style="font-size: 24px;"></i>
    </div>

    <div onclick="toogleRadio()" style="padding: 6px; width: 40px;float: right;margin-right: 10px;" >
        <i class="fa-solid fa-radio iconscolor" id="radioIcon"  style="font-size: 24px;"></i>
        <audio id="radio_player">
        </audio>
    </div>

    <div onclick="openMessages();" style="padding-top: 8px; width: 40px;float: right;margin-right: 10px;" >
        <a id="messageCount" class="ui red circular label" style="position: absolute;font-size: 9px;top: 4px;margin-left: 13px;display: none"></a>
        <i class="fa-solid fa-message iconscolor" style="font-size: 24px;"></i>
    </div>


    <div style="padding-top: 1.5px;; width: auto;left: 39px;position: absolute">
        <div class="ui horizontal label white" style="margin-left: 9px;margin-top: 7px;">${user.userName}</div>
    </div>
    <div style="padding: 2px; width: 40px;left: 5px;position: absolute" onclick="profileMenu()" >
        <img id="myDp" class="roundimage" src="${user.dp}">
    </div>

    <ul id="profile_drop_menu" class="list-group shadow-sm">
        <li onclick="openUserProfileEdit('${user.id}')" class="list-group-item d-flex justify-content-between align-items-center">
            Profile
            <i class="fa-solid fa-user-pen clIcons"></i>
        </li>
        <c:if test="${user.rank.code == 'OWNER'}">
            <li onclick="manageUsers()" class="list-group-item d-flex justify-content-between align-items-center">
                Manage
                <i class="fa-solid fa-user-group clIcons"></i>
            </li>
        </c:if>
        <c:if test="${user.rank.ban == 'Y'}">
            <li onclick="ipBanned()" class="list-group-item d-flex justify-content-between align-items-center">
                IP Banned
                <i class="fa-solid fa-globe clIcons"></i>
            </li>
        </c:if>
        <c:if test="${user.rank.code == 'OWNER'}">
            <li onclick="window.open('https://www.chatlinks.net/panel/signin', '_blank');" class="list-group-item d-flex justify-content-between align-items-center">
                Chat Panel
                <i class="fa-solid fa-gauge-high clIcons"></i>
            </li>
        </c:if>
        <c:if test="${user.rank.code != 'GUEST'}">
            <li onclick="changePassword()" class="list-group-item d-flex justify-content-between align-items-center">
                Change Password
                <i class="fa-solid fa-key clIcons"></i>
            </li>
        </c:if>
        <li onclick="logoutChat();" class="list-group-item d-flex justify-content-between align-items-center">
            Logout
            <i class="fa-solid fa-right-from-bracket clIcons"></i>
        </li>
    </ul>

</div>
<div id="mainchat" class="mainchat" style="float: left;z-index:50;height: calc(100% - 80px) !important;width: calc(100% - 200px);overflow: hidden">
    <input type="hidden" id="scroll_Policy" value="1">
    <ul id="chat_warp" class="chat mozscroll" style="height: calc(100%);overflow: auto;overflow-x: hidden;-webkit-overflow-scrolling: touch;width: 100%  !important;">


    </ul>
</div>
<div id="chat_users" class="border-left rightside" style="float: left;z-index:1;height: calc(100% - 80px) !important;width: 200px;overflow: hidden">
    <div id="user_list" style="overflow: hidden;overflow-y: auto;position: relative;height: 100%;background-color: white;-webkit-overflow-scrolling: touch;" class="mozscroll " >
        <div class="user_count"  id="currentrj" style="display: none">
            <div class="bcell">
                Current RJ/DJ
            </div>
        </div>
        <div id="RJ"></div>
        <div class="user_count">
            <div class="bcell">
                Online
            </div>
        </div>
        <div id="OWNER"></div>
        <div id="ADMIN"></div>
        <div id="CUSTOM"></div>
        <div id="MOD"></div>
        <div id="STAR"></div>
        <div id="VIP"></div>
        <div id="MEMBER"></div>
        <div id="GUEST"></div>

    </div>
</div>
<div style="clear: both"></div>
<div id="smily_box" class="border shadow-sm">
    <div style="height: 215px;overflow: hidden;overflow-y: auto; padding: 5px;" id="emo_content"> <p style="text-align: center;margin-top: 50px;">Loading...</p></div>
</div>
<div class="shadow-sm writeOption" id="writeOptionModal" >
    <div style="padding: 13px;">
        <div style="float: left;" onclick="writeMenuDialog()">
            <i class="fa-solid fa-paintbrush clIcons" style="font-size: 25px;"></i>
        </div>
        <div style="float: left;padding-left: 20px;" onclick="openEmojis('3')">
            <i class="fa-solid fa-face-smile clIcons" style="font-size: 25px;"></i>
        </div>
        <div style="float: left;padding-left: 20px;" onclick="openEmojis('2')">
            <i class="fa-solid fa-star clIcons" style="font-size: 25px;"></i>
        </div>

        <div style="float: left;padding-left: 20px;" >
            <i class="fa-regular fa-image clIcons" style="font-size: 25px;" onclick="processChatImage()" ></i>
            <input id="uploadChatImage" style="display: none" type="file" onchange="loadChatImageFile();" />
        </div>

    </div>

</div>
<div id="chat_footer" style="height: 40px;">
    <div id="mainInpurArea" style="height: 40px;width: 100%;" >
        <table style="width:100%;text-align: center;">
            <tr >
                <td class="clIconBrush"  onclick="openWriteOption();" style="width: 30px;padding-top: 6px;">
                    <i class="fa-solid fa-paperclip"style="font-size: 24px;padding-left: 10px; "></i>
                </td>
                <td style="padding-left: 8px;padding-top: 3px;">
                    <input id="main_chat_input" width="100%" class="mainChatInput"  style="text-indent: 13px;font-size: 14px;" spellcheck="true" autocomplete="off" maxlength="600" placeholder="Type Something ..." ${user.status == 'M' ? 'disabled' : ''} ${user.rank.code == 'GUEST' || user.rank.code == 'MEMBER' ? 'oncontextmenu="return false" oncopy="return false" ondrag="return false" ondrop="return false" onpaste="return false"' : ''} >
                </td>
                <td style="padding-top: 7px;width: 30px;" class="clIconSend">
                    <i id="send_main_msg" onclick="sendMessage();" class="fas fa-location-arrow"style="font-size: 26px;padding-right: 10px;padding-left: 10px;"></i>
                </td>
            </tr>
        </table>

    </div>
</div>

<div id="alertmsg" class="shadow animated fadeInDown" style="display:none;z-index: 3000;text-align: center;position: fixed; width: 100%;height:40px;background-color: white;top: 0px;">
    <input type="hidden" value="N" id="currentAlert">
    <div id="alertmsg_content" style="font-size: 20px;color: white;font-weight: 900px;padding-top: 10px;"></div>
</div>

<script src="/content/lib/other/jquery/jquery.min.js?v=${cache}"></script>
<script src="https://kit.fontawesome.com/c7ce8a048e.js" crossorigin="anonymous"></script>
<script src="/content/lib/socket/socketjs.min.js?v=${cache}"></script>
<script src="/content/lib/socket/stomp.min.js?v=${cache}"></script>
<script async src="/content/lib/other/Semantic/semantic.min.js?v=${cache}"></script>
<link href="/content/lib/other/jqueryconfrim/jquery-confirm.min.css?v=${cache}" type="text/css" rel="stylesheet" >
<script src="/content/lib/other/jqueryconfrim/jquery-confirm.min.js?v=${cache}"></script>
<form id="refresh" action="${user.chatRoomId}" method="post" ><input id="mytoken" type="hidden" name="token" value="${token}"/></form>
<script>
    var token = '${token}';
    const currentUserName = '${user.userName}';
    const currentUserId = '${user.id}';
    const currentUserRankId = ${user.rank.id};
    const currentDesign = '${chatroom.design}';
    var currentUserDp = '${user.dp}';
    var currentNameColor = '${user.nameColor}';
    var currentTextColor = '${user.textColor}';
    var currentUserFont = '${user.font}';
    var currentUserBold = '${user.bold}';
    var currentUserRJ = '${user.rj}';
    const currentActivity = '${user.activityDate}'

    const chatRoomId = '${user.chatRoomId}';
    const canDelete = '${user.rank.deleteMsg}';
    const canBan = '${user.rank.ban}';
    const canMute = '${user.rank.mute}';
    const canSpam = '${user.rank.spam}';
    const candeleteMsg = '${user.rank.deleteMsg}';
    const canKick = '${user.rank.kick}';
    const currentUserRank = '${user.rank.code}';
    const radioLink = '${chatroom.radio}';
    var radioSwitch = false;
</script>
<script src="/content/lib/app/emojis.js?v=${cache}" ></script>
<script src="/content/lib/app/functions.js?v=${cache}" ></script>
<script src="/content/lib/app/main.js?v=${cache}" ></script>
</body>
</html>
