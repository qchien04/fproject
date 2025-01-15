package com.example.fproject.entity.chatchit;

import com.example.fproject.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "chatroom")
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")  // Tùy chọn: Đặt tên cột nếu cần
    private Integer id;

    @Column(name = "chat_name")  // Tên cột chatName, không cho null và giới hạn độ dài
    private String chatRoomName;

    @Column(name = "chat_image")  // Tên cột chatImage, không cần nullable nếu có thể null
    private String chatRoomImage;

    @Column(name = "is_group")  // Tên cột isGroup, không cho null
    private boolean isGroup;
    //LAZY
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> admins = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private User createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<Message> messages = new ArrayList<>();

    @Override
    public String toString() {
        return "Chat [id=" + id + ", chatName=" + chatRoomName + ", chatImage=" + chatRoomImage + ", isGroup=" + isGroup
                + ", admins=" + this.getAdmins() + ", createdBy=" + this.getCreatedBy() + ", users=" + this.getUsers() +
                ", messages=" + this.getMessages()
                + "]";
    }

}
