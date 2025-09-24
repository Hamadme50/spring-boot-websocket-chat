package com.app.chatlinks.websockets;

import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.mysql.dao.ChatDAO;
import com.app.chatlinks.dto.UserDTO;
import com.app.chatlinks.dto.chat.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.app.chatlinks.websockets.ChatEngine.usersListRepo;

@Component
public class WebSocketEventListener {

    @Autowired
    ChatDAO chatDAO;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws Exception {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        UserDTO currentUser = chatDAO.getUserBySession(headerAccessor.getSessionId());
        if(currentUser != null){
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setUserId(currentUser.getId());
            chatMessage.setUserName(currentUser.getUserName());
            chatMessage.setUserStatus(currentUser.getStatus());

            try{
                if(!currentUser.getStatus().equals("S")) {
                    usersListRepo.computeIfPresent(currentUser.getChatRoomId(), (roomId, userMap) -> {
                        userMap.remove(currentUser.getId());
                        return userMap.isEmpty() ? null : userMap;
                    });
                }
            }
            catch (Exception e){
                try {
                    usersListRepo.computeIfPresent(currentUser.getChatRoomId(), (roomId, userMap) -> {
                        userMap.remove(currentUser.getId());
                        return userMap.isEmpty() ? null : userMap;
                    });
                }
                catch (Exception fd){
                    e.printStackTrace();
                }
                e.printStackTrace();
            }
            if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)){
                chatDAO.expireGuest(currentUser.getId());
            }
            chatDAO.removeUserSessionDb(currentUser.getId());
            if(!currentUser.getStatus().equals("S")){
                messagingTemplate.convertAndSend("/topic/"+currentUser.getChatRoomId(), chatMessage);
            }
        }



    }

    public UserDTO getCurrentUser(String token){

        UserDTO userDTO = null;
        try {
            Claims claims = decodeJWT(token);

            String userJsonBase64 = (String) claims.get("hash");
            byte[] bytes  = Base64.getDecoder().decode(userJsonBase64);
            String base64 = new String(bytes, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            userDTO = objectMapper.readValue(base64, UserDTO.class);
            if(userDTO.getId() == null){
                return null;
            }
            return userDTO;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userDTO;
    }
    public Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(GlobalConstants.BASE.TOKENKEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
