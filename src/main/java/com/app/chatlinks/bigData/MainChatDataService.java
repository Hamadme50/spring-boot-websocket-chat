package com.app.chatlinks.bigData;

import com.app.chatlinks.dto.chat.ChatMessage;
import com.app.chatlinks.dto.chat.MainChatDTO;
import com.app.chatlinks.dto.chat.PrivateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class MainChatDataService {

    @Autowired
    private MainChatRepository mainChatRepository;

    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public void saveMainChat(ChatMessage chatMessage,String ip){
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
        java.util.Date utilDate = new java.util.Date(chatMessage.getMsgDate().getTime());
        chat.setDate(utilDate.toInstant());
        chat.setType(chatMessage.getType().toString());
        chat.setDp(chatMessage.getDp());
        chat.setIp(ip);
        chat.setDelete("N");

        mainChatRepository.save(chat);

    }

    public void savePrivateChat(PrivateMessage chatMessage, String ip){
        PrivateChat chat = new PrivateChat();
        chat.setFromUserId(chatMessage.getFromUserId());
        chat.setFromUserName(chatMessage.getFromUserName());
        chat.setFromNameColor(chatMessage.getFromNameColor());
        chat.setFromTextColor(chatMessage.getFromTextColor());
        chat.setFromTextFont(chatMessage.getFromTextFont());
        chat.setFromDp(chatMessage.getFromDp());
        chat.setToUserId(chatMessage.getToUserId());
        chat.setContent(chatMessage.getContent());
        chat.setSeen(chatMessage.getSeen());
        java.util.Date utilDate = new java.util.Date(chatMessage.getMsgDate().getTime());
        chat.setDate(utilDate.toInstant());
        chat.setMsgType(chatMessage.getMsgType().toString());
        chat.setChatRoomId(chatMessage.getChatRoomId());
        chat.setImage(chatMessage.getImage());
        chat.setIp(ip);
        chat.setDelete("N");

        privateChatRepository.save(chat);

    }

    public void deleteMainChat(Long chatRoomid,String id){

        List<MainChat> data = mainChatRepository.findByChatRoomIdAndChatId(chatRoomid,id);
        for(MainChat d : data){
            d.setDelete("Y");
            mainChatRepository.save(d);
        }


    }
    public void deleteAllMainChat(Long chatRoomid){

        List<MainChat> data = mainChatRepository.findByChatRoomId(chatRoomid);
        for(MainChat d : data){
            d.setDelete("Y");
            mainChatRepository.save(d);
        }


    }

    public void deleteAllPrivateChat(Long chatRoomid,Long toUserId){

        Aggregation aggregation = Aggregation.newAggregation(
                // Step 1: Match documents where either fromUserId or toUserId matches
                Aggregation.match(Criteria.where("chatRoomId").is(chatRoomid)
                        .andOperator(
                                new Criteria().orOperator(
                                        Criteria.where("toUserId").is(toUserId),
                                        Criteria.where("fromUserId").is(toUserId)
                                )
                        )
                ),

                // Step 2: Sort the documents by date in descending order
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "date"))

        );
        AggregationResults<PrivateChat> results = mongoTemplate.aggregate(aggregation, "private", PrivateChat.class);
        List<PrivateChat> data =  results.getMappedResults();
        for(PrivateChat d : data){
            d.setDelete("Y");
            privateChatRepository.save(d);
        }


    }

    public List<MainChatDTO> getChatHistory(Long chatRoomid) {
        List<MainChatDTO> dto = new ArrayList<>();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("chatRoomId").is(chatRoomid)
                                .and("delete").is("N")
                ),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "date")), // Sort by date descending to get recent messages
                Aggregation.limit(60)  // Limit to the most recent 60 messages
        );

        AggregationResults<MainChat> results = mongoTemplate.aggregate(aggregation, "chat", MainChat.class);
        List<MainChat> data = results.getMappedResults();

        MainChatMapper mapper = new MainChatMapper();
        for (MainChat d : data) {
            dto.add(mapper.mapToDTO(d));
        }
        return dto;
    }


    public List<PrivateMessage> getMsgs(Long chatRoomid, Long toUSer){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("chatRoomId").is(chatRoomid).and("toUserId").is(toUSer).and("delete").is("N")),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "date")),
                Aggregation.group("fromUserId")
                        .first("fromUserId").as("fromUserId")
                        .first("fromUserName").as("fromUserName")
                        .first("fromNameColor").as("fromNameColor")
                        .first("fromTextColor").as("fromTextColor")
                        .first("fromTextFont").as("fromTextFont")
                        .first("fromDp").as("fromDp")
                        .first("content").as("content")
                        .first("msgType").as("msgType")
                        .first("date").as("date")
                        .first("seen").as("seen")
        );

        AggregationResults<PrivateChat> results = mongoTemplate.aggregate(aggregation, "private", PrivateChat.class);
        List<PrivateChat> data =  results.getMappedResults();

        List<PrivateMessage> chatList = new ArrayList<>();
        for(PrivateChat chatMessage : data){
            PrivateMessage chat = new PrivateMessage();
            chat.setFromUserId(chatMessage.getFromUserId());
            chat.setFromUserName(chatMessage.getFromUserName());
            chat.setFromNameColor(chatMessage.getFromNameColor());
            chat.setFromTextColor(chatMessage.getFromTextColor());
            chat.setFromTextFont(chatMessage.getFromTextFont());
            chat.setFromDp(chatMessage.getFromDp());
            chat.setToUserId(chatMessage.getToUserId());
            chat.setContent(chatMessage.getContent());
            chat.setSeen(chatMessage.getSeen());
            chat.setMsgDate(Date.from(chatMessage.getDate()));
            chat.setMsgType(PrivateMessage.MessageType.valueOf(chatMessage.getMsgType()));
            chat.setChatRoomId(chatMessage.getChatRoomId());
            chat.setImage(chatMessage.getImage());
            chat.setSeen(chatMessage.getSeen());
            chatList.add(chat);
        }
        chatList.sort((message1, message2) -> message2.getMsgDate().compareTo(message1.getMsgDate()));
        return chatList;

    }

    public Long checkUnreadMsgs(Long chatRoomid, Long toUser){
        Aggregation aggregation = Aggregation.newAggregation(
                // Step 1: Match documents based on chatRoomId, toUserId, and delete flag
                Aggregation.match(Criteria.where("chatRoomId").is(chatRoomid)
                        .and("toUserId").is(toUser).and("seen").is("N")),

                // Step 2: Count the total number of matching records
                Aggregation.count().as("recordCount")
        );

        // Execute the aggregation
        AggregationResults<CountResult> results = mongoTemplate.aggregate(aggregation, "private", CountResult.class);

        // Return the count of records
        if (!results.getMappedResults().isEmpty()) {
            return results.getMappedResults().get(0).getRecordCount();
        } else {
            return 0L; // No matching records
        }

    }

    public void seenMsgs(Long chatRoomid, Long toUser,Long fromUserId){
        Query query = new Query();
        query.addCriteria(Criteria.where("chatRoomId").is(chatRoomid).and("seen").is("N")
                .and("toUserId").is(toUser).and("fromUserId").is(fromUserId));

        Update update = new Update();
        update.set("seen", "Y");

        mongoTemplate.updateMulti(query, update, "private");

    }
    public List<PrivateMessage> getPrivateData(Long chatRoomid, Long toUserId,Long fromUserId){
        Aggregation aggregation = Aggregation.newAggregation(
                // Step 1: Match documents where either fromUserId or toUserId matches
                Aggregation.match(Criteria.where("chatRoomId").is(chatRoomid).and("delete").is("N")
                        .orOperator(
                                Criteria.where("toUserId").is(toUserId)
                                        .and("fromUserId").is(fromUserId),
                                Criteria.where("toUserId").is(fromUserId)
                                        .and("fromUserId").is(toUserId)
                        )
                ),

                // Step 2: Sort the documents by date in **descending** order
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "date")),
                // Step 3: Limit the results to the latest 30 messages
                Aggregation.limit(30)

        );


        AggregationResults<PrivateChat> results = mongoTemplate.aggregate(aggregation, "private", PrivateChat.class);
        List<PrivateChat> data =  results.getMappedResults();

        List<PrivateMessage> chatList = new ArrayList<>();
        for(PrivateChat chatMessage : data){
            PrivateMessage chat = new PrivateMessage();
            chat.setFromUserId(chatMessage.getFromUserId());
            chat.setFromUserName(chatMessage.getFromUserName());
            chat.setFromNameColor(chatMessage.getFromNameColor());
            chat.setFromTextColor(chatMessage.getFromTextColor());
            chat.setFromTextFont(chatMessage.getFromTextFont());
            chat.setFromDp(chatMessage.getFromDp());
            chat.setToUserId(chatMessage.getToUserId());
            chat.setContent(chatMessage.getContent());
            chat.setSeen(chatMessage.getSeen());
            chat.setMsgDate(Date.from(chatMessage.getDate()));
            chat.setMsgType(PrivateMessage.MessageType.valueOf(chatMessage.getMsgType()));
            chat.setChatRoomId(chatMessage.getChatRoomId());
            chat.setImage(chatMessage.getImage());
            chatList.add(chat);
        }
        Collections.reverse(chatList);
        return chatList;

    }


}


