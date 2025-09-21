package com.app.chatlinks.controller;

import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.mysql.dao.ChatDAO;
import com.app.chatlinks.dto.ChatRoomDTO;
import com.app.chatlinks.dto.UserDTO;
import com.app.chatlinks.dto.chat.ChatLoginDTO;
import com.app.chatlinks.dto.chat.GetChatDTO;
import com.app.chatlinks.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/chat")
public class ChatRoomController extends BaseController{

    @Autowired
    ChatDAO chatDAO;

    @Autowired
    ChatService chatService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String chat(@PathVariable Long id,ModelMap model) throws IOException {
        model.addAttribute("cache", GlobalConstants.BASE.CACHE);
        model.addAttribute("chatroom", id);
        return "chat/login";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String chat(ChatLoginDTO data, ModelMap model, Long id) throws IOException {
        model.addAttribute("cache", GlobalConstants.BASE.CACHE);
        try {
            if(data.getToken() != null && (!data.getToken().equals(""))){
                if(data.getToken().equals("logout")){
                    model.addAttribute("chatroom", data.getId());
                    return "chat/login";
                }
                else{
                    UserDTO loginUser = getCurrentUser(data.getToken());
                    UserDTO current = chatDAO.getUserById(loginUser.getId(),loginUser.getChatRoomId());
                    if(current.getStatus().equals(GlobalConstants.USER_STATUS.DELETE)){
                        model.addAttribute("chatroom", data.getId());
                        return "chat/login";
                    }
                    String token = createJWT(current);
                    model.addAttribute("user", current);
                    model.addAttribute("token", token);

                    ChatRoomDTO chatRoom = chatDAO.getChatRoomDataDTO(current.getChatRoomId());
                    model.addAttribute("chatroom", chatRoom);
                    sendJoinMSG(current);
                    if(chatRoom.getDesign().equals("I")){
                        return "chat/irc";
                    }
                    else{
                        return "chat/chat";
                    }

                }
            }

            String ip = getUserIP();
            if(chatDAO.checkIpBanned(ip,data.getId())){
                return  "chat/ban";
            }
            if(data.getNickname() != null){
                data.setNickname(data.getNickname().replaceAll("\\s+",""));
            }
            if(data.getId() != null && data.getNickname() != null && (!data.getNickname().equals("")) && (data.getPassword() == null || data.getPassword().equals("") ) && (data.getEmail() == null || data.getEmail().equals(""))){
                if(data.getNickname() != null && data.getNickname().length() < 3){
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", "Nickname can't less than 3 characters");
                    return "chat/login";
                }
                if(data.getNickname() != null && data.getNickname().length() > 12){
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", "Nickname can't greater than 12 characters");
                    return "chat/login";
                }
                if(!chatDAO.checkUserNameExist(data.getNickname(),data.getId())) {
                    UserDTO userDTO = chatDAO.getGuestLogin(data.getNickname(), ip, data.getId());
                    if (userDTO != null) {
                        String token = createJWT(userDTO);
                        model.addAttribute("user", userDTO);
                        model.addAttribute("token", token);

                        ChatRoomDTO chatRoom = chatDAO.getChatRoomDataDTO(userDTO.getChatRoomId());
                        model.addAttribute("chatroom", chatRoom);
                        sendJoinMSG(userDTO);
                        if(chatRoom.getDesign().equals("I")){
                            return "chat/irc";
                        }
                        else{
                            return "chat/chat";
                        }

                    } else {
                        model.addAttribute("chatroom", data.getId());
                        model.addAttribute("error", "Guest is temporary disabled by chat room owner");
                        return "chat/login";
                    }
                }
                else{
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", data.getNickname()+" nickname is already taken");
                    return "chat/login";
                }

            }
            else if (data.getId() != null && data.getNickname() != null && (!data.getNickname().equals("")) && data.getPassword() != null && (!data.getPassword().equals(""))  && (data.getEmail() == null || data.getEmail().equals(""))) {
                UserDTO userDTO = chatDAO.getLogin(data.getNickname(), data.getPassword(),ip, data.getId());

                if (userDTO != null) {
                    boolean sameuser =chatDAO.checkSessionAllow(userDTO.getId());
                    if (userDTO.getBan().equals("Y")) {
                        return "chat/ban";
                    }
                    if(!sameuser){
                        sendLogoutSameUser(userDTO);
                        chatDAO.removeUserSessionDb(userDTO.getId());
                        model.addAttribute("chatroom", data.getId());
                        model.addAttribute("error", "Your account did not correctly logout previously so we logout your account successfully now please login again");
                        return "chat/login";
                    }
                    String token = createJWT(userDTO);
                    model.addAttribute("user", userDTO);
                    model.addAttribute("token", token);
                    ChatRoomDTO chatRoom = chatDAO.getChatRoomDataDTO(userDTO.getChatRoomId());
                    model.addAttribute("chatroom", chatRoom);
                    sendJoinMSG(userDTO);
                    if(chatRoom.getDesign().equals("I")){
                        return "chat/irc";
                    }
                    else{
                        return "chat/chat";
                    }


                }
                else {
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", "Your have entered wrong nickname or password");
                    return "chat/login";
                }

            }
            else if(data.getId() != null && data.getNickname() != null && (!data.getNickname().equals("")) && data.getPassword() != null && (!data.getPassword().equals("")) && data.getEmail() != null && (!data.getEmail().equals(""))) {
                if(data.getNickname() != null && data.getNickname().length() < 3){
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", "Nickname can't less than 4 characters");
                    return "chat/login";
                }
                if(data.getNickname() != null && data.getNickname().length() > 12){
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", "Nickname can't greater than 12 characters");
                    return "chat/login";
                }
                if(!chatDAO.checkUserNameExist(data.getNickname(),data.getId())) {
                    if(!chatDAO.checkEmailExist(data.getEmail(),data.getId())) {
                        UserDTO userDTO = chatDAO.registerNewUser(data.getNickname(), data.getPassword(), data.getEmail(), ip, data.getId());
                        if (userDTO != null) {
                            String token = createJWT(userDTO);
                            model.addAttribute("user", userDTO);
                            model.addAttribute("token", token);
                            ChatRoomDTO chatRoom = chatDAO.getChatRoomDataDTO(userDTO.getChatRoomId());
                            model.addAttribute("chatroom", chatRoom);
                            sendJoinMSG(userDTO);
                            if(chatRoom.getDesign().equals("I")){
                                return "chat/irc";
                            }
                            else{
                                return "chat/chat";
                            }
                        } else {
                            model.addAttribute("chatroom", data.getId());
                            model.addAttribute("error", "Registration is temporary disabled by chat room owner");
                            return "chat/login";
                        }
                    }
                    else{
                        model.addAttribute("chatroom", data.getId());
                        model.addAttribute("error", data.getEmail()+" Email is already taken");
                        return "chat/login";
                    }

                }
                else{
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", data.getNickname()+" nickname is already taken");
                    return "chat/login";
                }

            }
            else if(data.getId() != null && data.getEmail() != null && (!data.getEmail().equals(""))  &&  (data.getNickname() == null || data.getPassword() == null || data.getPassword().equals("")  || data.getNickname().equals("") )) {
                if(chatDAO.checkEmailExist(data.getEmail(),data.getId())) {
                    chatDAO.RecoverUserPassword(data.getEmail(),data.getId());
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", "Your passowrd send to "+data.getEmail()+ " please check inbox or spam folder");
                    return "chat/login";
                }
                else{
                    model.addAttribute("chatroom", data.getId());
                    model.addAttribute("error", data.getEmail()+" Email is not attached with any account");
                    return "chat/login";
                }
            }

            else {
                model.addAttribute("error", "Please fill details correctly");
                model.addAttribute("chatroom", data.getId());
                return "chat/login";
            }
        }
        catch (Exception e){e.printStackTrace();}
        model.addAttribute("error", "Your session has been expired please re login");
        model.addAttribute("chatroom", data.getId());
        return "chat/login";
    }

    @RequestMapping(value = "/getChatRoom", method = RequestMethod.POST)
    @ResponseBody
    public GetChatDTO getChatRoom(Long id){
        GetChatDTO data = null;
        try {
            if(id != null && (!id.equals(0))){
                data = chatService.getChatRoomById(id);
                return data;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }



}
