package com.vishalrashmika.sinso.api.Search;

/**
 * Enum representing fields that can be used for sorting search results
 */
public enum SearchSortBy {
    SONG_NAME("songName"),
    SONG_NAME_SINHALA("songNameSinhala"),
    ARTIST_NAME("artistName"),
    ARTIST_NAME_SINHALA("artistNameSinhala"),
    COMPOSER("composer"),
    LYRICIST("lyricist");

    private final String value;

    SearchSortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchSortBy fromString(String value) {
        for (SearchSortBy sortBy : SearchSortBy.values()) {
            if (sortBy.value.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        return SONG_NAME; // Default fallback
    }
}
