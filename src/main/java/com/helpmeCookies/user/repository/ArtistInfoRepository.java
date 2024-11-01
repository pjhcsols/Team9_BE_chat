package com.helpmeCookies.user.repository;

import com.helpmeCookies.user.entity.ArtistInfo;
import com.helpmeCookies.user.repository.dto.ArtistSearch;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistInfoRepository extends JpaRepository<ArtistInfo, Long> {
	Optional<ArtistInfo> findByUserId(Long userId);
	boolean existsByUserId(Long userId);

	@Query(value = "SELECT a.id, a.nickname, a.artist_image_url, a.total_followers, a.total_likes " +
		"FROM artist_info a " +
		"WHERE MATCH(a.nickname) AGAINST (:query IN BOOLEAN MODE)",
		countQuery = "SELECT COUNT(*) " +
			"FROM artist_info a " +
			"WHERE MATCH(a.nickname) AGAINST (:query IN BOOLEAN MODE)",
		nativeQuery = true) // Index 사용
	Page<ArtistSearch> findByNicknameWithIdx(@Param("query") String query, Pageable pageable);
}
