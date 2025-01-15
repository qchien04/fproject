package com.example.fproject.service.imple;


import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.ChatRoom;
import com.example.fproject.entity.chatchit.Message;
import com.example.fproject.exception.ChatException;
import com.example.fproject.exception.MessageException;
import com.example.fproject.exception.UserException;
import com.example.fproject.repository.MessageRepo;
import com.example.fproject.request.SendMessageRequest;
import com.example.fproject.service.chatchit.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class MessageServiceImp implements MessageService {

    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private UserServiceImp userService;

    @Autowired
    private ChatRoomServiceImp chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = this.userService.findById(req.getUserId());
        ChatRoom chat = this.chatService.findChatRoomById(req.getChatId());

        Message message = new Message();
        message.setChatRoom(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());

        message = this.messageRepository.save(message);
        // Send message to WebSocket topic based on chat type
        if (chat.isGroup()) {
            messagingTemplate.convertAndSend("/group/" + chat.getId(), message);
        } else {
            System.out.println("ko p group");
            messagingTemplate.convertAndSend( "/user/" + chat.getId(), message);
            System.out.println("ko hay roi");
        }

        return message;
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
        ChatRoom chat = this.chatService.findChatRoomById(chatId);

        if (!chat.getUsers().contains(reqUser)) {
            throw new UserException("You are not related to this chat");
        }
        List<Message> messages = this.messageRepository.findByChatRoomId(chat.getId());
        return messages;

    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Message message = this.messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException("The required message is not found"));
        return message;
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException {
        Message message = this.messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException("The required message is not found"));

        if (message.getUser().getId().equals(reqUser.getId())) {
            this.messageRepository.delete(message);
        } else {
            throw new MessageException("You are not authorized for this task");
        }
    }

}
