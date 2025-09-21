package com.app.chatlinks.mapper.chat;

import com.app.chatlinks.dto.chat.MainChatDTO;
import com.app.chatlinks.mapper.AbstractMapper;
import com.app.chatlinks.mysql.model.MainChat;

public class MainChatMapper extends AbstractMapper<MainChatDTO, MainChat> {
    @Override
    public MainChat mapToModel(MainChatDTO src) {
        MainChat dest = null;
        if(src != null){
            dest = new MainChat();
            dest.setId(src.getId());
            dest.setChatId(src.getChatId());
            dest.setContent(src.getContent());
            dest.setUserName(src.getUserName());
            dest.setUserId(src.getUserId());
            dest.setChatRoomId(src.getChatRoomId());
            dest.setNameColor(src.getNameColor());
            dest.setTextColor(src.getTextColor());
            dest.setBold(src.getBold());
            dest.setUserStatus(src.getUserStatus());
            dest.setFont(src.getFont());
            dest.setMsgDate(src.getMsgDate());
            dest.setType(src.getType());
            dest.setDp(src.getDp());

        }

        return dest;
    }

    @Override
    public MainChatDTO mapToDTO(MainChat src) {
        MainChatDTO dest = null;
        if(src != null){
            dest = new MainChatDTO();
            dest.setId(src.getId());
            dest.setChatId(src.getChatId());
            dest.setContent(src.getContent());
            dest.setUserName(src.getUserName());
            dest.setUserId(src.getUserId());
            dest.setChatRoomId(src.getChatRoomId());
            dest.setNameColor(src.getNameColor());
            dest.setTextColor(src.getTextColor());
            dest.setBold(src.getBold());
            dest.setUserStatus(src.getUserStatus());
            dest.setFont(src.getFont());
            dest.setMsgDate(src.getMsgDate());
            dest.setType(src.getType());
            dest.setDp(src.getDp());
        }

        return dest;
    }
}
