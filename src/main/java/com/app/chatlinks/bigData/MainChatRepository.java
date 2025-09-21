package com.app.chatlinks.bigData;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface MainChatRepository extends MongoRepository<MainChat, String> {

    List<MainChat> findByChatRoomId(Long chatRoomId);

    List<MainChat> findByChatRoomIdAndChatId(Long chatRoomId,String chatId);

    List<MainChat> findByDateBetween(Instant start, Instant end);

    List<MainChat> findByChatRoomIdAndDateBetween(Long chatRoomId, Instant start, Instant end);

    List<MainChat> findTop60ByChatRoomIdOrderByDateDesc(Long chatRoomId);

    List<MainChat> findTop60ByChatRoomIdOrderByDate(Long chatRoomId);

    MainChat findTopByChatRoomIdOrderByDateDesc(Long chatRoomId);


}
