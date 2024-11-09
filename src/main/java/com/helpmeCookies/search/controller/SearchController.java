package com.helpmeCookies.search.controller;

import com.helpmeCookies.search.dto.PopularSearchResponse;
import com.helpmeCookies.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/popular")
    public ResponseEntity<PopularSearchResponse> getPopularSearch() {
        return ResponseEntity.ok(searchService.getPopularSearch());
    }
}
