package com.example.fproject.repository;

import com.example.fproject.entity.forUser.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile,Integer> {
    Optional<UserProfile> findByName(String name);
    Optional<UserProfile> findById(int id);
    Optional<UserProfile> findByUserId(Integer userId);
}
