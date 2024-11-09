package com.helpmeCookies.chat.controller;


import com.helpmeCookies.chat.dto.ChatRoomInfo;
import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.exception.chat.ChatRoomIdNotFoundException;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ImageStorageUtil imageStorageUtil;

    @GetMapping("/rooms/{chatRoomId}/messages")
    public List<ChatMessage> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomIdNotFoundException(chatRoomId));

        return chatMessageService.getMessagesByChatRoom(chatRoom);
    }

    @GetMapping("/rooms/user/{userId}")
    public List<Long> getChatRoomIdsByUser(@PathVariable Long userId) throws UserNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return chatMessageService.getChatRoomIdsByUser(user);
    }

    @GetMapping("/rooms/user/title/{userId}")
    public List<ChatRoomInfo> getChatRoomInfosByUserTitle(@PathVariable Long userId) throws UserNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return chatMessageService.getChatRoomInfosByUser(user);
    }

    @DeleteMapping("/messages/{id}")
    public void deleteMessage(@PathVariable Long id) {
        chatMessageService.deleteMessage(id);
    }
}

