package com.example.fproject.service.chatchit;



import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.Message;
import com.example.fproject.exception.ChatException;
import com.example.fproject.exception.MessageException;
import com.example.fproject.exception.UserException;
import com.example.fproject.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException;

    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;

    public Message findMessageById(Integer messaageId) throws MessageException;

    public void deleteMessage(Integer messageId, User reqUser) throws MessageException;

}
