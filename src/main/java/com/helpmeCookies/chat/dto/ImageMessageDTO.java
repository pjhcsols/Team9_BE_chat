package com.helpmeCookies.chat.dto;

import gift.domain.chat.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageMessageDTO {
    private ChatMessage chatMessage;
    private byte[] imageData;

    public ImageMessageDTO(ChatMessage chatMessage, byte[] imageData) {
        this.chatMessage = chatMessage;
        this.imageData = imageData;
    }
}

