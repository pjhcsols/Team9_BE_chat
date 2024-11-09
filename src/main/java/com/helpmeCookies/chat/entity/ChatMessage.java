package com.helpmeCookies.chat.entity;


import com.helpmeCookies.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    //private MessageType type;

    private String content; //내용

    @Column(name = "image_url")
    private String imageUrl; // 이미지 URL

    private LocalDateTime timestamp;

    protected ChatMessage() {}

    public ChatMessage(ChatRoom chatRoom, User sender, String content) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(ChatRoom chatRoom, User sender, String content, String imageUrl) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = LocalDateTime.now();
    }



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
