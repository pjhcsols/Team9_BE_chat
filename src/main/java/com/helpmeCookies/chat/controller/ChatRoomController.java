package com.helpmeCookies.chat.controller;


import com.helpmeCookies.chat.dto.ChatMessageDto;
import com.helpmeCookies.chat.dto.ChatRoomInfo;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.entity.MessageType;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ImageStorageUtil imageStorageUtil;
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ChatRoom createChatRoom(
            @RequestParam String userEmail1,
            @RequestParam String userEmail2
    ) throws UserNotFoundException {
        User user1 = userService.findByEmail(userEmail1)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail1));
        User user2 = userService.findByEmail(userEmail2)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail2));

        ChatRoom chatRoom = chatRoomService.createChatRoom(user1, user2);

        // 환영 메시지 생성
        String welcomeMessageContent = "Welcome, " + user2.getEmail() + "!";
        ChatMessageDto chatMessageDto = new ChatMessageDto(user1.getEmail(), welcomeMessageContent, MessageType.ENTER);
        chatMessageService.saveMessage(chatRoom.getId(), chatMessageDto);

        return chatRoom;
    }

    @GetMapping("/{chatRoomId}")
    public ChatRoom getChatRoom(@PathVariable Long chatRoomId) {
        return chatRoomService.getChatRoomById(chatRoomId);
    }

    @DeleteMapping("/{chatRoomId}")
    public void deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);

        imageStorageUtil.deleteChatFolder("chatRoom_" + chatRoomId);
    }

    @GetMapping("/user/{userId}")
    public List<ChatRoomInfo> getUserChatRooms(@PathVariable Long userId) {
        return chatRoomService.getChatRoomsByUserId(userId).stream()
                .map(chatRoom -> new ChatRoomInfo(chatRoom.getId(), chatRoom.getTitle()))
                .collect(Collectors.toList());
    }

    @PatchMapping("/{chatRoomId}/title")
    public void setChatRoomTitle(@PathVariable Long chatRoomId,
                                 @RequestParam Long userId,
                                 @RequestParam String title) {

        chatRoomService.setChatRoomTitleIfUserMatches(chatRoomId, userId, title);
    }

    @GetMapping
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomService.getAllChatRooms();
    }

}

