package com.example.fproject.controller.chat;

import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.ChatRoom;
import com.example.fproject.entity.chatchit.Message;
import com.example.fproject.exception.ChatException;
import com.example.fproject.repository.MessageRepo;
import com.example.fproject.request.MessagePayload;
import com.example.fproject.request.TypingPayload;
import com.example.fproject.service.imple.ChatRoomServiceImp;
import com.example.fproject.service.imple.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;


@RestController
public class ChatController {

    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private UserServiceImp userService;

    @Autowired
    private ChatRoomServiceImp chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/news")
    @SendTo("/room/news")
    public String broadcastNews(@Payload String message) {
        //System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("goi new");
        return message;
    }

    @MessageMapping("/sendMessage")
    public void broadcast(@Payload MessagePayload messagePayload) throws ChatException {
        messagingTemplate.convertAndSend("/room/" + messagePayload.getChatRoomId(), messagePayload);
        User user = this.userService.findByEmail(messagePayload.getEmailSend());
        ChatRoom chat = this.chatService.findChatRoomById(messagePayload.getChatRoomId());

        Message message = new Message();
        message.setChatRoom(chat);
        message.setUser(user);
        message.setContent(messagePayload.getContent());
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
    }
    @MessageMapping("/typing")
    public void typing(@Payload TypingPayload typingPayload) throws ChatException {
        System.out.println(typingPayload);
        messagingTemplate.convertAndSend("/room/" + typingPayload.getChatRoomId(), typingPayload);
    }

    @MessageMapping("/personnalmessage")
    @SendToUser("/queue/personnalmessage")
    public String reply(@Payload String message,
                        Principal user) {
        System.out.println("goi personnalmessage");
        //System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return message;
    }
}
