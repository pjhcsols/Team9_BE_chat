package com.helpmeCookies.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.repository.querydsl.UserCustomRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
	Optional<User> findById(Long id);
	Optional<User> findByUserInfoEmail(String email);
	Optional<User> findByNickname(String nickname);
	Optional<User> findByUserInfoPhone(String phone);
}
