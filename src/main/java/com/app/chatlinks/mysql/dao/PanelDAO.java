package com.app.chatlinks.mysql.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.app.chatlinks.config.GlobalConstants;
import com.app.chatlinks.config.SendMailSSL;
import com.app.chatlinks.dto.ChatRoomDTO;
import com.app.chatlinks.dto.CustomerDTO;
import com.app.chatlinks.dto.RankDTO;
import com.app.chatlinks.dto.chat.ChatUserDTO;
import com.app.chatlinks.dto.panel.DashboardDTO;
import com.app.chatlinks.mapper.ChatRoomMapper;
import com.app.chatlinks.mapper.CustomerMapper;
import com.app.chatlinks.mapper.RankMapper;
import com.app.chatlinks.mysql.model.ChatRoom;
import com.app.chatlinks.mysql.model.Customer;
import com.app.chatlinks.mysql.model.Rank;
import com.app.chatlinks.mysql.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
@Transactional
public class PanelDAO extends BaseDAO{
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private CacheManager cacheManager;

    public CustomerDTO loginCustomer(CustomerDTO customerDTO)  throws Exception{
        Query query =  entityManager.createQuery("select c from Customer c where c.email=:aEmail and (c.password=:aPassword OR c.tempPassword=:aPassword)");
        query.setParameter("aEmail",customerDTO.getEmail());
        query.setParameter("aPassword",encodePassword(customerDTO.getPassword()));

        Customer customer = (Customer) query.getSingleResult();
        customer.setTempPassword(null);
        customer = entityManager.merge(customer);
        CustomerMapper mapper = new CustomerMapper();
        CustomerDTO data = mapper.mapToDTO(customer);
        entityManager.flush();
        return data;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) throws Exception {
        CustomerDTO data = new CustomerDTO();
        Query query =  entityManager.createQuery("select c from Customer c where c.email=:aEmail");
        query.setParameter("aEmail",customerDTO.getEmail());
        List<Customer> alreadyCustomer = query.getResultList();
        if(alreadyCustomer.size() != 0){
            data.setRequestStatus(1);
            return data;
        }
        CustomerMapper mapper = new CustomerMapper();
        Customer customer = mapper.mapToModel(customerDTO);
        customer.setOwner("N");
        customer.setPassword(encodePassword(customerDTO.getPassword()));
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        customer.setJoinDadte(date);
        customer = entityManager.merge(customer);
        entityManager.flush();
        data = mapper.mapToDTO(customer);

        return data;
    }
    public Long newChatRoom(ChatRoomDTO chatRoomDTO,String email){
        try {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setCustomer(new Customer(chatRoomDTO.getCustomerId()));
            chatRoom.setDomain(chatRoomDTO.getDomain());
            chatRoom.setName(chatRoomDTO.getName());
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            chatRoom.setDate(date);
            chatRoom.setAntiSpam("N");
            chatRoom.setTheme("#006e99");
            chatRoom.setStatus("A");
            chatRoom.setPaid("N");
            chatRoom.setDesign("M");
            chatRoom.setGuest("Y");
            chatRoom.setRegister("Y");
            chatRoom = entityManager.merge(chatRoom);


            try{
                cacheManager.getCache("getChatRoomById").evict(chatRoom.getId());
            }
            catch (Exception e){
                e.printStackTrace();
            }


            Rank owner = new Rank();
            owner.setChatRoom(chatRoom);
            owner.setName("Owner");
            owner.setCode(GlobalConstants.USER_RANKS.OWNER);
            owner.setIcon("/content/default/owner.png");
            owner.setKick("Y");
            owner.setBan("Y");
            owner.setSpam("Y");
            owner.setMute("Y");
            owner.setChangeNick("Y");
            owner.setDeleteMsg("Y");
            Rank ownerRank = entityManager.merge(owner);

            Rank custom1 = new Rank();
            custom1.setChatRoom(chatRoom);
            custom1.setName("Custom Rank 1 Above From Admin");
            custom1.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom1.setIcon("/content/default/custom.png");
            custom1.setKick("Y");
            custom1.setBan("Y");
            custom1.setSpam("Y");
            custom1.setChangeNick("Y");
            custom1.setMute("Y");
            custom1.setDeleteMsg("Y");
            entityManager.merge(custom1);

            Rank custom2 = new Rank();
            custom2.setChatRoom(chatRoom);
            custom2.setName("Custom Rank 2 above from Admin");
            custom2.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom2.setIcon("/content/default/custom.png");
            custom2.setKick("Y");
            custom2.setBan("Y");
            custom2.setSpam("Y");
            custom2.setChangeNick("Y");
            custom2.setMute("Y");
            custom2.setDeleteMsg("Y");
            entityManager.merge(custom2);

            Rank admin = new Rank();
            admin.setChatRoom(chatRoom);
            admin.setName("Admin");
            admin.setCode(GlobalConstants.USER_RANKS.ADMIN);
            admin.setIcon("/content/default/admin.png");
            admin.setKick("Y");
            admin.setBan("Y");
            admin.setSpam("Y");
            admin.setMute("Y");
            admin.setChangeNick("Y");
            admin.setDeleteMsg("Y");
            entityManager.merge(admin);

            Rank custom3 = new Rank();
            custom3.setChatRoom(chatRoom);
            custom3.setName("Custom Rank 3 above from Mod");
            custom3.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom3.setIcon("/content/default/custom.png");
            custom3.setKick("Y");
            custom3.setBan("N");
            custom3.setChangeNick("Y");
            custom3.setSpam("Y");
            custom3.setMute("Y");
            custom3.setDeleteMsg("Y");
            entityManager.merge(custom3);

            Rank custom4 = new Rank();
            custom4.setChatRoom(chatRoom);
            custom4.setName("Custom Rank 4 above from Mod");
            custom4.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom4.setIcon("/content/default/custom.png");
            custom4.setKick("Y");
            custom4.setBan("N");
            custom4.setSpam("Y");
            custom4.setMute("Y");
            custom4.setDeleteMsg("Y");
            custom4.setChangeNick("Y");
            entityManager.merge(custom4);

            Rank mod = new Rank();
            mod.setChatRoom(chatRoom);
            mod.setName("Moderator");
            mod.setCode(GlobalConstants.USER_RANKS.MOD);
            mod.setIcon("/content/default/mod.png");
            mod.setKick("Y");
            mod.setBan("N");
            mod.setSpam("Y");
            mod.setChangeNick("Y");
            mod.setMute("Y");
            mod.setDeleteMsg("N");
            entityManager.merge(mod);

            Rank custom5 = new Rank();
            custom5.setChatRoom(chatRoom);
            custom5.setName("Custom Rank 5");
            custom5.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom5.setIcon("/content/default/custom.png");
            custom5.setKick("N");
            custom5.setBan("N");
            custom5.setSpam("N");
            custom5.setChangeNick("Y");
            custom5.setMute("N");
            custom5.setDeleteMsg("N");
            entityManager.merge(custom5);

            Rank custom6 = new Rank();
            custom6.setChatRoom(chatRoom);
            custom6.setName("Custom Rank 6");
            custom6.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom6.setIcon("/content/default/custom.png");
            custom6.setKick("N");
            custom6.setBan("N");
            custom6.setSpam("N");
            custom6.setChangeNick("Y");
            custom6.setMute("N");
            custom6.setDeleteMsg("N");
            entityManager.merge(custom6);

            Rank custom7 = new Rank();
            custom7.setChatRoom(chatRoom);
            custom7.setName("Custom Rank 7");
            custom7.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom7.setIcon("/content/default/custom.png");
            custom7.setKick("N");
            custom7.setBan("N");
            custom7.setSpam("N");
            custom7.setChangeNick("Y");
            custom7.setMute("N");
            custom7.setDeleteMsg("N");
            entityManager.merge(custom7);

            Rank custom8 = new Rank();
            custom8.setChatRoom(chatRoom);
            custom8.setName("Custom Rank 8");
            custom8.setCode(GlobalConstants.USER_RANKS.CUSTOM);
            custom8.setIcon("/content/default/custom.png");
            custom8.setKick("N");
            custom8.setBan("N");
            custom8.setSpam("N");
            custom8.setChangeNick("Y");
            custom8.setMute("N");
            custom8.setDeleteMsg("N");
            entityManager.merge(custom8);

            Rank star = new Rank();
            star.setChatRoom(chatRoom);
            star.setName("Star");
            star.setCode(GlobalConstants.USER_RANKS.STAR);
            star.setIcon("/content/default/star.png");
            star.setKick("N");
            star.setBan("N");
            star.setSpam("N");
            star.setChangeNick("Y");
            star.setMute("N");
            star.setDeleteMsg("N");
            entityManager.merge(star);


            Rank vip = new Rank();
            vip.setChatRoom(chatRoom);
            vip.setName("Vip");
            vip.setCode(GlobalConstants.USER_RANKS.VIP);
            vip.setIcon("/content/default/vip.png");
            vip.setKick("N");
            vip.setBan("N");
            vip.setSpam("N");
            vip.setChangeNick("Y");
            vip.setMute("N");
            vip.setDeleteMsg("N");
            entityManager.merge(vip);

            Rank member = new Rank();
            member.setChatRoom(chatRoom);
            member.setName("Member");
            member.setCode(GlobalConstants.USER_RANKS.MEMBER);
            member.setIcon("/content/default/member.png");
            member.setKick("N");
            member.setBan("N");
            member.setSpam("N");
            member.setChangeNick("N");
            member.setMute("N");
            member.setDeleteMsg("N");
            entityManager.merge(member);

            Rank guest = new Rank();
            guest.setChatRoom(chatRoom);
            guest.setName("Guest");
            guest.setCode(GlobalConstants.USER_RANKS.GUEST);
            guest.setIcon("/content/default/paw.png");
            guest.setKick("N");
            guest.setBan("N");
            guest.setChangeNick("N");
            guest.setSpam("N");
            guest.setMute("N");
            guest.setDeleteMsg("N");
            entityManager.merge(guest);


            User user = new User();
            user.setChatRoom(chatRoom);
            user.setRank(ownerRank);
            user.setUserName(chatRoomDTO.getNickname());
            user.setPassword(encodePassword(chatRoomDTO.getPassword()));
            user.setJoinDate(date);
            user.setStatus(GlobalConstants.USER_STATUS.ONLINE);
            user.setActivityDate(date);
            user.setBirthDate(date);
            user.setGender("N");
            user.setDp("/content/default/nick.png");
            user.setRj("Y");
            user.setIp("192.168.0.1");
            user.setSpam("N");
            user.setBan("N");
            user.setTweet("");
            user.setAbout("");
            user.setNameColor("nickcolor");
            user.setKick("N");
            user.setKick("N");
            user.setBold("N");
            user.setEmail(email);
            user.setPoints(0.0);
            user.setOwner("Y");
            user.setSession("Y");
            user.setTempPassword(null);
            entityManager.merge(user);
            entityManager.flush();

            return chatRoom.getId();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Long getMyChatRoom(Long customerId){
        Query query = entityManager.createQuery("select c.id from ChatRoom c where c.customer.id=:aCustomer");
        query.setParameter("aCustomer" ,customerId);
        Long chatRoom = (Long) query.getSingleResult();
        return chatRoom;
    }
    public boolean isDuplicateChatRoom (Long customerId){
        Query query = entityManager.createQuery("select count(c.id) from ChatRoom c where c.customer.id=:aCustomer");
        query.setParameter("aCustomer" ,customerId);
        Long chatRoom = (Long) query.getSingleResult();
        if(chatRoom > 1){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean checkCustomerEmailExist(String email){
        Query query = entityManager.createQuery("select count(u.email) from Customer u where u.email=:aEmail");
        query.setParameter("aEmail", email);
        Long count = (Long) query.getSingleResult();

        if(count.equals(0L)){
            return false;
        }
        else{
            return true;
        }
    }
    public void recoverCustomerPass(String email) throws NoSuchAlgorithmException {
            Query query = entityManager.createQuery("select c from Customer c where c.email=:aEmail");
            query.setParameter("aEmail", email);
            Customer customer = (Customer) query.getSingleResult();


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String emailDate = dtf.format(localDate);
            String password  = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

            SendMailSSL mail = new SendMailSSL();
            String msg = "Your temporary password : "+password+ "\n"+
                    "Use this password to login your account after that please change your password from your profile.\n"+
                    "If you didn't request forgot password just ignore this email.";
            mail.sendMail(customer.getEmail(),GlobalConstants.EMAIL.SUBJECTCUSTOMER+customer.getName()+" - "+emailDate,msg);

            customer.setTempPassword(encodePassword(password));

            entityManager.merge(customer);
            entityManager.flush();

    }
    public ChatRoomDTO getMyChatRoomData(Long customerId){
        Query query = entityManager.createQuery("select c from ChatRoom c where c.customer.id=:aCustomer");
        query.setParameter("aCustomer" ,customerId);
        ChatRoom chatRoom = (ChatRoom) query.getSingleResult();
        ChatRoomMapper chatRoomMapper = new ChatRoomMapper();
        ChatRoomDTO data =  chatRoomMapper.mapToDTO(chatRoom);
        return data;
    }

    public List<RankDTO> getRankList(ChatRoomDTO chatRoomDTO){
        List<RankDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select r from Rank r where r.chatRoom.id=:aChatRoomId");
        query.setParameter("aChatRoomId" ,chatRoomDTO.getId());
        List<Rank> ranks = query.getResultList();
        RankMapper rankMapper = new RankMapper();
        List<RankDTO> rankDTOLIst =  rankMapper.mapToDTOList(ranks);

        if(chatRoomDTO.getPaid().equals("N")){
            for(RankDTO rank: rankDTOLIst){
                if(!rank.getCode().equals(GlobalConstants.USER_RANKS.CUSTOM)){
                    data.add(rank);
                }
            }
        }
        else{
            data = rankDTOLIst;
        }

        return data;
    }
    public RankDTO getRankData(Long rankId,ChatRoomDTO chatRoomDTO){
        Query query = entityManager.createQuery("select r from Rank r where r.chatRoom.id=:aChatRoomId AND r.id=:aId");
        query.setParameter("aChatRoomId" ,chatRoomDTO.getId());
        query.setParameter("aId" ,rankId);
        Rank rank = (Rank) query.getSingleResult();
        RankMapper rankMapper = new RankMapper();
        RankDTO rankDTO = rankMapper.mapToDTO(rank);
        if(chatRoomDTO.getPaid().equals("Y")){
            return rankDTO;
        }
        else{
            if(rankDTO.getCode().equals(GlobalConstants.USER_RANKS.CUSTOM)){
                return null;
            }
            return rankDTO;
        }

    }
    public ChatUserDTO openUser(Long id,Long chatRoomId){
        Query query = entityManager.createQuery("select u from User u where u.chatRoom.id=:aChatoRoomId and u.id =:aid");
        query.setParameter("aChatoRoomId" ,chatRoomId);
        query.setParameter("aid" ,id);
        User user = (User) query.getSingleResult();
        ChatUserDTO data = populateUser(user);
        return data;
    }
    public ChatRoomDTO updateSettings(ChatRoomDTO data,Long customerId){
        Query query = entityManager.createQuery("select c from ChatRoom c where c.customer.id=:aCustomer");
        query.setParameter("aCustomer" ,customerId);
        ChatRoom chatRoom = (ChatRoom) query.getSingleResult();
        chatRoom.setName(data.getName());
        chatRoom.setDomain(data.getDomain());
        chatRoom.setTheme(data.getTheme());
        chatRoom.setTopic(data.getTopic());
        chatRoom.setRadio(data.getRadio());
        chatRoom.setAntiSpam(data.getAntiSpam());
        chatRoom.setGuest(data.getGuest());
        chatRoom.setRegister(data.getRegister());
        chatRoom.setDesign(data.getDesign());
        chatRoom = entityManager.merge(chatRoom);
        entityManager.flush();

        try{
            cacheManager.getCache("getChatRoomById").evict(chatRoom.getId());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        ChatRoomMapper chatRoomMapper = new ChatRoomMapper();
        ChatRoomDTO update  = chatRoomMapper.mapToDTO(chatRoom);
        return update;
    }
    public RankDTO updateRank(RankDTO data,ChatRoomDTO chatRoomDTO){
        Query query = entityManager.createQuery("select r from Rank r where r.chatRoom.id=:aChatRoom and r.id=:rankId");
        query.setParameter("aChatRoom" ,chatRoomDTO.getId());
        query.setParameter("rankId" ,data.getId());
        Rank rank = (Rank) query.getSingleResult();
        if(chatRoomDTO.getPaid().equals("N")){
            if(rank.getCode().equals(GlobalConstants.USER_RANKS.CUSTOM)){
                return null;
            }
        }
        rank.setName(data.getName());
        rank.setIcon(data.getIcon());
        rank.setChangeNick(data.getChangeNick());
        rank.setDeleteMsg(data.getDeleteMsg());
        rank.setKick(data.getKick());
        rank.setSpam(data.getSpam());
        rank.setMute(data.getMute());
        rank.setBan(data.getBan());
        rank = entityManager.merge(rank);
        entityManager.flush();

        RankMapper rankMapper = new RankMapper();
        RankDTO updateRank  = rankMapper.mapToDTO(rank);
        return updateRank;
    }

    public int changePassword(CustomerDTO customerDTO) throws Exception {
        Query query = entityManager.createQuery("select c from Customer c where c.id=:aId");
        query.setParameter("aId",customerDTO.getId());
        Customer customer = (Customer) query.getSingleResult();
        if(customer.getPassword().equals(encodePassword(customerDTO.getOldPassword()))){
            customer.setPassword(encodePassword(customerDTO.getPassword()));
            customer.setTempPassword(null);
            entityManager.merge(customer);
            entityManager.flush();
            return 1;
        }

        return 0;

    }
    public List<ChatRoomDTO> getNetworkData(){
        Query queryUsers = entityManager.createQuery("select c from ChatRoom c");
        List<ChatRoom> chatRooms =  queryUsers.getResultList();
        ChatRoomMapper chatRoomMapper = new ChatRoomMapper();
        List<ChatRoomDTO> data =  chatRoomMapper.mapToDTOList(chatRooms);
        return data;
    }

    public DashboardDTO getDashboardData(Long customerId){
        Query queryUsers = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.customer.id=:aCustomerId");
        queryUsers.setParameter("aCustomerId",customerId);
        Long users = (Long) queryUsers.getSingleResult();

        Query queryAdmins = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.customer.id=:aCustomerId and u.rank.code=:aRank");
        queryAdmins.setParameter("aCustomerId",customerId);
        queryAdmins.setParameter("aRank", GlobalConstants.USER_RANKS.ADMIN);
        Long admins = (Long) queryAdmins.getSingleResult();

        Query queryMods = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.customer.id=:aCustomerId and u.rank.code=:aRank");
        queryMods.setParameter("aCustomerId",customerId);
        queryMods.setParameter("aRank",GlobalConstants.USER_RANKS.MOD);
        Long mods = (Long) queryMods.getSingleResult();

        Query queryCustom = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.customer.id=:aCustomerId and u.rank.code=:aRank");
        queryCustom.setParameter("aCustomerId",customerId);
        queryCustom.setParameter("aRank",GlobalConstants.USER_RANKS.CUSTOM);
        Long customs = (Long) queryCustom.getSingleResult();

        Query queryBanned = entityManager.createQuery("select count(b.id) from IpBanned b where b.chatRoom.customer.id=:aCustomerId");
        queryBanned.setParameter("aCustomerId",customerId);
        Long bannd = (Long) queryBanned.getSingleResult();

        Query queryRjs = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.customer.id=:aCustomerId and u.rj=:aRj");
        queryRjs.setParameter("aCustomerId",customerId);
        queryRjs.setParameter("aRj","Y");
        Long rjs = (Long) queryRjs.getSingleResult();

        Query queryMute = entityManager.createQuery("select count(u.id) from User u where u.chatRoom.customer.id=:aCustomerId and u.status=:aMute");
        queryMute.setParameter("aCustomerId",customerId);
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
    public List<ChatUserDTO> getUsersBySearch(String search , Long chatRoomId){
        List<ChatUserDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select u.userName , u.dp , u.id, u.nameColor, u.rank.name from User u where u.chatRoom.id=:achatroomid and u.userName like :aSearch");
        query.setParameter("achatroomid" ,chatRoomId);
        query.setParameter("aSearch" ,"%"+search+"%");
        query.setMaxResults(10);
        List<Object[]> userList = query.getResultList();
        if(userList.size() == 0){
            return null;
        }
        for(Object[]user : userList){
            ChatUserDTO userDTO =new ChatUserDTO();
            userDTO.setUserName((String) user[0]);
            userDTO.setDp((String) user[1]);
            userDTO.setId((Long) user[2]);
            userDTO.setNameColor((String) user[3]);
            userDTO.setRankName((String) user[4]);
            data.add(userDTO);
        }
        return data;

    }
    public List<ChatUserDTO> showMutedUsers(Long chatRoomId){
        List<ChatUserDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select u.userName , u.dp , u.id, u.nameColor, u.rank.name from User u where u.chatRoom.id=:achatroomid and u.status=:aSearch");
        query.setParameter("achatroomid" ,chatRoomId);
        query.setParameter("aSearch" ,"M");
        List<Object[]> userList = query.getResultList();
        if(userList.size() == 0){
            return null;
        }
        for(Object[]user : userList){
            ChatUserDTO userDTO =new ChatUserDTO();
            userDTO.setUserName((String) user[0]);
            userDTO.setDp((String) user[1]);
            userDTO.setId((Long) user[2]);
            userDTO.setNameColor((String) user[3]);
            userDTO.setRankName((String) user[4]);
            data.add(userDTO);
        }
        return data;

    }
    public List<ChatUserDTO> showBannedUsers(Long chatRoomId){
        List<ChatUserDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select u.userName , u.dp , u.id, u.nameColor, u.rank.name from User u where u.chatRoom.id=:achatroomid and u.ban=:aSearch");
        query.setParameter("achatroomid" ,chatRoomId);
        query.setParameter("aSearch" ,"Y");
        List<Object[]> userList = query.getResultList();
        if(userList.size() == 0){
            return null;
        }
        for(Object[]user : userList){
            ChatUserDTO userDTO =new ChatUserDTO();
            userDTO.setUserName((String) user[0]);
            userDTO.setDp((String) user[1]);
            userDTO.setId((Long) user[2]);
            userDTO.setNameColor((String) user[3]);
            userDTO.setRankName((String) user[4]);
            data.add(userDTO);
        }
        return data;

    }
    public List<ChatUserDTO> showRJUsers(Long chatRoomId){
        List<ChatUserDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select u.userName , u.dp , u.id, u.nameColor, u.rank.name from User u where u.chatRoom.id=:achatroomid and u.rj=:aSearch");
        query.setParameter("achatroomid" ,chatRoomId);
        query.setParameter("aSearch" ,"Y");
        List<Object[]> userList = query.getResultList();
        if(userList.size() == 0){
            return null;
        }
        for(Object[]user : userList){
            ChatUserDTO userDTO =new ChatUserDTO();
            userDTO.setUserName((String) user[0]);
            userDTO.setDp((String) user[1]);
            userDTO.setId((Long) user[2]);
            userDTO.setNameColor((String) user[3]);
            userDTO.setRankName((String) user[4]);
            data.add(userDTO);
        }
        return data;

    }
    public List<ChatUserDTO> showUsers(Long chatRoomId){
        List<ChatUserDTO> data = new ArrayList<>();
        Query query = entityManager.createQuery("select u.userName , u.dp , u.id, u.nameColor, u.rank.name from User u where u.chatRoom.id=:achatroomid order by u.id desc");
        query.setParameter("achatroomid" ,chatRoomId);
        query.setMaxResults(50);
        List<Object[]> userList = query.getResultList();
        if(userList.size() == 0){
            return null;
        }
        for(Object[]user : userList){
            ChatUserDTO userDTO =new ChatUserDTO();
            userDTO.setUserName((String) user[0]);
            userDTO.setDp((String) user[1]);
            userDTO.setId((Long) user[2]);
            userDTO.setNameColor((String) user[3]);
            userDTO.setRankName((String) user[4]);
            data.add(userDTO);
        }
        return data;

    }

    private ChatUserDTO populateUser(User user){
        ChatUserDTO chatUserDTO = new ChatUserDTO();
        chatUserDTO.setUserId(user.getId());
        chatUserDTO.setUserName(user.getUserName());
        chatUserDTO.setNameColor(user.getNameColor());
        chatUserDTO.setStatus(user.getStatus());
        chatUserDTO.setGender(user.getGender());
        chatUserDTO.setDp(user.getDp());
        chatUserDTO.setFont(user.getFont());
        chatUserDTO.setTextColor(user.getTextColor());
        chatUserDTO.setTweet(user.getTweet());
        chatUserDTO.setAbout(user.getAbout());
        chatUserDTO.setPoints(user.getPoints());
        chatUserDTO.setBirthDate(user.getBirthDate());
        chatUserDTO.setActivityDate(user.getActivityDate());
        chatUserDTO.setJoinDate(user.getJoinDate());
        chatUserDTO.setRankCode(user.getRank().getCode());
        chatUserDTO.setRankName(user.getRank().getName());
        chatUserDTO.setRankIcon(user.getRank().getIcon());
        chatUserDTO.setRj(user.getRj());
        chatUserDTO.setSpam(user.getSpam());
        chatUserDTO.setBan(user.getBan());
        chatUserDTO.setBold(user.getBold());
        chatUserDTO.setOther(user.getIp());

        return chatUserDTO;
    }
}
