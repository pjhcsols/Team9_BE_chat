package com.helpmeCookies.chat.controller;


import com.helpmeCookies.chat.dto.ImageMessageDTO;
import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/v1/websocket")
public class WebSocketChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ImageStorageUtil imageStorageUtil;

    public WebSocketChatController(SimpMessageSendingOperations messagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService, UserService userService, ImageStorageUtil imageStorageUtil) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.imageStorageUtil = imageStorageUtil;
    }

    @MessageMapping("/chat/{chatRoomId}")
    @Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 특정 채팅방에 텍스트 메시지를 전송합니다.")
    @Parameter(name = "chatRoomId", description = "채팅방 ID", required = true, schema = @Schema(type = "long"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 메시지를 전송했습니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content)
    })
    public void chat(@DestinationVariable Long chatRoomId,
                     @Parameter(description = "전송할 채팅 메시지", required = true) ChatMessage message) throws UserNotFoundException {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        User sender = userService.findByEmail(message.getSender().getEmail())
                .orElseThrow(() -> new UserNotFoundException("발신자를 찾을 수 없습니다: " + message.getSender().getEmail()));

        chatMessageService.saveMessage(chatRoom, sender, message.getContent());
        messagingTemplate.convertAndSend("/api/sub/chat/rooms/" + chatRoomId, message);
    }

    @MessageMapping("/chat/{chatRoomId}/file")
    @Operation(summary = "파일 전송", description = "WebSocket을 통해 특정 채팅방에 파일을 전송합니다.")
    @Parameter(name = "chatRoomId", description = "채팅방 ID", required = true, schema = @Schema(type = "long"))
    @Parameter(name = "file", description = "전송할 파일", required = true, schema = @Schema(type = "string", format = "binary"))
    @Parameter(name = "userEmail", description = "발신자 이메일", required = true, schema = @Schema(type = "string"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 파일을 전송했습니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content)
    })
    public void sendFile(@DestinationVariable Long chatRoomId,
                         @RequestParam("file") MultipartFile file,
                         @RequestParam String userEmail)
            throws UserNotFoundException, IOException {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        User sender = userService.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("발신자를 찾을 수 없습니다: " + userEmail));

        ChatMessage message = chatMessageService.saveFileMessage(chatRoom, sender, file);
        String imagePath = message.getImageUrl();
        byte[] imageBytes = java.nio.file.Files.readAllBytes(new File(imagePath).toPath());

        ImageMessageDTO imageMessageDTO = new ImageMessageDTO(message, imageBytes);
        messagingTemplate.convertAndSend("/api/sub/chat/rooms/" + chatRoomId, imageMessageDTO);
    }
}