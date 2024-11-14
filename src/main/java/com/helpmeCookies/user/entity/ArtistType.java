package com.helpmeCookies.user.entity;

public enum ArtistType {
	USER("User"), BUSINESS("BusinessArtist"), STUDENT("StudentArtist");

	private final String type;

	ArtistType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
