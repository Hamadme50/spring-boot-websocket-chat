package com.app.chatlinks.service;

import com.app.chatlinks.dto.chat.GetChatDTO;
import com.app.chatlinks.mysql.dao.ChatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    ChatDAO chatDAO;

    @Cacheable(value = "getChatRoomById", key = "#id")
    public GetChatDTO getChatRoomById(Long id) {
        return chatDAO.getChatRoomById(id);
    }
}
