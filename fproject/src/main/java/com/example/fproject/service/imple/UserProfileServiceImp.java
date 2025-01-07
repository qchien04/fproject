package com.example.fproject.service.imple;


import com.example.fproject.entity.forUser.User;
import com.example.fproject.entity.forUser.UserProfile;
import com.example.fproject.repository.UserProfileRepo;
import com.example.fproject.security.TokenProvider;
import com.example.fproject.service.forUser.UserProfileService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class UserProfileServiceImp implements UserProfileService {

    private final UserProfileRepo userProfileRepo;

    private TokenProvider tokenProvider;


    public UserProfileServiceImp(UserProfileRepo userProfileRepo,TokenProvider tokenProvider) {
        this.userProfileRepo = userProfileRepo;
        this.tokenProvider=tokenProvider;
    }


    @Override
    @Transactional
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepo.save(userProfile);
    }

    @Override
    public UserProfile findById(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public UserProfile findByUsername(String username) {
        return null;
    }


    @Override
    public List<UserProfile> searchUser(String query) {
        return List.of();
    }
}
