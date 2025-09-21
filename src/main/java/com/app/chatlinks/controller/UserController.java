package com.app.chatlinks.controller;

import com.app.chatlinks.bigData.MainChatDataService;
import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.dto.chat.PrivateMessage;
import com.app.chatlinks.mysql.dao.ChatDAO;
import com.app.chatlinks.mysql.dao.PanelDAO;
import com.app.chatlinks.dto.*;
import com.app.chatlinks.dto.chat.ChatUserDTO;
import com.app.chatlinks.dto.chat.MainChatDTO;
import com.app.chatlinks.dto.panel.DashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import static com.app.chatlinks.websockets.ChatEngine.usersListRepo;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
    @Autowired
    ChatDAO chatDAO;

    @Autowired
    PanelDAO panelDAO;

    @Autowired
    MainChatDataService mainChatDataService;

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public int logout(String token){
        //endUserSession(chatroomid);
        return 1;
    }

    @RequestMapping(value = "/updateUnSpam", method = RequestMethod.POST)
    @ResponseBody
    public String updateUnSpam(String token){
        UserDTO currentUser = getCurrentUser(token);
        currentUser.setSpam("N");
        return setUserSession(currentUser);
    }
    @RequestMapping(value = "/updateMute", method = RequestMethod.POST)
    @ResponseBody
    public String updateMute(String token){
        UserDTO currentUser = getCurrentUser(token);
        currentUser.setStatus("M");
        return  setUserSession(currentUser);
    }
    @RequestMapping(value = "/updateRJ", method = RequestMethod.POST)
    @ResponseBody
    public String updateRJ(String value,String token){
        UserDTO currentUser = getCurrentUser(token);
        currentUser.setRj(value);
        return setUserSession(currentUser);
    }
    @RequestMapping(value = "/updateUnMute", method = RequestMethod.POST)
    @ResponseBody
    public String updateUnMute(String token){
        UserDTO currentUser = getCurrentUser(token);
        currentUser.setStatus("O");
        return setUserSession(currentUser);
    }
    @RequestMapping(value = "/updateBan", method = RequestMethod.POST)
    @ResponseBody
    public String updateBan(String token){
        UserDTO currentUser = getCurrentUser(token);
        currentUser.setBan("Y");
        return setUserSession(currentUser);
    }
    @RequestMapping(value = "/updateSpam", method = RequestMethod.POST)
    @ResponseBody
    public String updateSpam(String token){
        UserDTO currentUser = getCurrentUser(token);
        currentUser.setSpam("Y");
        return setUserSession(currentUser);
    }

    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ResponseBody
    public List<ChatUserDTO> getUserList(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getId() != null){
                    List<ChatUserDTO> userList = new ArrayList<>();
                    userList.addAll(usersListRepo.get(currentUser.getChatRoomId()).values());
                    return userList;
                }

            }

        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/getUserIP", method = RequestMethod.POST)
    @ResponseBody
    public String getUserIP(String username,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getBan().equals("Y") && (!username.equals(""))){
                    return chatDAO.getUserIP(username,currentUser.getChatRoomId());
                }
                else{
                    return null;
                }

            }

        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeUserName", method = RequestMethod.POST)
    @ResponseBody
    public String changeUserName(String username,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && username != null && (!username.equals(""))){
                username = (username.replaceAll("\\s+",""));
                if ((!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) && currentUser.getRank().getChangeNick().equals("Y")) {
                    if(username.length() >= 3 && username.matches("^[a-zA-Z0-9_-]*$")) {
                        if(!chatDAO.checkUserNameExist(username,currentUser.getChatRoomId())){
                            return chatDAO.changeNick(currentUser.getId(), username, currentUser.getChatRoomId());
                        }
                    }
                }
                else{
                    return null;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeTweet", method = RequestMethod.POST)
    @ResponseBody
    public String changeTweet(String tweet,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && tweet != null && (!tweet.equals(""))){
                if ((!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST))) {
                        chatDAO.changeTweet(currentUser.getId(), tweet, currentUser.getChatRoomId());
                        currentUser.setTweet(tweet);
                        return setUserSession(currentUser);
                }
                else{
                    return "B";
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyPassword", method = RequestMethod.POST)
    @ResponseBody
    public String changeMyPassword(String password,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && password != null && (!password.equals(""))){
                if ((!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST))) {
                    chatDAO.changeMyPassword(currentUser.getId(), password, currentUser.getChatRoomId());
                    return "Y";
                }
                else{
                    return null;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyGender", method = RequestMethod.POST)
    @ResponseBody
    public String changeMyGender(String gender,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && gender != null && (!gender.equals(""))){
                chatDAO.changeMyGender(currentUser.getId(), gender, currentUser.getChatRoomId());
                currentUser.setGender(gender);
                return setUserSession(currentUser);
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyStatus", method = RequestMethod.POST)
    @ResponseBody
    public String changeMyStatus(String status,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && status != null && (!status.equals(""))){
                if(!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)){

                    if(status.equals("O") || status.equals("A") || status.equals("B") || status.equals("E") || status.equals("G") ){
                        chatDAO.changeMyStatus(currentUser.getId(), status, currentUser.getChatRoomId());
                        currentUser.setStatus(status);
                        return setUserSession(currentUser);
                    }
                    if(status.equals("R")){
                        if(currentUser.getRj().equals("Y")){
                            chatDAO.changeMyStatus(currentUser.getId(), status, currentUser.getChatRoomId());
                            currentUser.setStatus(status);
                            return  setUserSession(currentUser);
                        }
                    }
                    if(status.equals("S")){
                        if((!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) && (!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.MEMBER))){
                            chatDAO.changeMyStatus(currentUser.getId(), status, currentUser.getChatRoomId());
                            currentUser.setStatus(status);
                            return  setUserSession(currentUser);
                        }
                    }

                }

            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyAbout", method = RequestMethod.POST)
    @ResponseBody
    public String changeMyAbout(String about,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && about != null && (!about.equals(""))){
                if(!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) {
                    chatDAO.changeMyAbout(currentUser.getId(), about, currentUser.getChatRoomId());
                    currentUser.setAbout(about);
                    return setUserSession(currentUser);
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeRJ", method = RequestMethod.POST)
    @ResponseBody
    public String changeRJ(String value,Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && value != null && (!value.equals(""))){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) {
                    chatDAO.changeRJ(userId, value, currentUser.getChatRoomId());
                    sendUpdateRj(userId,value);
                    return "Y";
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeUserRank", method = RequestMethod.POST)
    @ResponseBody
    public String changeUserRank(Long value,Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && value != null && (!value.equals(""))){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) {
                    ChatRoomDTO chatRoomDTO = chatDAO.getChatRoomDataDTO(currentUser.getChatRoomId());
                    String action = chatDAO.changeUserRank(userId, value, chatRoomDTO);
                    if(action.equals("Y")){
                        sendReload(userId);
                        return "Y";
                    }
                    else{
                     return null;
                    }
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/getRankList", method = RequestMethod.POST)
    @ResponseBody
    public List<RankDTO> getRankList(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) {
                    ChatRoomDTO  chatRoomDTO =chatDAO.getChatRoomDataDTO(currentUser.getChatRoomId());
                    List<RankDTO> data =panelDAO.getRankList(chatRoomDTO);
                    return data;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyUserNameColor", method = RequestMethod.POST)
    @ResponseBody
    public String changeMyUserNameColor(String color,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && color != null && (!color.equals(""))){
                chatDAO.changeMyUserNameColor(currentUser.getId(), color, currentUser.getChatRoomId());
                currentUser.setNameColor(color);
                return setUserSession(currentUser);
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyWriting", method = RequestMethod.POST)
    @ResponseBody
    public String changeMyWriting(String bold,String color,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && color != null && (!color.equals("")) && bold != null && (!bold.equals(""))  ){
                chatDAO.changeMyWriting(currentUser.getId(), color, bold,currentUser.getChatRoomId());
                currentUser.setTextColor(color);
                currentUser.setBold(bold);
                return setUserSession(currentUser);
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeDOB", method = RequestMethod.POST)
    @ResponseBody
    public String changeDOB(Long dob, String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && dob != null ){
                Date birthday = new Date(dob);
                chatDAO.changeMyDOB(currentUser.getId(), birthday, currentUser.getChatRoomId());
                currentUser.setBirthDate(birthday);
                return setUserSession(currentUser);
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/manageUsers", method = RequestMethod.POST)
    @ResponseBody
    public DashboardDTO manageUsers(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                return chatDAO.getManageUsersData(currentUser.getChatRoomId());
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/manageips", method = RequestMethod.POST)
    @ResponseBody
    public List<IpBannedDTO> manageips (String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser.getRank().getBan().equals("Y")){
                return chatDAO.manageips(currentUser.getChatRoomId());
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/removeIp", method = RequestMethod.POST)
    @ResponseBody
    public String removeIp (Long id,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser.getRank().getBan().equals("Y")){
                chatDAO.removeIp(id,currentUser.getChatRoomId());
                return "Y";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/changeMyDP", method = RequestMethod.POST)
    @ResponseBody
    public DataDTO changeMyDP(String imageData, String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null && imageData != null ){
                if(!currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST)) {
                    String type = GetFileExtension(imageData);
                    if (type.equals("jpeg") || type.equals("png") || type.equals("PNG") || type.equals("JPEG") || type.equals("jpg") || type.equals("HEIC")) {
                        String encodedImg = imageData.split(",")[1];
                        byte[] decodedImg = Base64.getDecoder()
                                .decode(encodedImg.getBytes(StandardCharsets.UTF_8));
                        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                        String uploadFolder = getUploadFolder(GlobalConstants.BASE.UPLOAD);
                        String filename = currentUser.getId()+ date.getTime()+ "." + type;

                        String version = "?v=" + date.getTime();
                        String dp = GlobalConstants.BASE.UPLOADURL + filename;

                        BufferedOutputStream writter =
                                new BufferedOutputStream(new FileOutputStream(new File(uploadFolder + "/" + filename)));
                        writter.write(decodedImg);
                        writter.close();

                        chatDAO.changeMyDP(currentUser.getId(), dp, currentUser.getChatRoomId(),filename);
                        currentUser.setDp(dp);

                        try{
                            BufferedOutputStream writterBackup =
                                    new BufferedOutputStream(new FileOutputStream(new File(GlobalConstants.BASE.UPLOADBACKUP + "/" + filename)));
                            writterBackup.write(decodedImg);
                            writterBackup.close();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        DataDTO dataDTO = new DataDTO();
                        dataDTO.setToken(setUserSession(currentUser));
                        dataDTO.setValue(dp);

                        return dataDTO;
                    } else {
                        return null;
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/kickUser", method = RequestMethod.POST)
    @ResponseBody
    public String kickUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getKick().equals("Y") && userId != null){
                    if(usersListRepo.containsKey(currentUser.getChatRoomId())){
                        UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                        if(allowRank(currentUser,target)){
                            ChatUserDTO targetUser = usersListRepo.get(currentUser.getChatRoomId()).get(userId);
                            sendKick(targetUser.getUserId());
                            removeFromUserList(target);
                            sendLeave(target);
                            return "Y";
                        }

                    }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/spamUser", method = RequestMethod.POST)
    @ResponseBody
    public String spamUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getSpam().equals("Y") && userId != null){
                    //if(usersListRepo.containsKey(currentUser.getChatRoomId())){
                        UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                        if(allowRank(currentUser,target)){
                            chatDAO.spamUser(target.getId(),currentUser.getChatRoomId());
                            sendSpam(target.getId());
                            return "Y";
                        }
                   // }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/banUser", method = RequestMethod.POST)
    @ResponseBody
    public String banUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getBan().equals("Y") && userId != null && token != null){
                    UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                    if(allowRank(currentUser,target)){
                        chatDAO.banUser(userId,currentUser.getChatRoomId());
                        sendBan(userId);
                        removeFromUserList(target);
                        sendLeave(target);
                        return "Y";
                    }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/muteUser", method = RequestMethod.POST)
    @ResponseBody
    public String muteUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getMute().equals("Y") && userId != null && token != null){
                    UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                    if(allowRank(currentUser,target)){
                        chatDAO.muteUser(userId,currentUser.getChatRoomId());
                        sendMute(userId);
                        return "Y";
                    }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/updateSession", method = RequestMethod.POST)
    @ResponseBody
    public String updateSession(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            UserDTO userDTO = chatDAO.getUserById(currentUser.getId(),currentUser.getChatRoomId());
            return setUserSession(userDTO);
        }
        catch (Exception e){
            return "N";
        }
    }
    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    @ResponseBody
    public List<ChatUserDTO> findUser(String search,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null && search != null && (!search.equals(""))){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                   List<ChatUserDTO> data =  panelDAO.getUsersBySearch(search,currentUser.getChatRoomId());
                   return data;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/openUserProfile", method = RequestMethod.POST)
    @ResponseBody
    public ChatUserDTO openUserProfile(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            UserDTO data = chatDAO.getUserById(userId,currentUser.getChatRoomId());
            return  populateUser(data);
        }
        catch (Exception e){
            return null;
        }
    }
    @RequestMapping(value = "/showMutedUsers", method = RequestMethod.POST)
    @ResponseBody
    public List<ChatUserDTO> showMutedUsers(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null ){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                   List<ChatUserDTO> data =  panelDAO.showMutedUsers(currentUser.getChatRoomId());
                   return data;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/showBannedUsers", method = RequestMethod.POST)
    @ResponseBody
    public List<ChatUserDTO> showBannedUsers(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null ){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                   List<ChatUserDTO> data =  panelDAO.showBannedUsers(currentUser.getChatRoomId());
                   return data;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/getChatHistory", method = RequestMethod.POST)
    @ResponseBody
    public List<MainChatDTO> getChatHistory(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null ){
                return mainChatDataService.getChatHistory(currentUser.getChatRoomId());
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/showRJUsers", method = RequestMethod.POST)
    @ResponseBody
    public List<ChatUserDTO> showRJUsers(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null ){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                   List<ChatUserDTO> data =  panelDAO.showRJUsers(currentUser.getChatRoomId());
                   return data;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/showUsers", method = RequestMethod.POST)
    @ResponseBody
    public List<ChatUserDTO> showUsers(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null ){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                   List<ChatUserDTO> data =  panelDAO.showUsers(currentUser.getChatRoomId());
                   return data;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/openUser", method = RequestMethod.POST)
    @ResponseBody
    public ChatUserDTO openUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser != null && userId != null){
                if(currentUser.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)){
                    UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                    if(allowRank(currentUser,target)){
                        return populateUser(target);
                    }
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }
    @RequestMapping(value = "/unMuteUser", method = RequestMethod.POST)
    @ResponseBody
    public String unMuteUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getMute().equals("Y") && userId != null && token != null){
                    UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                    if(allowRank(currentUser,target)){
                        chatDAO.unMuteUser(userId,currentUser.getChatRoomId());
                        sendUnMute(userId);
                        return "Y";
                    }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/unBanUser", method = RequestMethod.POST)
    @ResponseBody
    public String unBanUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getBan().equals("Y") && userId != null && token != null){
                    UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                    if(allowRank(currentUser,target)){
                        chatDAO.unBanUser(userId,currentUser.getChatRoomId());
                        return "Y";
                    }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/unSpamUser", method = RequestMethod.POST)
    @ResponseBody
    public String unSpamUser(Long userId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getSpam().equals("Y") && userId != null && token != null){
                    UserDTO target = chatDAO.getUserById(userId,currentUser.getChatRoomId());
                    if(allowRank(currentUser,target)){
                        chatDAO.unSpamUser(userId,currentUser.getChatRoomId());
                        sendUnSpam(userId);
                        return "Y";
                    }

                }
                else{
                    return "N";
                }

            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }

    @RequestMapping(value = "/getMsgs", method = RequestMethod.POST)
    @ResponseBody
    public List<PrivateMessage> getMsgs( String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                List<PrivateMessage> data = mainChatDataService.getMsgs(currentUser.getChatRoomId(),currentUser.getId());
                return data;
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @RequestMapping(value = "/getPrivateData", method = RequestMethod.POST)
    @ResponseBody
    public List<PrivateMessage> getPrivateData(Long fromUserId, String token){
        if(fromUserId != null){
            try {
                UserDTO currentUser = getCurrentUser(token);
                if(currentUser !=null){
                    List<PrivateMessage> data = mainChatDataService.getPrivateData(currentUser.getChatRoomId(),currentUser.getId(),fromUserId);
                    return data;
                }

            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
    @RequestMapping(value = "/spamProtection", method = RequestMethod.POST)
    @ResponseBody
    public String spamProtection(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                chatDAO.spamProtection(currentUser.getId(),currentUser.getChatRoomId());
                currentUser.setStatus("M");
                return setUserSession(currentUser);
            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }
    @RequestMapping(value = "/kicked", method = RequestMethod.GET)
    public String kicked() throws IOException {

        return "chat/kick";
    }
    @RequestMapping(value = "/ban", method = RequestMethod.GET)
    public String ban() throws IOException {

        return "chat/ban";
    }

    @RequestMapping(value = "/clearAllChat", method = RequestMethod.POST)
    @ResponseBody
    public String clearAllChat(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
                if(currentUser.getRank().getDeleteMsg().equals("Y")){
                    clearChat(currentUser);
                    new Thread(() -> {
                        try {
                            mainChatDataService.deleteAllMainChat(currentUser.getChatRoomId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                }
            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }

    @RequestMapping(value = "/checkUnreadMsgs", method = RequestMethod.POST)
    @ResponseBody
    public Long checkUnreadMsgs(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
               return mainChatDataService.checkUnreadMsgs(currentUser.getChatRoomId(),currentUser.getId());
            }

        }
        catch (Exception e){
            return 0L;
        }
        return 0L;
    }

    @RequestMapping(value = "/seenMsgs", method = RequestMethod.POST)
    @ResponseBody
    public Long seenMsgs(Long fromUserId,String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){
               mainChatDataService.seenMsgs(currentUser.getChatRoomId(),currentUser.getId(),fromUserId);
            }

        }
        catch (Exception e){
            return 0L;
        }
        return 0L;
    }

    @RequestMapping(value = "/clearAllMsgs", method = RequestMethod.POST)
    @ResponseBody
    public String clearAllMsgs(String token){
        try {
            UserDTO currentUser = getCurrentUser(token);
            if(currentUser !=null){

                    new Thread(() -> {
                        try {
                            mainChatDataService.deleteAllPrivateChat(currentUser.getChatRoomId(),currentUser.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    return "Y";
            }

        }
        catch (Exception e){
            return "N";
        }
        return "N";
    }

}
