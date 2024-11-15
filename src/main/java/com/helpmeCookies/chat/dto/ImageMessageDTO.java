package com.helpmeCookies.chat.dto;


import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageMessageDTO {
    private ChatMessage chatMessage;
    private String imageBase64; // Base64로 인코딩된 이미지 데이터
    private MessageType messageType; // 메시지 타입

    public ImageMessageDTO(ChatMessage chatMessage, byte[] imageData, MessageType messageType) {
        this.chatMessage = chatMessage;
        this.imageBase64 = "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(imageData);
        this.messageType = messageType;
    }
}

