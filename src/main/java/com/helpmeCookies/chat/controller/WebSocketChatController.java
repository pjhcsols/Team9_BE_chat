package com.helpmeCookies.chat.controller;


import com.helpmeCookies.chat.dto.ChatMessageDto;
import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.entity.MessageType;
import com.helpmeCookies.chat.service.ChatMessageService;
import com.helpmeCookies.chat.service.ChatRoomService;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
public class WebSocketChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;


    public WebSocketChatController(SimpMessageSendingOperations messagingTemplate,
                                   ChatMessageService chatMessageService,
                                   ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;

    }

    @MessageMapping("/chat/{chatRoomId}")
    public void chat(@DestinationVariable Long chatRoomId, @RequestBody ChatMessageDto messageDto) {
        ChatMessage savedMessage = chatMessageService.saveMessage(chatRoomId, messageDto);

        messagingTemplate.convertAndSend("/v1/sub/chat/rooms/" + chatRoomId, savedMessage);
    }

    @MessageMapping("/chat/{chatRoomId}/file")
    @Operation(summary = "파일 전송", description = "WebSocket을 통해 특정 채팅방에 파일을 전송합니다.")
    public void sendFile(@DestinationVariable Long chatRoomId,
                         @RequestParam String fileBase64,
                         @RequestParam String userEmail) {
        try {
            ChatMessage message = chatMessageService.saveFileMessage(chatRoomId, userEmail, fileBase64);

            byte[] imageBytes = chatMessageService.convertImageUrlToBytes(message.getContent());
            String encodedContent = ChatMessageDto.getImageContent(imageBytes);

            ChatMessageDto chatMessageDto = new ChatMessageDto(
                    message.getChatRoom().getId(),
                    message.getSender().getEmail(),
                    encodedContent,
                    message.getTimestamp().toString(),
                    MessageType.IMAGE
            );

            messagingTemplate.convertAndSend("/api/sub/chat/rooms/" + chatRoomId, chatMessageDto);
        } catch (UserNotFoundException | IOException e) {

            log.error("파일 전송 중 오류 발생: " + e.getMessage(), e);
        }
    }


    @SubscribeMapping("/chat/rooms/{chatRoomId}/list")
    public List<ChatMessage> sendInitialMessages(@DestinationVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);

        return chatMessageService.getMessagesByChatRoom(chatRoom);
    }


}