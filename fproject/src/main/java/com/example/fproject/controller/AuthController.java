package com.example.fproject.controller;


import com.example.fproject.entity.OTPCode;
import com.example.fproject.entity.forUser.User;
import com.example.fproject.exception.UserException;
import com.example.fproject.request.AuthAccount;
import com.example.fproject.request.LoginRequest;
import com.example.fproject.request.UserRegister;
import com.example.fproject.response.AuthRespone;
import com.example.fproject.response.RegisterRespone;
import com.example.fproject.security.TokenProvider;
import com.example.fproject.service.MailService;
import com.example.fproject.service.OTPCodeService;
import com.example.fproject.service.UserDetailsCustom;
import com.example.fproject.service.forUser.UserProfileService;
import com.example.fproject.service.forUser.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MailService mailService;
    private OTPCodeService otpCodeService;
    private UserService userService;
    private UserProfileService userProfileService;
    private PasswordEncoder passwordEncoder;
    private UserDetailsCustom userDetailsCustom;
    private TokenProvider tokenProvider;
    public AuthController(OTPCodeService otpCodeService, MailService mailService, TokenProvider tokenProvider, UserDetailsCustom userDetailsCustom, UserProfileService userProfileService,
                          UserService userService, PasswordEncoder passwordEncoder) {
        this.mailService = mailService;
        this.userService=userService;
        this.userProfileService=userProfileService;
        this.passwordEncoder=passwordEncoder;
        this.userDetailsCustom=userDetailsCustom;
        this.tokenProvider=tokenProvider;
        this.otpCodeService=otpCodeService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUserHandler(@RequestBody UserRegister userRegister) throws UserException, MessagingException {
        String username=userRegister.getUsername();
        String email=userRegister.getEmail();
        String password=userRegister.getPassword();
        User isUser=userService.findByEmail(email);
        if(isUser!=null){
            throw new UserException("Email is used with other account "+email);
        }


        Random random = new Random();
        String randomInt = (10000 + random.nextInt(89999))+"";

        OTPCode otpCode=new OTPCode(randomInt,email,5);
        otpCodeService.saveOTPCode(otpCode);

        mailService.sendEmail(email,"Xac thuc tai khoan",randomInt,null);


        RegisterRespone res=new RegisterRespone();
        res.setEmail(email);
        res.setUsername(username);
        res.setPassword(password);

        return new ResponseEntity<RegisterRespone>(res, HttpStatus.OK);
    }


    @PostMapping("/authAccount")
    public ResponseEntity<AuthRespone> authAccountHandler(@RequestBody AuthAccount authAccount) throws UserException, MessagingException {
        String username=authAccount.getUsername();
        String email=authAccount.getEmail();
        String password=authAccount.getPassword();
        OTPCode otp=otpCodeService.findOTPCode(email,authAccount.getOtp());

        if(otp==null){
            throw new UserException("OTP invalid! "+email);
        }

        User createdUser=new User();
        createdUser.setEmail(email);
        createdUser.setUsername(username);
//        createdUser.setPassword(password);

        createdUser.setPassword(passwordEncoder.encode(password));
        userService.save(createdUser);
        Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=tokenProvider.genarateToken(authentication);
        AuthRespone res=new AuthRespone(jwt,true);
        return new ResponseEntity<AuthRespone>(res, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthRespone> loginHandler(@RequestBody LoginRequest loginRequest) throws UserException {
        String username=loginRequest.getUsername();
        String password=loginRequest.getPassword();
        System.out.println(username+" "+password);
        Authentication authentication=authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=tokenProvider.genarateToken(authentication);
        AuthRespone res=new AuthRespone(jwt,true);
        return new ResponseEntity<AuthRespone>(res, HttpStatus.OK);

    }

    public Authentication authenticate(String username,String password){
        UserDetails userDetails= userDetailsCustom.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid user");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password or username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


}
