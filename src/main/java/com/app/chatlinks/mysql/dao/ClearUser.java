package com.app.chatlinks.mysql.dao;

import java.util.TimerTask;

public class ClearUser extends TimerTask {

    ChatDAO db;
    public ClearUser(ChatDAO chatDAO) {
        db =chatDAO;
    }

    // Add your task here
    public void run() {
        try {
            db.clearUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
