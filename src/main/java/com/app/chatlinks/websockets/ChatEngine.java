package com.app.chatlinks.websockets;

import com.app.chatlinks.bigData.MainChatDataService;
import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.mysql.dao.ChatDAO;
import com.app.chatlinks.dto.UserDTO;
import com.app.chatlinks.dto.chat.ChatMessage;
import com.app.chatlinks.dto.chat.ChatUserDTO;
import com.app.chatlinks.dto.chat.PrivateMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatEngine{
    @Autowired
    MainChatDataService mainChatDataService;

    @Autowired
    ChatDAO chatDAO;

    public static Map<Long, ConcurrentHashMap<Long, ChatUserDTO>> usersListRepo = new ConcurrentHashMap<>();

    @Autowired
    public SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage aChatMessage, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        try {
            ChatMessage chatMessage = new ChatMessage();

            List token = headerAccessor.getNativeHeader("X-Authorization");
            String currentToken = (String) token.get(0);
            UserDTO currentUser = getCurrentUser(currentToken);
            if(aChatMessage.getType().equals(ChatMessage.MessageType.DELETE)){
                if(currentUser.getRank().getDeleteMsg().equals("Y")){
                    chatMessage.setContent(aChatMessage.getContent());
                    chatMessage.setType(ChatMessage.MessageType.DELETE);
                    messagingTemplate.convertAndSend("/topic/"+currentUser.getChatRoomId(), chatMessage);

                    new Thread(() -> {
                        try {
                            mainChatDataService.deleteMainChat(currentUser.getChatRoomId(),aChatMessage.getContent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            else if(aChatMessage.getType().equals(ChatMessage.MessageType.REPORT)){
                chatMessage.setContent(currentUser.getUserName());
                chatMessage.setType(ChatMessage.MessageType.REPORT);
                chatMessage.setId(aChatMessage.getContent());
                messagingTemplate.convertAndSend("/topic/"+currentUser.getChatRoomId(), chatMessage);
            }
            else if(aChatMessage.getType().equals(ChatMessage.MessageType.IMAGE)){
                if( (!currentUser.getStatus().equals("M")) && (!currentUser.getBan().equals("Y")) ){
                    if((!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) && (!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.MEMBER))) {
                        chatMessage.setContent(aChatMessage.getContent());
                        chatMessage.setUserName(currentUser.getUserName());
                        chatMessage.setDp(currentUser.getDp());
                        chatMessage.setNameColor(currentUser.getNameColor());
                        chatMessage.setTextColor(currentUser.getTextColor());
                        chatMessage.setImage(aChatMessage.getImage());
                        chatMessage.setBold(currentUser.getBold());
                        chatMessage.setType(ChatMessage.MessageType.IMAGE);
                        chatMessage.setUserId(currentUser.getId());
                        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                        chatMessage.setId(date.getTime() + "_" + currentUser.getChatRoomId());

                        messagingTemplate.convertAndSend("/topic/" + currentUser.getChatRoomId(), chatMessage);
                    }
                }
            }
            else{
                if( (!currentUser.getStatus().equals("M")) && (!currentUser.getBan().equals("Y")) ){
                    chatMessage.setContent(aChatMessage.getContent());
                    chatMessage.setUserName(currentUser.getUserName());
                    chatMessage.setDp(currentUser.getDp());
                    chatMessage.setNameColor(currentUser.getNameColor());
                    chatMessage.setTextColor(currentUser.getTextColor());
                    chatMessage.setBold(currentUser.getBold());
                    chatMessage.setFont(currentUser.getFont());
                    if(currentUser.getSpam().equals("Y")){
                        chatMessage.setType(ChatMessage.MessageType.SPAM);
                    }
                    else{
                        chatMessage.setType(ChatMessage.MessageType.CHAT);
                    }
                    chatMessage.setUserId(currentUser.getId());
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    chatMessage.setMsgDate(date);
                    chatMessage.setId(date.getTime()+"_"+currentUser.getChatRoomId());
                    chatMessage.setChatRoomId(currentUser.getChatRoomId());
                    chatMessage.setUserStatus(currentUser.getStatus());
                    messagingTemplate.convertAndSend("/topic/"+currentUser.getChatRoomId(), chatMessage);



                }
            }

            try {
                    mainChatDataService.saveMainChat(chatMessage,currentUser.getIp());
                } catch (Exception e) {
                    e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @MessageMapping("/chat.sendPm")
    public void sendPm(@Payload PrivateMessage aPrivateMessage, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        try {
            PrivateMessage privateMessage = new PrivateMessage();

            List token = headerAccessor.getNativeHeader("X-Authorization");
            String currentToken = (String) token.get(0);
            UserDTO currentUser = getCurrentUser(currentToken);
            if( (!currentUser.getStatus().equals("M")) && (!currentUser.getBan().equals("Y")) ) {

                if(aPrivateMessage.getMsgType() != null && aPrivateMessage.getMsgType().equals(PrivateMessage.MessageType.IMAGE)) {
                    if ((!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) && (!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.MEMBER))) {
                        privateMessage.setToUserId(aPrivateMessage.getToUserId());
                        privateMessage.setContent(aPrivateMessage.getContent());

                        privateMessage.setFromUserId(currentUser.getId());
                        privateMessage.setFromUserName(currentUser.getUserName());
                        privateMessage.setFromNameColor(currentUser.getNameColor());
                        privateMessage.setFromTextColor(currentUser.getTextColor());
                        privateMessage.setFromTextFont(currentUser.getFont());
                        privateMessage.setFromDp(currentUser.getDp());
                        privateMessage.setSeen("N");
                        privateMessage.setImage(aPrivateMessage.getImage());
                        privateMessage.setMsgType(PrivateMessage.MessageType.IMAGE);
                        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                        privateMessage.setMsgDate(date);
                        privateMessage.setChatRoomId(currentUser.getChatRoomId());

                        messagingTemplate.convertAndSend("/queue/" + aPrivateMessage.getToUserId(), privateMessage);
                    }
                }
                else{
                    privateMessage.setToUserId(aPrivateMessage.getToUserId());
                    privateMessage.setContent(aPrivateMessage.getContent());

                    privateMessage.setFromUserId(currentUser.getId());
                    privateMessage.setFromUserName(currentUser.getUserName());
                    privateMessage.setFromNameColor(currentUser.getNameColor());
                    privateMessage.setFromTextColor(currentUser.getTextColor());
                    privateMessage.setFromTextFont(currentUser.getFont());
                    privateMessage.setFromDp(currentUser.getDp());
                    privateMessage.setSeen("N");
                    privateMessage.setMsgType(PrivateMessage.MessageType.CHAT);
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    privateMessage.setMsgDate(date);
                    privateMessage.setChatRoomId(currentUser.getChatRoomId());

                    messagingTemplate.convertAndSend("/queue/" + aPrivateMessage.getToUserId(), privateMessage);


                }

                try{
                    mainChatDataService.savePrivateChat(privateMessage,currentUser.getIp());
                }
                catch (Exception e){

                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage aChatMessage,
                        SimpMessageHeaderAccessor headerAccessor) throws Exception {
        try {
            List token = headerAccessor.getNativeHeader("X-Authorization");
            String currentToken = (String) token.get(0);
            UserDTO currentUser = getCurrentUser(currentToken);
            ChatUserDTO chatUserDTO = populateUser(currentUser);

            try{
                usersListRepo.computeIfPresent(currentUser.getChatRoomId(), (roomId, userMap) -> {
                    userMap.remove(currentUser.getId());
                    return userMap.isEmpty() ? null : userMap;
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                /*if (!usersListRepo.containsKey(currentUser.getChatRoomId())) {
                    usersListRepo.put(currentUser.getChatRoomId(), new ConcurrentHashMap<Long, ChatUserDTO>());
                }
                if (usersListRepo.get(currentUser.getChatRoomId()).get(currentUser.getId()) == null) {
                    usersListRepo.get(currentUser.getChatRoomId()).put(currentUser.getId(), chatUserDTO);
                } else {
                    usersListRepo.get(currentUser.getChatRoomId()).remove(currentUser.getId());
                    if (usersListRepo.get(currentUser.getChatRoomId()).size() == 0) {
                        usersListRepo.put(currentUser.getChatRoomId(), new ConcurrentHashMap<Long, ChatUserDTO>());
                        usersListRepo.get(currentUser.getChatRoomId()).put(currentUser.getId(), chatUserDTO);
                    } else {
                        usersListRepo.get(currentUser.getChatRoomId()).put(currentUser.getId(), chatUserDTO);
                    }

                }*/


                usersListRepo.compute(currentUser.getChatRoomId(), (roomId, userMap) -> {
                    if (userMap == null) {
                        userMap = new ConcurrentHashMap<>();
                    }
                    userMap.put(currentUser.getId(), chatUserDTO);
                    return userMap;
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }
            chatDAO.setUserSessionDb(currentUser.getId(),headerAccessor.getSessionId());

            PrivateMessage privateMessage = new PrivateMessage();
            privateMessage.setMsgType(PrivateMessage.MessageType.GETUSERS);
            messagingTemplate.convertAndSend("/queue/" + currentUser.getId(), privateMessage);

            if(!currentUser.getStatus().equals("S")){
                sendJoin(chatUserDTO);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private ChatUserDTO populateUser(UserDTO userDTO){
        ChatUserDTO chatUserDTO = new ChatUserDTO();
        chatUserDTO.setChatRoomId(userDTO.getChatRoomId());
        chatUserDTO.setUserId(userDTO.getId());
        chatUserDTO.setUserName(userDTO.getUserName());
        chatUserDTO.setNameColor(userDTO.getNameColor());
        chatUserDTO.setStatus(userDTO.getStatus());
        chatUserDTO.setGender(userDTO.getGender());
        chatUserDTO.setDp(userDTO.getDp());
        chatUserDTO.setFont(userDTO.getFont());
        chatUserDTO.setTextColor(userDTO.getTextColor());
        chatUserDTO.setTweet(userDTO.getTweet());
        chatUserDTO.setAbout(userDTO.getAbout());
        chatUserDTO.setPoints(userDTO.getPoints());
        chatUserDTO.setBirthDate(userDTO.getBirthDate());
        chatUserDTO.setActivityDate(userDTO.getActivityDate());
        chatUserDTO.setJoinDate(userDTO.getJoinDate());
        chatUserDTO.setRankCode(userDTO.getRank().getCode());
        chatUserDTO.setRankName(userDTO.getRank().getName());
        chatUserDTO.setRankIcon(userDTO.getRank().getIcon());
        chatUserDTO.setRj(userDTO.getRj());
        chatUserDTO.setSpam(userDTO.getSpam());
        chatUserDTO.setBan(userDTO.getBan());
        chatUserDTO.setBold(userDTO.getBold());
        chatUserDTO.setRankId(userDTO.getRankId());

        return chatUserDTO;
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

    public void sendJoin(ChatUserDTO chatUserDTO){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setUserName(chatUserDTO.getUserName());
        chatMessage.setUser(chatUserDTO);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        chatMessage.setId(date.getTime()+"_"+chatUserDTO.getChatRoomId());

        messagingTemplate.convertAndSend("/topic/"+chatUserDTO.getChatRoomId(), chatMessage);
    }

}
