package com.app.chatlinks.bigData;

import com.app.chatlinks.dto.chat.MainChatDTO;
import com.app.chatlinks.mapper.AbstractMapper;

import java.util.Date;


public class MainChatMapper extends AbstractMapper<MainChatDTO, MainChat> {

    @Override
    public MainChat mapToModel(MainChatDTO src) {
        MainChat dest = null;
        if(src != null){
            dest = new MainChat();
          //  dest.setId(String.valueOf(src.getId()));
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
            java.util.Date utilDate = new java.util.Date(src.getMsgDate().getTime());
            dest.setDate(utilDate.toInstant());
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
        //    dest.setId(Long.valueOf(src.getId()));
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
            dest.setMsgDate(Date.from(src.getDate()));
            dest.setType(src.getType());
            dest.setDp(src.getDp());
        }

        return dest;
    }
}
