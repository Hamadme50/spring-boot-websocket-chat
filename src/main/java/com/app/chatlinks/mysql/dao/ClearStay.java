package com.app.chatlinks.mysql.dao;

import com.app.chatlinks.dto.chat.ChatUserDTO;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.app.chatlinks.websockets.ChatEngine.usersListRepo;

public class ClearStay extends TimerTask {

    // Add your task here
    public void run() {

        try {
            LocalDate dateBefore =  LocalDate.now().minusDays(2);
            java.sql.Date date = java.sql.Date.valueOf( dateBefore );
            List<ChatUserDTO> removeUsers = new ArrayList<>();
            for (Map.Entry<Long, ConcurrentHashMap<Long, ChatUserDTO>> entry : usersListRepo.entrySet()) {
                for (Map.Entry<Long, ChatUserDTO> userList : entry.getValue().entrySet()) {
                    ChatUserDTO user =  userList.getValue();
                    if(user.getActivityDate().before(date)){
                        removeUsers.add(user);
                    }
                }
            }
            for(ChatUserDTO userDTO : removeUsers){
                try{
                    usersListRepo.get(userDTO.getChatRoomId()).remove(userDTO.getId());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
