package com.helpmeCookies.user.dto;

import com.helpmeCookies.user.entity.ArtistType;

import lombok.Builder;

@Builder
public record UserTypeDto(
	String role,
	ArtistType userType
) {
}
