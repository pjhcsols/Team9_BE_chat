package com.helpmeCookies.chat.controller;

import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.exception.chat.ChatRoomIdNotFoundException;
import com.helpmeCookies.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ImageStorageUtil imageStorageUtil;

    @GetMapping("/messages")
    public List<ChatMessage> getAllMessages() {
        return chatMessageService.getAllMessages();
    }

    @GetMapping("/rooms/{chatRoomId}/messages")
    public List<ChatMessage> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomIdNotFoundException(chatRoomId));

        return chatMessageService.getMessagesByChatRoom(chatRoom);
    }

    @PostMapping("/image/convert")
    public ResponseEntity<byte[]> convertImageUrlToBase64(@RequestParam String imagePath) throws IOException {

        byte[] imageBytes = chatMessageService.convertImageUrlToBytes(imagePath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpeg");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/messages/{id}")
    public void deleteMessage(@PathVariable Long id) {
        chatMessageService.deleteMessage(id);
    }
}

