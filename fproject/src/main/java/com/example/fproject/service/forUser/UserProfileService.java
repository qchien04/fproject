package com.example.fproject.service.forUser;

import com.example.fproject.entity.forUser.UserProfile;
import com.example.fproject.exception.UserException;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserProfileService {
    public UserProfile save(UserProfile userProfile);
    public UserProfile findById(int id);
    public void deleteById(int id);
    public UserProfile findByUsername(String username);
    public UserProfile findByUserId(int userId) throws UserException;
    public List<UserProfile> searchUser(String query);
}
