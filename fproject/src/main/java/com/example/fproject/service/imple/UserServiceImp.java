package com.example.fproject.service.imple;


import com.example.fproject.entity.forUser.User;
import com.example.fproject.exception.UserException;
import com.example.fproject.repository.UserRepo;
import com.example.fproject.response.PageResponse;
import com.example.fproject.security.TokenProvider;
import com.example.fproject.service.forUser.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
public class UserServiceImp implements UserService {


    private final UserRepo userRepo;
    private TokenProvider tokenProvider;

    public UserServiceImp(UserRepo userRepo,TokenProvider tokenProvider) {
        this.userRepo = userRepo;
        this.tokenProvider=tokenProvider;
    }

    @Transactional
    public User save(User user) {
        return userRepo.save(user);
    }

    public User findById(int id) throws UserException {
        Optional<User> result= userRepo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserException("User not found : "+id);

    }

    public Page<User> findAll(int page,int size) {
        return userRepo.findAll(PageRequest.of(page,size, Sort.by("userId")));
    }

    @Transactional
    public void deleteById(int id) {
        userRepo.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User findByJwt(String jwt) throws UserException{
        String email= tokenProvider.getEmailFromToken(jwt);
        if(email==null){
            throw new BadCredentialsException("Invalid token");
        }
        User user=findByUsername(email);
        if (user==null){
            throw new UserException("User not found with email "+email);
        }
        return user;
    }

    public User findByUsername(String username) {
        Optional<User> result= userRepo.findByUsername(username);
        if(result.isPresent()){
            return result.get();
        }
        else{
            throw new RuntimeException("User not found : "+username);
        }
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> listUser=userRepo.searchUser(query);
        return listUser;
    }

    public PageResponse<User> getPageUsers(int page, int size) {
        List<User> listUser=userRepo.findAll();
        int totalCustomers = listUser.size();

        int start = Math.min(page * size, totalCustomers);
        int end = Math.min((page + 1) * size, totalCustomers);

        List<User> paginatedCustomers = listUser.subList(start, end);

        return new PageResponse<>(paginatedCustomers, page, size, totalCustomers);
    }





}
