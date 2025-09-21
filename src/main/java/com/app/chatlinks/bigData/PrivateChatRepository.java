package com.app.chatlinks.bigData;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface PrivateChatRepository extends MongoRepository<PrivateChat, String> {

    List<PrivateChat> findByChatRoomId(Long chatRoomId);

    List<PrivateChat> findByChatRoomIdAndId(Long chatRoomId,String id);

    List<PrivateChat> findByChatRoomIdAndToUserId(Long chatRoomId,Long toUserId);

    List<PrivateChat> findByDateBetween(Instant start, Instant end);

    List<PrivateChat> findByChatRoomIdAndDateBetween(Long chatRoomId, Instant start, Instant end);

    List<PrivateChat> findTop60ByChatRoomIdOrderByDateDesc(Long chatRoomId);

    PrivateChat findTop30ByChatRoomIdAndToUserIdAndFromUserIdOrderByDateDesc(Long chatRoomId,Long toUserId,Long fromUserId);


}
