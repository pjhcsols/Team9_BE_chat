package com.helpmeCookies.chat.controller;


import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

        String welcomeMessageContent = "Welcome, " + user2.getEmail() + "!";
        chatMessageService.saveMessage(chatRoom, user1, welcomeMessageContent);

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
}
