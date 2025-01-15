package com.example.fproject.service.auth;

import com.example.fproject.entity.auth.UserProfile;
import com.example.fproject.exception.UserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserProfileService {
    public UserProfile save(UserProfile userProfile);
    public UserProfile findById(int id);
    public void deleteById(int id);
    public UserProfile findByUsername(String username);
    public UserProfile findByUserId(int userId) throws UserException;
    public List<UserProfile> searchUser(String query);
}
