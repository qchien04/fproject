package com.example.fproject.service;


import com.example.fproject.entity.forUser.Permission;
import com.example.fproject.entity.forUser.Role;
import com.example.fproject.service.forUser.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private UserService userService;

    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.fproject.entity.forUser.User user=this.userService.findByUsername(username);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            for (Permission permission: role.getPermissions() ){
                authorities.add(new SimpleGrantedAuthority( permission.getPermissionName() ) );
            }
        }
        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
