package com.example.fproject.controller;



import com.example.fproject.entity.forUser.User;
import com.example.fproject.exception.UserException;
import com.example.fproject.service.forUser.UserProfileService;
import com.example.fproject.service.forUser.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private UserProfileService userProfileService;

    public UserController(UserService userService,UserProfileService userProfileService){
        this.userProfileService=userProfileService;
        this.userService=userService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getUserTokenHandler(@RequestHeader("Authorization") String token){
        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user=userService.findByJwt(token);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query) throws UserException {
        System.out.println("query: "+query);
        List<User> users=userService.searchUser(query);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

//    @PutMapping("/update")
//    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorizaion") String token) throws UserException {
//        User user=userService.findByJwt(token);
//        userProfileService.updateUserProfile(user.getUserId(),req);
//        ApiResponse apiResponse=new ApiResponse("Update successfully",true);
//        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.ACCEPTED);
//    }



}
