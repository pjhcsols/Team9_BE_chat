package com.helpmeCookies.user.dto;

import com.helpmeCookies.user.repository.dto.ArtistSearch;
import java.util.List;
import org.springframework.data.domain.Page;

public class ArtistInfoPage {

    public record Info(
        Long id,
        String nickname,
        String artistImageUrl,
        Long totalFollowers,
        Long totalLikes
    ) {

        private static Info from(ArtistSearch artistSearch) {
            return new Info(
                artistSearch.getId(),
                artistSearch.getNickname(),
                artistSearch.getArtistImageUrl(),
                artistSearch.getTotalFollowers(),
                artistSearch.getTotalLikes()
            );
        }

        public static List<Info> of(List<ArtistSearch> content) {
            return content.stream()
                .map(Info::from)
                .toList();
        }
    }

    public record Paging (
        boolean hasNext,
        List<Info> artists
    ) {
        public static Paging from(Page<ArtistSearch> artistPage) {
            return new Paging(
                artistPage.hasNext(),
                Info.of(artistPage.getContent())
            );
        }
    }

}
