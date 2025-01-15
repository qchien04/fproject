package com.example.fproject.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Integer chatRoomId;
    private String content;
    private String name;
    private String emailSend;
    private LocalDateTime timeSend;
}
