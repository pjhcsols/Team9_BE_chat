package com.helpmeCookies.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpmeCookies.global.exception.user.DuplicateResourceException;
import com.helpmeCookies.global.exception.user.ResourceNotFoundException;
import com.helpmeCookies.global.utils.AwsS3FileUtils;
import com.helpmeCookies.user.dto.UserCommonInfoDto;
import com.helpmeCookies.user.dto.UserDto;
import com.helpmeCookies.user.dto.UserInfoDto;
import com.helpmeCookies.user.dto.UserTypeDto;
import com.helpmeCookies.user.dto.response.UserFollowingRes;
import com.helpmeCookies.user.entity.ArtistInfo;
import com.helpmeCookies.user.entity.Social;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.entity.UserInfo;
import com.helpmeCookies.user.repository.ArtistInfoRepository;
import com.helpmeCookies.user.repository.SocialRepository;
import com.helpmeCookies.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final ArtistInfoRepository artistInfoRepository;
	private final SocialRepository socialRepository;
	private final AwsS3FileUtils awsS3FileUtils;


	@Transactional
	public UserDto getUserInfo(Long userId) {
		User user = checkUser(userId);

		return UserDto.fromEntity(user);
	}

	@Transactional
	public UserDto updateUser(UserCommonInfoDto userCommonInfoDto, UserInfoDto userInfoDto, Long userId) {

		User existingUser = checkUser(userId);

		userRepository.findByNickname(userCommonInfoDto.nickname())
			.ifPresent(user -> {
				if(!user.getId().equals(userId)) {
					throw new DuplicateResourceException("이미 사용중인 닉네임입니다.");
				}
			});

		UserInfo userInfo = userInfoDto.toEntity();
		existingUser.updateUserCommonInfo(userCommonInfoDto.nickname(), userCommonInfoDto.ImageUrl());
		existingUser.updateUserInfo(userInfo);

		return UserDto.fromEntity(userRepository.save(existingUser));
	}


	@Transactional
	public UserTypeDto getUserType(Long userId) {
		String usertype = artistInfoRepository.findByUserId(userId)
			.map(artistInfo -> artistInfo.getArtistType().getType()) // 값이 있을 때 처리
			.orElse("User"); // 값이 없을 때 기본값

		return UserTypeDto.builder()
			.userType(usertype)
			.build();
	}

	@Transactional
	public Page<UserFollowingRes> getFollowingWithPaging(Long userId, Pageable pageable) {
		return userRepository.findFollowingUsers(userId, pageable)
			.map(UserFollowingRes::fromDto);
	}

	@Transactional
	public void followArtist(Long userId, Long artistId) {
		User user = checkUser(userId);
		ArtistInfo artistInfo = getArtistInfo(artistId);

		if(isFollowing(user, artistInfo)) {
			throw new DuplicateResourceException("이미 팔로우한 아티스트입니다.");
		}

		Social social = Social.builder()
			.follower(user)
			.following(artistInfo)
			.build();

		socialRepository.save(social);
	}

	@Transactional
	public void unfollowArtist(Long userId, Long artistId) {
		User user = checkUser(userId);
		ArtistInfo artistInfo = getArtistInfo(artistId);

		if(isFollowing(user, artistInfo)) {
			Social social = getSocial(user, artistInfo);
			socialRepository.delete(social);
		}
	}

	@Transactional
	public boolean checkSignup(String email) {
		return userRepository.findByUserInfoPhone(email).isPresent();
	}

	private User checkUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 유저입니다."));
	}

	private Social getSocial(User user, ArtistInfo artistInfo) {
		return socialRepository.findByFollowerAndFollowing(user, artistInfo)
			.orElseThrow(() -> new ResourceNotFoundException("팔로우하지 않은 아티스트입니다."));
	}

	private boolean isFollowing(User user, ArtistInfo artistInfo) {
		return socialRepository.existsByFollowerAndFollowing(user, artistInfo);
	}

	private ArtistInfo getArtistInfo(Long artistId) {
		ArtistInfo artistInfo = artistInfoRepository.findByUserId(artistId)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 아티스트입니다."));
		return artistInfo;
	}

	public Optional<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByUserInfoEmail(email);
	}
}
