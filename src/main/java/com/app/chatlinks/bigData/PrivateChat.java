package com.app.chatlinks.bigData;

import com.app.chatlinks.mysql.model.GenericModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "private")
public class PrivateChat extends GenericModel {
    @Id private String id;
    @Indexed private Long fromUserId;
    private String fromUserName;
    private String fromNameColor;
    private String fromTextColor;
    private String fromTextFont;
    private String fromDp;
    @Indexed private Long toUserId;
    private String content;
    private String seen;
    private Instant date;
    private String msgType;
    @Indexed private Long chatRoomId;
    private String image;
    private String ip;

    private String delete;
}
