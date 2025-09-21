package com.app.chatlinks.controller;

import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.config.HttpSessionConfig;
import com.app.chatlinks.dto.*;
import com.app.chatlinks.dto.chat.ChatMessage;
import com.app.chatlinks.dto.chat.ChatUserDTO;
import com.app.chatlinks.dto.chat.PrivateMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import static com.app.livechatzone.websockets.ChatController.usersListRepo;
import static com.app.chatlinks.websockets.ChatEngine.usersListRepo;

public class BaseController<T extends GenericDTO<T>> implements Serializable {
    public static ConcurrentMap<String, HttpSession> sessionsList = new ConcurrentHashMap<>();
    @Autowired
    public HttpSession session;
    @Autowired
    public HttpServletRequest request;
    private Class<T> dtoClass;
    protected T dataObject;
    @Autowired
    protected Environment environment;
    @Autowired
    public HttpSessionConfig sessionFactory;

    @Autowired
    public SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public static ResourcePatternResolver resourcePatternResolver;


    public String createJWT(UserDTO userDTO) throws JsonProcessingException {
        String timeoutValue = environment.getProperty("chatlinks.token.timeout");
        Long ttlMillis = Long.valueOf(timeoutValue);
        ttlMillis = ttlMillis * 60000;
        Map<String, Object> customClaims = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper(); // or use an existing one
        String json = objectMapper.writeValueAsString(userDTO);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.getEncoder().encodeToString(bytes);

        customClaims.put("hash", base64);
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(GlobalConstants.BASE.TOKENKEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(customClaims)
                .setIssuedAt(now)
                .setSubject(userDTO.getUserName())
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(GlobalConstants.BASE.TOKENKEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
    /*public void removeUserFromList(UserDTO user){
        if (usersListRepo.get(user.getId().toString()) != null) {
            usersListRepo.remove(user.getId().toString());
        }
        userDaoDb.changeUserStatus(user.getId(), GlobalConstants.USER_STATUS.ONLINE);
        reloadUserList();
    }*/


   /* public void sendKick(String userUID){
        PrivateMsgDTO msg = new PrivateMsgDTO();
        msg.setType(PrivateMsgDTO.PrivateMessageType.KICK);
        messagingTemplate.convertAndSend("/topic/"+userUID,msg);
    }

    public void joinMessage(UserDTO user){
        ChatMessage msg = new ChatMessage();
        msg.setType(ChatMessage.MessageType.WELCOME);
        msg.setContent(user.getUsername() + " has joined the chat");
        msg.setUid(user.getUserUID());
        msg.setSender(user.getUsername());
        messagingTemplate.convertAndSend("/topic/public",msg);
    }*/
   public String GetFileExtension(String base64String)
   {
       String[] strings = base64String.split(",");
       switch (strings[0]) {//check image's extension
           case "data:image/jpeg;base64":
               return  "jpeg";
           case "data:image/png;base64":
               return "png";
           case "data:image/PNG;base64":
               return "PNG";
           case "data:image/JPEG;base64":
               return "JPEG";
           case "data:image/jpg;base64":
               return "jpg";
           case "data:image/HEIC;base64":
               return "HEIC";
           default:
               return "NO";
       }
   }
   public String getUploadFolder (String folder) {
       ClassLoader loader = Thread.currentThread().getContextClassLoader();
       URL url = loader.getResource(folder);
       String path = url.getPath();
       return path;
   }
   public void sendJoin(UserDTO userDTO){
       ChatUserDTO chatUserDTO = populateUser(userDTO);
       ChatMessage chatMessage = new ChatMessage();
       chatMessage.setType(ChatMessage.MessageType.JOIN);
       chatMessage.setUserName(userDTO.getUserName());
       chatMessage.setUser(chatUserDTO);
       java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
       chatMessage.setId(date.getTime()+"_"+userDTO.getChatRoomId());

       messagingTemplate.convertAndSend("/topic/"+userDTO.getChatRoomId(), chatMessage);
   }
   public void sendJoinMSG(UserDTO userDTO){
       ChatUserDTO chatUserDTO = populateUser(userDTO);
       ChatMessage chatMessage = new ChatMessage();
       chatMessage.setType(ChatMessage.MessageType.JOINMSG);
       chatMessage.setUserName(userDTO.getUserName());
       chatMessage.setUser(chatUserDTO);
       java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
       chatMessage.setId(date.getTime()+"_"+userDTO.getChatRoomId());

       messagingTemplate.convertAndSend("/topic/"+userDTO.getChatRoomId(), chatMessage);
   }
   public void clearChat(UserDTO userDTO){
       ChatMessage chatMessage = new ChatMessage();
       chatMessage.setType(ChatMessage.MessageType.CLEAR);
       chatMessage.setUserName(userDTO.getUserName());
       messagingTemplate.convertAndSend("/topic/"+userDTO.getChatRoomId(), chatMessage);
   }
   public void sendLeave(UserDTO targetUser){
       ChatMessage chatMessage = new ChatMessage();
       chatMessage.setType(ChatMessage.MessageType.LEAVE);
       chatMessage.setUserName(targetUser.getUserName());
       chatMessage.setUserStatus(targetUser.getStatus());

       messagingTemplate.convertAndSend("/topic/"+targetUser.getChatRoomId(), chatMessage);
   }
    public void sendKick(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.KICK);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendSpam(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.SPAMMER);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendMute(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.MUTE);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendUnMute(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.UNMUTE);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendUnSpam(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.UNSPAMMER);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendBan(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.BAN);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendReconnect(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.RECONNECT);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendReload(Long userId){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.RELOAD);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendUpdateRj(Long userId,String value){
        PrivateMessage msg = new PrivateMessage();
        msg.setMsgType(PrivateMessage.MessageType.UPDATERJ);
        msg.setContent(value);
        messagingTemplate.convertAndSend("/queue/"+userId, msg);
    }
    public void sendLogoutSameUser(UserDTO currentUser){
        ChatMessage msg = new ChatMessage();
        msg.setType(ChatMessage.MessageType.SAMEUSER);
        msg.setUserId(currentUser.getId());
        messagingTemplate.convertAndSend("/topic/"+currentUser.getChatRoomId(), msg);
    }
    public boolean allowRank(UserDTO current ,UserDTO target ){
        if(current.getRank().getId().compareTo(target.getRank().getId()) < 0){
            return true;
        }
        else{
            return false;
        }
    }

    public ChatUserDTO populateUser(UserDTO userDTO){
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

    public List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public String setUserSession(UserDTO userDTO){
        try{
            String token = createJWT(userDTO);
            return token;
        }
        catch (Exception e){
            return null;
        }
    }
    public void endUserSession(Long chatRoomId){
        session.setAttribute(GlobalConstants.SESSION.USER + "-" + chatRoomId, null);
        session.setAttribute(GlobalConstants.SESSION.CHATROOMID , null);
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


    public void removeFromUserList(UserDTO currentUser){
       try{
           if (usersListRepo.containsKey(currentUser.getChatRoomId())) {
               if (usersListRepo.get(currentUser.getChatRoomId()).get(currentUser.getId()) != null) {
                   usersListRepo.get(currentUser.getChatRoomId()).remove(currentUser.getId());
               }
           }
       }
       catch (Exception e){
           e.printStackTrace();
       }

    }

    public String getUserIP(){
        String ip = request.getHeader(GlobalConstants.BASE.IP_HEADER);
        if(ip == null || ip.equals("")){
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public CustomerDTO getCurrentCustomer(){
        CustomerDTO customerDTO = new CustomerDTO();
        String customerSession = request.getSession().getAttribute(GlobalConstants.SESSION.CUSTOMER).toString();
        Gson gson = new Gson();
        customerDTO = gson.fromJson(customerSession,CustomerDTO.class);
        return customerDTO;
    }
    /*public Boolean allowRank(String rank,UserDTO user){
       Boolean flag = false;
       if(rank.equalsIgnoreCase(user.getRank().getCode())){
           flag = true;
       }
        return flag;
    }*/

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public T getDataObject() {
        return dataObject;
    }

    public void setDataObject(T dataObject) {
        this.dataObject = dataObject;
    }

    public static Map<String, String> emo = new HashMap<String, String>();


}
