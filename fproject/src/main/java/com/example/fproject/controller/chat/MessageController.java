package com.example.fproject.controller.chat;


import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.Message;
import com.example.fproject.exception.ChatException;
import com.example.fproject.exception.MessageException;
import com.example.fproject.exception.UserException;
import com.example.fproject.request.SendMessageRequest;
import com.example.fproject.response.ApiResponse;
import com.example.fproject.response.MessageResponse;
import com.example.fproject.service.auth.UserService;
import com.example.fproject.service.imple.MessageServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageServiceImp messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest sendMessageRequest,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = this.userService.findByJwt(jwt);

        sendMessageRequest.setUserId(user.getId());
        System.out.println("bat dau create mess");
        Message message = this.messageService.sendMessage(sendMessageRequest);
        System.out.println("Done create");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageResponse>> getChatMessageHandler(@PathVariable Integer chatId,
                                                               @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = this.userService.findByJwt(jwt);
        List<Message> messages = this.messageService.getChatsMessages(chatId, user);
        List<MessageResponse> res=new ArrayList<>();
        for(Message i:messages){
            MessageResponse messageResponse=new MessageResponse(chatId,i.getContent(),i.getUser().getUserProfile().getName(),i.getUser().getEmail(), i.getTimestamp());
            res.add(messageResponse);
        }
        return new ResponseEntity<List<MessageResponse>>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId,
                                                            @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException, MessageException {

        User user = this.userService.findByJwt(jwt);

        this.messageService.deleteMessage(messageId, user);

        ApiResponse res = new ApiResponse("Deleted successfully......", false);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}