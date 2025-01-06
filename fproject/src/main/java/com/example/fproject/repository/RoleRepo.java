package com.example.fproject.repository;

import com.example.fproject.entity.forUser.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleName(String roleName);
}
