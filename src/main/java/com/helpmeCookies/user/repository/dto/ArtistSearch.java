package com.helpmeCookies.user.repository.dto;

public interface ArtistSearch {
    Long getId();
    String getNickname();
    String getArtistImageUrl();
    Long getTotalFollowers();
    Long getTotalLikes();
}
