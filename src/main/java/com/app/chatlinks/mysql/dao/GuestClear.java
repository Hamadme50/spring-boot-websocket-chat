package com.app.chatlinks.mysql.dao;

import java.util.TimerTask;

public class GuestClear extends TimerTask {

    ChatDAO db;
    public GuestClear(ChatDAO chatDAO) {
        db =chatDAO;
    }

    // Add your task here
    public void run() {

        try {
            db.clearGuest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            db.clearChatHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
