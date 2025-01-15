package com.example.fproject.service.imple;


import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.ChatRoom;
import com.example.fproject.exception.ChatException;
import com.example.fproject.exception.UserException;
import com.example.fproject.repository.ChatRoomRepo;
import com.example.fproject.request.GroupChatRequest;
import com.example.fproject.service.auth.UserService;
import com.example.fproject.service.chatchit.ChatRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatRoomServiceImp implements ChatRoomService {

    private ChatRoomRepo chatRoomRepo;
    private UserService userService;

    public ChatRoomServiceImp(ChatRoomRepo chatRoomRepo, UserService userService) {
        this.chatRoomRepo = chatRoomRepo;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ChatRoom createChat(User reqUser, Integer userId) throws UserException {
        System.out.println("Vao day");
        User user = this.userService.findById(userId);
        ChatRoom isChatExist = this.chatRoomRepo.findSingleChatByUserIds(user, reqUser);
        if (isChatExist != null) {
            System.out.println("khac null");
            return isChatExist;
        }
        ChatRoom chat = new ChatRoom();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        chat = this.chatRoomRepo.save(chat);
        return chat;
    }

    @Override
    public ChatRoom findChatRoomById(Integer chatId) throws ChatException {
        return this.chatRoomRepo.findById(chatId)
                .orElseThrow(() -> new ChatException("The requested chat is not found"));
    }
    @Override
    @Transactional
    public List<ChatRoom> findAllChatRoomByUserId(Integer userId) throws UserException {
        User user = this.userService.findById(userId);

        List<ChatRoom> chats = this.chatRoomRepo.findChatByUserId(user.getId());

        List<ChatRoom> res = new ArrayList<>();
        for (ChatRoom i : chats) {
//            System.out.println(i.getAdmins());
//            System.out.println( i.getCreatedBy());
//            System.out.println(i.getUsers());
//            System.out.println(i.getMessages());
//            Hibernate.initialize(i.getAdmins());
//            Hibernate.initialize(i.getUsers());
//            Hibernate.initialize(i.getMessages());
//            Hibernate.initialize(i.getCreatedBy());

//            Set<User> u=i.getAdmins();
//            User cr=i.getCreatedBy();
//            Set<User> se=i.getUsers();
//            List<Message> lm=i.getMessages();

            i.getAdmins().size();
            i.getCreatedBy();
            i.getUsers().size();
            i.getMessages().size();
 //           System.out.println(u+" "+cr+" "+se+" "+lm+" ");
//            res.add(new Chat(i.getId(), i.getChatName(), i.getChatImage(), , i.getAdmins(),
//                    i.getCreatedBy(), i.getUsers(), i.getMessages()));
        }
//        return chats;
        return chats;

    }





    @Override
    public ChatRoom createGroup(GroupChatRequest req, User reqUser) throws UserException {
        ChatRoom group = new ChatRoom();
        group.setGroup(true);
        group.setChatRoomImage(req.getChatImage());
        group.setChatRoomName(req.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);

        for (Integer userId : req.getUserIds()) {
            User user = this.userService.findById(userId);
            group.getUsers().add(user);
        }

        group = this.chatRoomRepo.save(group);
        return group;
    }

    @Override
    public ChatRoom addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        ChatRoom chat = this.chatRoomRepo.findById(chatId)
                .orElseThrow(() -> new ChatException("The expected chat is not found"));

        User user = this.userService.findById(userId);

        if (chat.getAdmins().contains((reqUser))) {
            chat.getUsers().add(user);
            return chat;
        } else {
            throw new UserException("You have not access to add user");
        }
    }

    @Override
    public ChatRoom renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
        ChatRoom chat = this.chatRoomRepo.findById(chatId)
                .orElseThrow(() -> new ChatException("The expected chat is not found"));

        if (chat.getUsers().contains(reqUser)) {
            chat.setChatRoomName(groupName);
            return this.chatRoomRepo.save(chat);
        } else {
            throw new UserException("YOu are not the user");
        }
    }

    @Override
    public ChatRoom removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        ChatRoom chat = this.chatRoomRepo.findById(chatId)
                .orElseThrow(() -> new ChatException("The expected chat is not found"));

        User user = this.userService.findById(userId);

        if (chat.getAdmins().contains((reqUser))) {
            chat.getUsers().remove(user);
            return chat;
        } else if (chat.getUsers().contains(reqUser)) {
            if (user.getId().equals(reqUser.getId())) {
                chat.getUsers().remove(user);
                return this.chatRoomRepo.save(chat);
            }

        }
        throw new UserException("You have not access to remove user");
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        ChatRoom chat = this.chatRoomRepo.findById(chatId)
                .orElseThrow(() -> new ChatException("The expected chat is not found while deleteing"));
        this.chatRoomRepo.delete(chat);
    }
}
