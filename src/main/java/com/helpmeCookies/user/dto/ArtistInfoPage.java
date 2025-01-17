package com.helpmeCookies.user.dto;

import com.helpmeCookies.user.repository.dto.ArtistSearch;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

public class ArtistInfoPage {

    public record Info(
        Long id,
        String nickname,
        String artistImageUrl,
        Long totalFollowers,
        Long totalLikes,
        Boolean isFollowing
    ) {

        private static Info from(ArtistSearch artistSearch, boolean isFollowing) {
            return new Info(
                artistSearch.getId(),
                artistSearch.getNickname(),
                artistSearch.getArtistImageUrl(),
                artistSearch.getTotalFollowers(),
                artistSearch.getTotalLikes(),
                isFollowing
            );
        }

        public static List<Info> of(List<ArtistSearch> content, Set<Long> followingIds) {
            return content.stream()
                .map(artistSearch -> from(artistSearch, followingIds.contains(artistSearch.getId())))
                .toList();
        }
    }

    public record Paging (
        boolean hasNext,
        List<Info> artists
    ) {
        public static Paging of(Page<ArtistSearch> artistPage, Set<Long> followingIds) {
            return new Paging(
                artistPage.hasNext(),
                Info.of(artistPage.getContent(), followingIds)
            );
        }
    }

}
