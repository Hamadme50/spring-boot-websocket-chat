package com.app.chatlinks.mapper;

import com.app.chatlinks.dto.ChatRoomDTO;
import com.app.chatlinks.mysql.model.ChatRoom;

public class ChatRoomMapper extends AbstractMapper<ChatRoomDTO, ChatRoom>{
    @Override
    public ChatRoom mapToModel(ChatRoomDTO src) {
        ChatRoom dest = null;
        if(src != null){
            dest = new ChatRoom();
            dest.setId(src.getId());
            dest.setName(src.getName());
            dest.setDomain(src.getDomain());
            dest.setTheme(src.getTheme());
            dest.setPaid(src.getPaid());
            dest.setDate(src.getDate());
            dest.setStatus(src.getStatus());
            dest.setTopic(src.getTopic());
            dest.setAntiSpam(src.getAntiSpam());
            dest.setRadio(src.getRadio());
            dest.setGuest(src.getGuest());
            dest.setRegister(src.getRegister());
            dest.setDesign(src.getDesign());
        }
        return dest;
    }

    @Override
    public ChatRoomDTO mapToDTO(ChatRoom src) {
        ChatRoomDTO dest = null;
        if(src != null){
            dest = new ChatRoomDTO();
            dest.setId(src.getId());
            dest.setName(src.getName());
            dest.setDomain(src.getDomain());
            dest.setTheme(src.getTheme());
            dest.setPaid(src.getPaid());
            dest.setDate(src.getDate());
            dest.setStatus(src.getStatus());
            dest.setTopic(src.getTopic());
            dest.setAntiSpam(src.getAntiSpam());
            dest.setRadio(src.getRadio());
            dest.setGuest(src.getGuest());
            dest.setRegister(src.getRegister());
            dest.setDesign(src.getDesign());

            dest.setCustomerId(src.getCustomer().getId());
        }
        return dest;
    }
}
