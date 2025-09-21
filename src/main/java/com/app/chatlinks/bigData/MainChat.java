package com.app.chatlinks.bigData;

import com.app.chatlinks.mysql.model.GenericModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "chat")
public class MainChat extends GenericModel {
    @Id
    private String id;
    @Indexed private String chatId;
    private String content;
    private String userName;
    private Long userId;
    @Indexed private Long chatRoomId;
    private String nameColor;
    private String dp;
    private String textColor;
    private String bold;
    private String font;
    private String userStatus;
    private String type;
    private String delete;

    private String ip;
    private Instant date;

}
