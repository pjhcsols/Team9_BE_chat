package com.helpmeCookies.chat.repository;


import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUser1AndUser2(User user1, User user2);
}
