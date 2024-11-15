package com.helpmeCookies.chat.controller;

import com.helpmeCookies.chat.dto.ChatMessageDto;
import com.helpmeCookies.chat.dto.ChatRoomInfo;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.entity.MessageType;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ImageStorageUtil imageStorageUtil;
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoom>> createChatRoom(
            @RequestParam String userEmail1,
            @RequestParam String userEmail2
    ) throws UserNotFoundException {
        User user1 = userService.findByEmail(userEmail1)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail1));
        User user2 = userService.findByEmail(userEmail2)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail2));

        ChatRoom chatRoom = chatRoomService.createChatRoom(user1, user2);

        String welcomeMessageContent = "Welcome, " + user2.getEmail() + "!";
        ChatMessageDto chatMessageDto = new ChatMessageDto(user1.getEmail(), welcomeMessageContent, MessageType.ENTER);
        chatMessageService.saveMessage(chatRoom.getId(), chatMessageDto);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, chatRoom));
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<ChatRoom>> getChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, chatRoom));
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<Void>> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        imageStorageUtil.deleteChatFolder("chatRoom_" + chatRoomId);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ChatRoomInfo>>> getUserChatRooms(@PathVariable Long userId) {
        List<ChatRoomInfo> chatRooms = chatRoomService.getChatRoomsByUserId(userId).stream()
                .map(chatRoom -> new ChatRoomInfo(chatRoom.getId(), chatRoom.getTitle()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, chatRooms));
    }

    @PatchMapping("/{chatRoomId}/title")
    public ResponseEntity<ApiResponse<Void>> setChatRoomTitle(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId,
            @RequestParam String title
    ) {
        chatRoomService.setChatRoomTitleIfUserMatches(chatRoomId, userId, title);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoom>>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, chatRooms));
    }
}
