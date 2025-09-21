<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script>
    $(document).ready ( function(){

        if(sucessMsg != null && sucessMsg != undefined && sucessMsg != ''){
            var placementFrom = "top";
            var placementAlign = "center";
            var state = "success";
            var content = {};

            content.message = sucessMsg;
            content.title = '';
            content.icon = 'la la-bell';

            content.url = '';
            content.target = '';
            setTimeout(function (){
                $.notify(content,{
                    type: state,
                    placement: {
                        from: placementFrom,
                        align: placementAlign
                    },
                    time: 3000,
                });
            },2000);

        }
        else if(errorMsg != null && errorMsg != undefined && errorMsg != ''){
            var placementFrom = "top";
            var placementAlign = "center";
            var state = "danger";
            var content = {};

            content.message = errorMsg;
            content.title = '';
            content.icon = 'la la-bell';

            content.url = '';
            content.target = '';
            setTimeout(function (){
                $.notify(content,{
                    type: state,
                    placement: {
                        from: placementFrom,
                        align: placementAlign
                    },
                    time: 3000,
                });
            },2000);
        }
        else if(online != undefined){
            if(online != null && online != '') {
                var placementFrom = "top";
                var placementAlign = "center";
                if (online === "true") {
                    var state = "success";
                } else {
                    var state = "danger";
                }
                var content = {};

                if (online === "true") {
                    content.message = "Proxy is Online";
                } else {
                    content.message = "Proxy is Offline";
                }


                content.title = '';
                content.icon = 'la la-bell';

                content.url = '';
                content.target = '';

                setTimeout(function () {
                    $.notify(content, {
                        type: state,
                        placement: {
                            from: placementFrom,
                            align: placementAlign
                        },
                        time: 3000,
                    });
                }, 2000);
            }
        }
    });
</script>

<footer class="footer">
    <div class="container-fluid">

    </div>
</footer>
</div>
</div>
</div>
</body>
</html>

