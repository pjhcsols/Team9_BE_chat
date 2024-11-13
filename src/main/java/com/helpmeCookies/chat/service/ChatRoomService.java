package com.helpmeCookies.chat.service;


import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.repository.ChatRoomRepository;
import com.helpmeCookies.global.exception.chat.ChatRoomNotFoundException;
import com.helpmeCookies.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(User user1, User user2) {
        ChatRoom chatRoom = new ChatRoom(user1, user2);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoom(User user1, User user2) {
        return chatRoomRepository.findByUser1AndUser2(user1, user2);
    }

    public Optional<ChatRoom> findById(Long id) {
        return chatRoomRepository.findById(id);
    }

    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found with ID: " + id));
    }


    // deleteChatRoom 메서드 추가
    public void deleteChatRoom(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new ChatRoomNotFoundException("Chat room not found with ID: " + id));
        chatRoomRepository.delete(chatRoom);
    }
}
