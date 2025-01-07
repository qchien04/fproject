package com.example.fproject.controller;

import com.example.fproject.entity.forUser.User;
import com.example.fproject.entity.forUser.UserProfile;
import com.example.fproject.response.AuthRespone;
import com.example.fproject.security.TokenProvider;
import com.example.fproject.service.UserDetailsCustom;
import com.example.fproject.service.forUser.UserProfileService;
import com.example.fproject.service.forUser.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/google/login")
@AllArgsConstructor
public class GoogleAuthController{

    private UserService userService;
    private UserProfileService userProfileService;
    private PasswordEncoder passwordEncoder;
    private UserDetailsCustom userDetailsCustom;
    private TokenProvider tokenProvider;
    @PostMapping("/user")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");
        System.out.println("Call"+accessToken);
        // Gọi Google API để xác thực Access Token
        String tokenInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> tokenInfo = restTemplate.getForObject(tokenInfoUrl, Map.class);
            System.out.println(tokenInfo);
            if (tokenInfo != null && tokenInfo.containsKey("email")) {
                // Xử lý thông tin người dùng
                String email = (String) tokenInfo.get("email");

                String name = (String) tokenInfo.get("name");
                String picture = (String) tokenInfo.get("picture");


                User find_user =userService.findByEmail(email);

                System.out.println(find_user);
                if(find_user==null){
                    System.out.println("bang null la ro");
                    User user=new User(email,email,"123");
                    UserProfile userProfile=new UserProfile();
                    userProfile.setName(name);
                    userProfile.setAvt(picture);
                    userProfile.setUser(user);
                    userProfileService.save(userProfile);

                    System.out.println("come in");
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                    org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(email, "123", authorities);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    String jwt=tokenProvider.genarateToken(authentication);
                    AuthRespone res=new AuthRespone(jwt,true);
                    return new ResponseEntity<AuthRespone>(res, HttpStatus.OK);

                }
                else{
                    System.out.println("da co trong db");
                    Authentication authentication=authenticate(email);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt=tokenProvider.genarateToken(authentication);
                    AuthRespone res=new AuthRespone(jwt,true);
                    return new ResponseEntity<AuthRespone>(res, HttpStatus.OK);
                }

            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Invalid access token"
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to verify token"
            ));
        }
    }

    public Authentication authenticate(String username){
        UserDetails userDetails= userDetailsCustom.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid user");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
