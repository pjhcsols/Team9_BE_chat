package com.helpmeCookies.search.dto;

import java.util.List;

public record PopularSearchResponse (
    List<String> popularSearch
) {
}
