package com.example.fproject.repository;

import com.example.fproject.entity.auth.User;
import com.example.fproject.entity.chatchit.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom,Integer> {

    @Query("select c from ChatRoom c join c.users u where u.id=:id")
    public List<ChatRoom> findChatByUserId(@Param("id") Integer userId);


    @Query("select c from ChatRoom c where c.isGroup=false and :user member of c.users and :reqUser member of c.users")
    public ChatRoom findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);

}
