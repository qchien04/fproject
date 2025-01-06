package com.example.fproject.service.forUser;

import com.example.fproject.entity.forUser.UserProfile;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserProfileService {
    public UserProfile findById(int id);
    public void deleteById(int id);
    public UserProfile findByUsername(String username);
    public List<UserProfile> searchUser(String query);
}
