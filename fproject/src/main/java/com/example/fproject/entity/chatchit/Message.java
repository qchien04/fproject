package com.example.fproject.entity.chatchit;

import com.example.fproject.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")  // Tùy chọn: Đặt tên cột nếu cần
    private Integer id;

    @Column(name = "content")  // Đặt tên cột, không cho phép null, giới hạn độ dài
    private String content;

    @Column(name = "timestamp")  // Đặt tên cột và không cho phép null
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chat_id")  // Tùy chọn: Đặt tên cột khóa ngoại
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Tùy chọn: Đặt tên cột khóa ngoại
    private User user;


    @Override
    public String toString() {
        return "Message [id=" + id + ", content=" + content + ", timestamp=" + timestamp + ", chat=" + chatRoom + ", user="
                + user + "]";
    }

}
