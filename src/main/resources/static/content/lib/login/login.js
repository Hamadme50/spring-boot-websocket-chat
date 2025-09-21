$( document ).ready(function() {

    var dt = new Date();
    dt.setSeconds(dt.getSeconds() + 60);
    document.cookie = "cookietest=1; expires=" + dt.toGMTString();
    var cookiesEnabled = document.cookie.indexOf("cookietest=") != -1;
    if(!cookiesEnabled){
       // $("#smallModal").show();
    }

    $('#ihavepassword').on('change', function() {
        if(this.checked === true){
            $("#password").show();
            $("#nickname").show();
            $("#password").prop('required',true);
            $("#nickname").prop('required',true);
        }
        else{
            $("#password").hide();
            $("#password").prop('required',false);
        }
    });

    $('#registerClick').on('click', function() {
        $("#email").show();
        $("#nickname").show();
        $("#registerText").hide();
        $("#loginText").show();
        $("#password").show();
        $("#ihavepassword").prop("disabled",true);
        $("#ihavepassword").prop("checked",false);
        $("#nickname").prop('required',true);

        $("#password").prop('required',true);
        $("#email").prop('required',true);

        $(".loginbutton").html("Register");
    });
    $('#forgotClick').on('click', function() {
        $("#email").show();
        $("#password").hide();
        $("#nickname").hide();
        $("#ihavepassword").prop("disabled",true);
        $("#ihavepassword").prop("checked",false);

        $("#password").prop('required',false);
        $("#nickname").prop('required',false);
        $("#email").prop('required',true);

        $(".loginbutton").html("Recover Password");
    });

    $('#loginClick').on('click', function() {
        $("#nickname").show();
        $("#email").hide();
        $("#registerText").show();
        $("#loginText").hide();
        $("#password").hide();
        $("#password").prop('required',false);
        $("#email").prop('required',false);
        $("#ihavepassword").prop("disabled",false);
        $("#nickname").prop('required',true);

        $(".loginbutton").html("Login");
    });

    function getChatRoom(){
        var chatroomId = $("#chatroom").val();
        if(chatroomId != null && chatroomId != 0 && chatroomId != ""){
            $.post('/chat/getChatRoom', {
                id: chatroomId,
            }, function(response) {
                if(response !== null && response !== ""){
                    $("#chatname").text(response.name);
                    $("#chattopic").text(response.topic);
                    $(".chatroomcolor").css("background-color",response.theme);
                    $("#chatroomlogin").show();
                    $("#email").val("");
                    $("#password").val("");
                }
                else {
                    $("#error404").show();
                }

            });
        }
    }
    setTimeout(getChatRoom(),3000);

});
