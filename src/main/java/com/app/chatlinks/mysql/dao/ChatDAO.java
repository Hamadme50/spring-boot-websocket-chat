package com.app.chatlinks.mysql.dao;

import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.config.SendMailSSL;
import com.app.chatlinks.dto.ChatRoomDTO;
import com.app.chatlinks.dto.IpBannedDTO;
import com.app.chatlinks.dto.UserDTO;
import com.app.chatlinks.dto.chat.*;
import com.app.chatlinks.dto.panel.DashboardDTO;
import com.app.chatlinks.mapper.ChatRoomMapper;
import com.app.chatlinks.mapper.IpBannedMapper;
import com.app.chatlinks.mapper.UserMapper;
import com.app.chatlinks.mapper.chat.MainChatMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;
import com.app.chatlinks.mysql.model.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.app.chatlinks.websockets.ChatEngine.usersListRepo;

@Repository
@Transactional
public class ChatDAO extends BaseDAO{
    @PersistenceContext
    EntityManager entityManager;

    public GetChatDTO getChatRoomById(Long id){
        GetChatDTO chatLoginDTO = null;
        Query query = entityManager.createQuery("select c.name, c.topic, c.theme from ChatRoom c where c.id=:chatroomId");
        query.setParameter("chatroomId", id);
        Object[] data = (Object[]) query.getSingleResult();
        chatLoginDTO = new GetChatDTO();
        chatLoginDTO.setName((String) data[0]);
        chatLoginDTO.setTopic((String) data[1]);
        chatLoginDTO.setTheme((String) data[2]);
        return chatLoginDTO;

    }
    public void saveMainChat(ChatMessage chatMessage){
        MainChat chat = new MainChat();
        chat.setChatId(chatMessage.getId());
        chat.setContent(chatMessage.getContent());
        chat.setUserName(chatMessage.getUserName());
        chat.setUserId(chatMessage.getUserId());
        chat.setChatRoomId(chatMessage.getChatRoomId());
        chat.setNameColor(chatMessage.getNameColor());
        chat.setTextColor(chatMessage.getTextColor());
        chat.setBold(chatMessage.getBold());
        chat.setFont(chatMessage.getFont());
        chat.setUserStatus(chatMessage.getUserStatus());
        chat.setMsgDate(chatMessage.getMsgDate());
        chat.setType(chatMessage.getType().toString());
        chat.setDp(chatMessage.getDp());
        entityManager.merge(chat);
        entityManager.flush();
    }

    public List<MainChatDTO> getChatHistory(Long chatRoomId){
        Query query = entityManager.createQuery("select a from MainChat a where a.chatRoomId=:chatRoom order by a.id desc");
        query.setParameter("chatRoom",chatRoomId);
        query.setMaxResults(70);
        List<MainChat> data =  query.getResultList();
        MainChatMapper mapper = new MainChatMapper();
        List<MainChatDTO> chat = mapper.mapToDTOList(data);

        return chat;
    }

    public void deleteMainChat(Long chatRoomid,String id){
        Query deleteQuery = entityManager.createQuery("delete from MainChat d where d.chatId=:id and d.chatRoomId=:chatRoomid");
        deleteQuery.setParameter("id",id);
        deleteQuery.setParameter("chatRoomid" ,chatRoomid);
        deleteQuery.executeUpdate();
    }
    public void deleteAllMainChat(Long chatRoomid){
        Query deleteQuery = entityManager.createQuery("delete from MainChat d where d.chatRoomId=:chatRoomid");
        deleteQuery.setParameter("chatRoomid" ,chatRoomid);
        deleteQuery.executeUpdate();
    }

    public void clearChatHistory(){
        Query query = entityManager.createQuery("select c.id from ChatRoom c");
        List<Long> chatRoomIds =  query.getResultList();
        if(chatRoomIds != null && chatRoomIds.size() != 0){
            for (Long id : chatRoomIds){
                Query latestQuery = entityManager.createQuery("select a.id from MainChat a where a.chatRoomId=:id order by a.id desc");
                latestQuery.setParameter("id" ,id);
                latestQuery.setMaxResults(70);
                List<Long> latestRecords =  latestQuery.getResultList();
                if(latestRecords != null && latestRecords.size() != 0){
                    Query deleteQuery = entityManager.createQuery("delete from MainChat d where d.chatRoomId=:id and d.id not IN (:latestRecords)");
                    deleteQuery.setParameter("latestRecords",latestRecords);
                    deleteQuery.setParameter("id" ,id);
                    deleteQuery.executeUpdate();
                }

            }
        }

    }

    public ChatRoomDTO getChatRoomDataDTO(Long id){
        Query query = entityManager.createQuery("select c from ChatRoom c where c.id=:chatroomId");
        query.setParameter("chatroomId", id);
        ChatRoom chatRoom = (ChatRoom) query.getSingleResult();

        ChatRoomMapper chatRoomMapper = new ChatRoomMapper();
        ChatRoomDTO data  = chatRoomMapper.mapToDTO(chatRoom);
        return data;

    }

    public ChatRoom getChatRoomData(Long id){
        Query query = entityManager.createQuery("select c from ChatRoom c where c.id=:chatroomId");
        query.setParameter("chatroomId", id);
        ChatRoom data = (ChatRoom) query.getSingleResult();
        return data;

    }

    public void expireGuest(Long userId){
        Query query = entityManager.createQuery("select u from User u where u.id=:aUserId");
        query.setParameter("aUserId", userId);
        User user = (User) query.getSingleResult();
        if(!user.getStatus().equals(GlobalConstants.USER_STATUS.DELETE)){
            user.setUserName(user.getId()+"-"+user.getUserName());
            user.setStatus(GlobalConstants.USER_STATUS.DELETE);
            entityManager.merge(user);
            entityManager.flush();
        }
    }
    public void clearGuest(){
        Query query = entityManager.createQuery("delete from User u where u.status=:aStatus");
        query.setParameter("aStatus", GlobalConstants.USER_STATUS.DELETE);
        query.executeUpdate();

    }

    public void clearSession(){
        List<Long> clearSessionUsers = new ArrayList<>();
        for (Map.Entry<Long, ConcurrentHashMap<Long, ChatUserDTO>> entry : usersListRepo.entrySet()) {
            for (Map.Entry<Long, ChatUserDTO> userList : entry.getValue().entrySet()) {
                ChatUserDTO user =  userList.getValue();
                clearSessionUsers.add(user.getId());
            }
        }
        if(clearSessionUsers.size() != 0){
            try {
                Query query = entityManager.createQuery("update User u set u.session=null where u.id not IN(:clearUsers) and u.rank.code !=:rank");
                query.setParameter("clearUsers", clearSessionUsers);
                query.setParameter("aRank", GlobalConstants.USER_RANKS.GUEST);
                query.executeUpdate();
            }
            catch (Exception e){

            }

        }
    }

    public void clearUser(){
        LocalDate dateBefore =  LocalDate.now().minusDays(30);
        java.sql.Date date = java.sql.Date.valueOf( dateBefore );
        Query query = entityManager.createQuery("select u from User u where u.activityDate <:aStatus");
        query.setParameter("aStatus", date);
        List<User> users = query.getResultList();
        for(User user : users){

            if(user.getOwner().equals("N") && (!user.getRank().getCode().equals(GlobalConstants.USER_RANKS.GUEST))){
                try{
                    Path fileToDeletePath = Paths.get(GlobalConstants.BASE.DELTEUPLOAD + "\\" + user.getFile());
                    Files.delete(fileToDeletePath);

                    Path fileToDeletePathBackup = Paths.get(GlobalConstants.BASE.UPLOADBACKUP + "\\" + user.getFile());
                    Files.delete(fileToDeletePathBackup);

                    entityManager.remove(user);

                }
                catch (Exception e){
                }
            }

        }

    }

    public Rank getRankData(String code, Long chatRoomId){
        Query query = entityManager.createQuery("select r from Rank r where r.chatRoom.id=:chatroomId and r.code=:rank");
        query.setParameter("chatroomId", chatRoomId);
        query.setParameter("rank", code);
        Rank data = (Rank) query.getSingleResult();
        return data;

    }
    public Rank getRankDataBtId(Long id, Long chatRoomId){
        Query query = entityManager.createQuery("select r from Rank r where r.chatRoom.id=:chatroomId and r.id=:rank");
        query.setParameter("chatroomId", chatRoomId);
        query.setParameter("rank", id);
        Rank data = (Rank) query.getSingleResult();
        return data;

    }

    public UserDTO getLogin(String username, String password,String ip, Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.userName=:aUserName and (u.password=:aPassword OR u.tempPassword=:aPassword) and u.chatRoom.id=:id");
            query.setParameter("aUserName", username);
            query.setParameter("aPassword", encodePassword(password));
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            user.setActivityDate(date);
            user.setTempPassword(null);
            user.setIp(ip);
            entityManager.merge(user);
            UserMapper mapper = new UserMapper();
            UserDTO data = mapper.mapToDTO(user);
            entityManager.flush();
            return data;
        }
        catch (Exception e){
            return null;
        }

    }
    public UserDTO getUserById(Long id, Long chatRoomId){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:chatroom");
            query.setParameter("userId", id);
            query.setParameter("chatroom", chatRoomId);
            User user = (User) query.getSingleResult();
            UserMapper mapper = new UserMapper();
            UserDTO data = mapper.mapToDTO(user);
            return data;
        }
        catch (Exception e){
            return null;
        }

    }
    public void RecoverUserPassword(String email, Long chatRoomId){
        try {
            Query query = entityManager.createQuery("select u from User u where u.email=:aEmail and u.chatRoom.id=:chatroom");
            query.setParameter("aEmail", email);
            query.setParameter("chatroom", chatRoomId);
            User user = (User) query.getSingleResult();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String emailDate = dtf.format(localDate);
            String password  = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

            SendMailSSL mail = new SendMailSSL();
            String msg = "Your temporary password : "+password+ "\n"+
                    "Use this password to login your account after that please change your password from your profile.\n"+
                    "If you didn't request forgot password just ignore this email.";
            mail.sendMail(user.getEmail(),GlobalConstants.EMAIL.SUBJECT+user.getUserName()+" - "+emailDate,msg);

            user.setTempPassword(encodePassword(password));

            entityManager.merge(user);
            entityManager.flush();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public String getUserIP(String username,  Long id){
        try {
            Query query = entityManager.createQuery("select u.ip from User u where u.userName=:aUserName and u.chatRoom.id=:id");
            query.setParameter("aUserName", username);
            query.setParameter("id", id);
            String ip = (String) query.getSingleResult();
            return ip;
        }
        catch (Exception e){
            return null;
        }

    }

    public boolean checkIpBanned(String ip,Long chatRoomId){
        try {
            Query query = entityManager.createQuery("select count (u) from IpBanned u where u.chatRoom.id=:id and u.ip=:aIp");
            query.setParameter("aIp", ip);
            query.setParameter("id", chatRoomId);
            Long ipCount = (Long) query.getSingleResult();
            if(ipCount != null && ipCount.equals(0L)){
                return false;
            }
            else{
                return true;
            }
        }
        catch (Exception e){
            return false;
        }

    }
    public boolean checkSessionAllow(Long id){
        Query query = entityManager.createQuery("select count(u.id) from User u where u.id=:aid and u.session is null");
        query.setParameter("aid", id);
        Long count = (Long) query.getSingleResult();

        if(count.equals(0L)){
            return false;
        }
        else{
            return true;
        }
    }
    public void setUserSessionDb(Long userId,String sessionId){
        Query query = entityManager.createQuery("select u from User u  where u.id=:userId");
        query.setParameter("userId", userId);
        User user = (User) query.getSingleResult();
        user.setSession(sessionId);
        entityManager.merge(user);
        entityManager.flush();
    }

    public void removeUserSessionDb(Long userId){
        Query query = entityManager.createQuery("select u from User u  where u.id=:userId");
        query.setParameter("userId", userId);
        User user = (User) query.getSingleResult();
        user.setSession(null);
        entityManager.merge(user);
        entityManager.flush();
    }
    public void removeAllUserSessionDb(){
        Query query = entityManager.createQuery("update User u set u.session =null");
        query.executeUpdate();
    }

    public UserDTO getUserBySession(String session){
        try {
            Query query = entityManager.createQuery("select u from User u where u.session=:aSession");
            query.setParameter("aSession", session);
            User user = (User) query.getSingleResult();
            UserMapper mapper = new UserMapper();
            UserDTO data = mapper.mapToDTO(user);
            return data;
        }
        catch (Exception e){
            return null;
        }

    }

    public void updateBan(String usernme,Long chatRoomId, String ip){
        IpBanned  ipBanned = new IpBanned();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(chatRoomId);
        ipBanned.setChatRoom(chatRoom);
        ipBanned.setIp(ip);
        ipBanned.setUsername(usernme);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        ipBanned.setDate(date);
        entityManager.merge(ipBanned);
        entityManager.flush();
    }
    public String banUser(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            if( !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.ADMIN)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.MOD)) ){
                user.setBan("Y");
                String ip = user.getIp();
                entityManager.merge(user);
                updateBan(user.getUserName(),user.getChatRoom().getId(),ip);
                return "Y";
            }
            else{
                return "N";
            }
        }
        catch (Exception e){
            return "N";
        }

    }
    public String spamUser(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            if( !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.ADMIN)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.MOD)) ){
                user.setSpam("Y");
                entityManager.merge(user);
                entityManager.flush();
                return "Y";
            }
            else{
                return "N";
            }
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeNick(Long userId, String username ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setUserName(username);
            entityManager.merge(user);
            entityManager.flush();

            return "Y";
        }
        catch (Exception e){
            return null;
        }

    }
    public String changeTweet(Long userId, String tweet ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setTweet(tweet);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeMyPassword(Long userId, String password ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setPassword(encodePassword(password));
            user.setTempPassword(null);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return null;
        }

    }
    public String changeMyAbout(Long userId, String about ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setAbout(about);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeRJ(Long userId, String value,Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setRj(value);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeUserRank(Long userId, Long value,ChatRoomDTO chatRoomDTO){
        try {
            Rank rank = getRankDataBtId(value,chatRoomDTO.getId());
            if(rank.getCode().equals(GlobalConstants.USER_RANKS.CUSTOM)){
                if(chatRoomDTO.getPaid().equals("N")){
                    return "N";
                }
            }

            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id and u.owner =:aOwner");
            query.setParameter("userId", userId);
            query.setParameter("id", chatRoomDTO.getId());
            query.setParameter("aOwner", "N");
            User user = (User) query.getSingleResult();
            user.setRank(rank);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeMyWriting(Long userId, String color ,String bold,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setTextColor(color);
            user.setBold(bold);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeMyUserNameColor(Long userId, String color ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setNameColor(color);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeMyStatus(Long userId, String status ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setStatus(status);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }public String changeMyGender(Long userId, String gender ,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setGender(gender);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public List<IpBannedDTO> manageips (Long chatRoomId){
        List<IpBannedDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select b from IpBanned b where b.chatRoom.id =:aCustomerId");
        query.setParameter("aCustomerId",chatRoomId);
        List<IpBanned> ips =  query.getResultList();
        IpBannedMapper mapper = new IpBannedMapper();
        data = mapper.mapToDTOList(ips);
        return data;
    }
    public void removeIp (Long id, Long chatRoomId){
        Query query = entityManager.createQuery("delete from IpBanned b where b.id=:aId and b.chatRoom.id =:aCustomerId");
        query.setParameter("aId",id);
        query.setParameter("aCustomerId",chatRoomId);
        query.executeUpdate();
    }

    public DashboardDTO getManageUsersData(Long chatRoomId){
        Query queryUsers = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId");
        queryUsers.setParameter("aCustomerId",chatRoomId);
        Long users = (Long) queryUsers.getSingleResult();

        Query queryAdmins = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId and u.rank.code=:aRank");
        queryAdmins.setParameter("aCustomerId",chatRoomId);
        queryAdmins.setParameter("aRank", GlobalConstants.USER_RANKS.ADMIN);
        Long admins = (Long) queryAdmins.getSingleResult();

        Query queryMods = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId and u.rank.code=:aRank");
        queryMods.setParameter("aCustomerId",chatRoomId);
        queryMods.setParameter("aRank",GlobalConstants.USER_RANKS.MOD);
        Long mods = (Long) queryMods.getSingleResult();

        Query queryCustom = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId and u.rank.code=:aRank");
        queryCustom.setParameter("aCustomerId",chatRoomId);
        queryCustom.setParameter("aRank",GlobalConstants.USER_RANKS.CUSTOM);
        Long customs = (Long) queryCustom.getSingleResult();

        Query queryBanned = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId and u.ban=:aStatus");
        queryBanned.setParameter("aCustomerId",chatRoomId);
        queryBanned.setParameter("aStatus","Y");
        Long bannd = (Long) queryBanned.getSingleResult();

        Query queryRjs = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId and u.rj=:aRj");
        queryRjs.setParameter("aCustomerId",chatRoomId);
        queryRjs.setParameter("aRj","Y");
        Long rjs = (Long) queryRjs.getSingleResult();

        Query queryMute = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.id=:aCustomerId and u.status=:aMute");
        queryMute.setParameter("aCustomerId",chatRoomId);
        queryMute.setParameter("aMute","M");
        Long mutes = (Long) queryMute.getSingleResult();

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setUsers(users);
        dashboardDTO.setAdmins(admins);
        dashboardDTO.setMods(mods);
        dashboardDTO.setCustoms(customs);
        dashboardDTO.setBanned(bannd);
        dashboardDTO.setRj(rjs);
        dashboardDTO.setMutes(mutes);

        return dashboardDTO;
    }
    public String changeMyDOB(Long userId, Date dob , Long id){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setBirthDate(dob);
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String changeMyDP(Long userId, String dp , Long id,String file){
        try {
            Query query = entityManager.createQuery("select u from User u where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();



            try{
                if(user.getFile() != null) {
                    Path fileToDeletePath = Paths.get(GlobalConstants.BASE.DELTEUPLOAD + "\\" + user.getFile());
                    Files.delete(fileToDeletePath);

                    Path fileToDeletePathBackup = Paths.get(GlobalConstants.BASE.UPLOADBACKUP + "\\" + user.getFile());
                    Files.delete(fileToDeletePathBackup);

                }

            }
            catch (Exception e){

            }





            user.setDp(dp);
            user.setFile(file);
            entityManager.merge(user);
            entityManager.flush();

            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String muteUser(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            if( !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.ADMIN)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.MOD)) ){
                user.setStatus("M");
                entityManager.merge(user);
                entityManager.flush();
                return "Y";
            }
            else{
                return "N";
            }
        }
        catch (Exception e){
            return "N";
        }

    }
    public String spamProtection(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setStatus("M");
            entityManager.merge(user);
            entityManager.flush();
            return "Y";
        }
        catch (Exception e){
            return "N";
        }

    }
    public String unMuteUser(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            if( !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.ADMIN)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.MOD)) ){
                user.setStatus("O");
                entityManager.merge(user);
                entityManager.flush();
                return "Y";
            }
            else{
                return "N";
            }
        }
        catch (Exception e){
            return "N";
        }

    }
    public String unBanUser(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            user.setBan("N");
            entityManager.merge(user);

            Query ipbanned = entityManager.createQuery("delete from IpBanned b where b.chatRoom.id=:id and b.ip=:aIp");
            ipbanned.setParameter("id", id);
            ipbanned.setParameter("aIp", user.getIp());
            ipbanned.executeUpdate();
            entityManager.flush();
            return "Y";

        }
        catch (Exception e){
            return "N";
        }

    }
    public String unSpamUser(Long userId,  Long id){
        try {
            Query query = entityManager.createQuery("select u from User u left join fetch u.rank where u.id=:userId and u.chatRoom.id=:id");
            query.setParameter("userId", userId);
            query.setParameter("id", id);
            User user = (User) query.getSingleResult();
            if( !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.OWNER)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.ADMIN)) &&  !(user.getRank().getCode().equals(GlobalConstants.USER_RANKS.MOD)) ){
                user.setSpam("N");
                entityManager.merge(user);
                entityManager.flush();
                return "Y";
            }
            else{
                return "N";
            }
        }
        catch (Exception e){
            return "N";
        }

    }

    public UserDTO getGuestLogin(String username,String ip,Long id){
        try {
            ChatRoom chatRoom = getChatRoomData(id);
            if(chatRoom.getGuest().equals("N")){
                return null;
            }

            User guest = new User();
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            guest.setChatRoom(chatRoom);
            guest.setRank(getRankData(GlobalConstants.USER_RANKS.GUEST,id));
            guest.setUserName(username);
            guest.setPassword(encodePassword(GlobalConstants.BASE.GUESTPASSWORD));
            guest.setJoinDate(date);
            guest.setStatus(GlobalConstants.USER_STATUS.ONLINE);
            guest.setActivityDate(date);
            guest.setBirthDate(date);
            guest.setGender("N");
            guest.setDp("/content/default/guestdp.png");
            guest.setRj("N");
            guest.setIp(ip);
            guest.setOwner("N");
            if(chatRoom.getStatus().equals("S")){
                guest.setSpam("Y");
            }
            else{
                guest.setSpam("N");
            }
            guest.setBan("N");
            guest.setTweet("");
            guest.setAbout("");
            guest.setNameColor("nickcolor");
            guest.setKick("N");
            guest.setEmail("guest@chatlinks.net");
            guest.setPoints(0.0);
            guest.setBold("N");
            guest = entityManager.merge(guest);
            entityManager.flush();
            UserMapper mapper = new UserMapper();
            UserDTO data =  mapper.mapToDTO(guest);

            return data;
        }
        catch (Exception e){
            return null;
        }

    }

    public boolean checkUserNameExist(String username,Long id){
        Query query = entityManager.createQuery("select count(u.userName) from User u where LOWER(u.userName)=:aUserName and u.chatRoom.id=:id");
        query.setParameter("aUserName", username.toLowerCase());
        query.setParameter("id", id);
        Long count = (Long) query.getSingleResult();

        if(count.equals(0L)){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean checkEmailExist(String email,Long id){
        Query query = entityManager.createQuery("select count(u.userName) from User u where LOWER(u.email)=:aEmail and u.chatRoom.id=:id");
        query.setParameter("aEmail", email.toLowerCase());
        query.setParameter("id", id);
        Long count = (Long) query.getSingleResult();

        if(count.equals(0L)){
            return false;
        }
        else{
            return true;
        }
    }

    public UserDTO registerNewUser(String username,String password,String email,String ip,Long id){
        try {
            ChatRoom chatRoom = getChatRoomData(id);
            if(chatRoom.getRegister().equals("N")){
                return null;
            }

            User member = new User();
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            member.setChatRoom(chatRoom);
            member.setRank(getRankData(GlobalConstants.USER_RANKS.MEMBER,id));
            member.setUserName(username);
            member.setPassword(encodePassword(password));
            member.setJoinDate(date);
            member.setStatus(GlobalConstants.USER_STATUS.ONLINE);
            member.setActivityDate(date);
            member.setBirthDate(date);
            member.setGender("N");
            member.setDp("/content/default/user.png");
            member.setRj("N");
            member.setOwner("N");
            member.setIp(ip);
            if(chatRoom.getStatus().equals("S")){
                member.setSpam("Y");
            }
            else{
                member.setSpam("N");
            }
            member.setBan("N");
            member.setTweet("");
            member.setAbout("");
            member.setNameColor("nickcolor");
            member.setKick("N");
            member.setEmail(email);
            member.setPoints(0.0);
            member.setBold("N");
            member = entityManager.merge(member);
            entityManager.flush();
            UserMapper mapper = new UserMapper();
            UserDTO data =  mapper.mapToDTO(member);

            return data;
        }
        catch (Exception e){
            return null;
        }

    }



}
