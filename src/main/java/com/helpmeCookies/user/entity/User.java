package com.helpmeCookies.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.helpmeCookies.global.entity.BaseTimeEntity;
import com.helpmeCookies.product.entity.HashTag;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users") // 예약어 회피
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private String nickname;

	private String userImageUrl;

	@Embedded
	private UserInfo userInfo;

	public void updateUserCommonInfo(String nickname, String userImageUrl) {
		this.nickname = nickname;
		this.userImageUrl = userImageUrl;
	}

	public void updateUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	private User setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		return this;
	}

	public String getEmail() {
		return userInfo.getEmail();
	}
}