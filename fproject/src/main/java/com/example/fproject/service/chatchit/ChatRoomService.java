package com.example.fproject.service.chatchit;



import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.ChatRoom;
import com.example.fproject.exception.ChatException;
import com.example.fproject.exception.UserException;
import com.example.fproject.request.GroupChatRequest;


import java.util.List;


public interface ChatRoomService {

    public ChatRoom createChat(User reqUser, Integer userId) throws UserException;

    public ChatRoom findChatRoomById(Integer chatId) throws ChatException;

    public List<ChatRoom> findAllChatRoomByUserId(Integer userId) throws UserException;

    public ChatRoom createGroup(GroupChatRequest req, User reqUser) throws UserException;

    public ChatRoom addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;

    public ChatRoom renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException;

    public ChatRoom removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException;

    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException;

}
