package com.example.fproject.controller.chat;

import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.ChatRoom;
import com.example.fproject.exception.ChatException;
import com.example.fproject.exception.UserException;
import com.example.fproject.request.GroupChatRequest;
import com.example.fproject.request.SingleChatRequest;
import com.example.fproject.response.ApiResponse;
import com.example.fproject.response.ChatRoomRespone;
import com.example.fproject.response.UserNameAndPicture;
import com.example.fproject.service.imple.ChatRoomServiceImp;
import com.example.fproject.service.imple.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/chatRoom")
public class ChatRoomController {

    @Autowired
    private ChatRoomServiceImp chatService;

    @Autowired
    private UserServiceImp userService;

    @PostMapping("/single")
    public ResponseEntity<ChatRoom> createChatHandler(@RequestBody SingleChatRequest singleChatRequest) throws UserException {
        System.out.println("bat dau single");
       // String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User reqUser = this.userService.findByEmail(singleChatRequest.getEmailSend());
        ChatRoom chat = this.chatService.createChat(reqUser, singleChatRequest.getUserId());

        return new ResponseEntity<ChatRoom>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatRoom> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest,
                                                   @RequestHeader("Authorization") String jwt) throws UserException {

        System.out.println(groupChatRequest);
        User reqUser = this.userService.findByJwt(jwt);
        ChatRoom chat = this.chatService.createGroup(groupChatRequest, reqUser);
        return new ResponseEntity<ChatRoom>(chat, HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatRoom> findChatByIdHandler(@PathVariable int chatId) throws ChatException {

        ChatRoom chat = this.chatService.findChatRoomById(chatId);

        return new ResponseEntity<ChatRoom>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ChatRoomRespone>> findChatByUserIdHandler(@RequestHeader("Authorization") String jwt)
            throws UserException {
        System.out.println("lay room");
        User reqUser = this.userService.findByJwt(jwt);
        List<ChatRoom> chats = this.chatService.findAllChatRoomByUserId(reqUser.getId());

        List<ChatRoomRespone> res=new ArrayList<>();
        for(ChatRoom i:chats){
            if(i.isGroup()){
                //not support yet
            }
            else{
                ChatRoomRespone chatRoomRespone=new ChatRoomRespone();
                chatRoomRespone.setId(i.getId());
                for(User j:i.getUsers()){
                    UserNameAndPicture userNameAndPicture=new UserNameAndPicture(j.getEmail(),j.getUserProfile().getName(),j.getUserProfile().getAvt());
                    chatRoomRespone.getUsers().add(userNameAndPicture);
                }
                res.add(chatRoomRespone);
            }
        }
        return new ResponseEntity<List<ChatRoomRespone>>(res, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<ChatRoom> addUserToGroupHandler(@PathVariable Integer chatId,
                                                      @PathVariable Integer userId, @RequestHeader("Authorization") String jwt)
            throws UserException, ChatException {

        User reqUser = this.userService.findByJwt(jwt);

        ChatRoom chat = this.chatService.addUserToGroup(userId, chatId, reqUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<ChatRoom> removeUserFromGroupHandler(@PathVariable Integer chatId,
                                                           @PathVariable Integer userId, @RequestHeader("Authorization") String jwt)
            throws UserException, ChatException {

        User reqUser = this.userService.findByJwt(jwt);

        ChatRoom chat = this.chatService.removeFromGroup(userId, chatId, reqUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId,
                                                         @RequestHeader("Authorization") String jwt)
            throws UserException, ChatException {

        User reqUser = this.userService.findByJwt(jwt);

        this.chatService.deleteChat(chatId, reqUser.getId());

        ApiResponse res = new ApiResponse("Deleted Successfully...", false);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}