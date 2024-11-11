package com.helpmeCookies.global.exception.chat;

public class ChatRoomIdNotFoundException extends RuntimeException {
    public ChatRoomIdNotFoundException(Long chatRoomId) {
        super("Chat room with id " + chatRoomId + " not found.");
    }
}
