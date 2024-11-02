package com.helpmeCookies.user.dto.request;

import java.util.List;

import com.helpmeCookies.product.entity.HashTag;
import com.helpmeCookies.user.dto.UserDto;
import com.helpmeCookies.user.dto.UserInfoDto;

public record UserReq(
	String name,
	String email,
	String birthdate,
	String phone,
	String address,
	List<HashTag> hashTags,
	String userImageUrl,
	String nickname
) {

}