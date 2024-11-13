package com.helpmeCookies.chat.dto;

public class ChatRoomInfo {
    private Long id;
    private String title;

    public ChatRoomInfo(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}