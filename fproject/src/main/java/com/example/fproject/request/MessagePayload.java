package com.example.fproject.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {

    private String name;
    private String content;
    private String emailSend;
    private Integer chatRoomId;
    private LocalDateTime timeSend;
}
