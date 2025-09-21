package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.PrivateChatDTO;
import com.app.chatlinks.mysql.model.PrivateChat;

public class PrivateChatMapper extends AbstractMapper<PrivateChatDTO, PrivateChat>{
    @Override
    public PrivateChat mapToModel(PrivateChatDTO src) {
        PrivateChat dest = null;
        if(src != null){
            dest = new PrivateChat();
            dest.setId(src.getId());
            dest.setChat(src.getChat());
            dest.setDate(src.getDate());
            dest.setSeen(src.getSeen());

        }
        return dest;
    }

    @Override
    public PrivateChatDTO mapToDTO(PrivateChat src) {
        PrivateChatDTO dest = null;
        if(src != null){
            dest = new PrivateChatDTO();
            dest.setId(src.getId());
            dest.setChat(src.getChat());
            dest.setDate(src.getDate());
            dest.setSeen(src.getSeen());

            dest.setChatRoomId(src.getChatRoom().getId());
            dest.setFromUserId(src.getFromUser().getId());
            dest.setToUserId(src.getToUser().getId());

        }
        return dest;
    }
}
