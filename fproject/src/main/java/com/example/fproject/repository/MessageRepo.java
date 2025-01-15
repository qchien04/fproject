package com.example.fproject.repository;


import com.example.fproject.entity.chatchit.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Integer> {
    @Query("select m from Message m join m.chatRoom c where c.id=:chatId")
    public List<Message> findByChatRoomId(@Param("chatId") Integer chatId);
}
