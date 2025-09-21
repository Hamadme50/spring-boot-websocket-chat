package com.app.chatlinks.config;

public interface GlobalConstants {

    interface BASE{
        String DOMAIN = "https://localhost";
        String CHATROOMURL = "https://localhost/chat/";
        String UPLOAD = "static/content/upload";
        String UPLOADBACKUP = "/home/chatlinks/src/main/resources/static/content/upload";
        String DELTEUPLOAD = "/home/chatlinks/target/classes/static/content/upload";
        String UPLOADURL = "/content/upload/";
        String IP_HEADER = "CF-Connecting-IP";
        String IP_HEADER_COMMON = "X-Forwarded-For";
        Double CACHE = 5.3;
        String GUESTPASSWORD = "CHGUESTSZZX";
        String TOKENKEY = "CLTOKENKEYADDDD";
    }
    interface EMAIL{
        String ADDRESS = "";
        String USERNAME = "";
        String PASSWORD = "";
        String SUBJECT = "Forgot password request for nickname : ";
        String SUBJECTCUSTOMER = "Forgot password request for account : ";
    }

    interface USER_STATUS{
        String MUTED = "M";
        String ONLINE = "O";
        String AWAY = "A";
        String BUSY = "B";
        String STAY = "S";
        String EATING = "E";
        String GAMING = "G";
        String INVISIBLE = "I";
        String DELETE = "D";
    }

    interface USER_RANKS{
        String OWNER = "OWNER";
        String ADMIN = "ADMIN";
        String MOD = "MOD";
        String CUSTOM = "CUSTOM";
        String STAR = "STAR";
        String VIP = "VIP";
        String MEMBER = "MEMBER";
        String GUEST = "GUEST";
    }
    interface SESSION{
        String CUSTOMER = "customer_session";
        String USER = "user_session";
        String CHATROOMID = "chatroom_id";
    }
}
