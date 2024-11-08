package com.helpmeCookies.user.repository;

import com.helpmeCookies.user.entity.ArtistInfo;
import com.helpmeCookies.user.entity.Social;
import com.helpmeCookies.user.entity.User;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
	Boolean existsByFollowerAndFollowing(User follower, ArtistInfo following);
	Optional<Social> findByFollowerAndFollowing(User follower, ArtistInfo following);

	@Query("SELECT s.following.id FROM Social s WHERE s.follower.id = :userId")
	Set<Long> findFollowingIdByFollowerId(Long userId);
}
