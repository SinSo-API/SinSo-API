package com.vishalrashmika.sinso.api.Search;

/**
 * DTO class for enhanced search filter criteria
 */
public class SearchFilterCriteria {

    // Basic search parameters (existing functionality)
    private String query;           // Global search query
    private String artist;          // Artist name filter
    private String title;           // Song title filter
    private String lyrics;          // Lyrics content filter

    // Enhanced filter parameters
    private String artistId;         // Specific artist ID filter
    private String composer;        // Composer name filter
    private String lyricist;        // Lyricist name filter

    // Boolean filters
    private Boolean hasLyrics;      // Filter songs with/without lyrics
    private Boolean hasComposer;    // Filter songs with/without composer
    private Boolean hasLyricist;    // Filter songs with/without lyricist

    // Language filter
    private SearchLanguage language; // Language preference filter

    // Match type filter
    private SearchMatchType matchType; // Search match type

    // Sorting options
    private SearchSortBy sortBy;     // Field to sort by
    private SearchSortOrder sortOrder; // Sort order

    // Pagination (existing functionality)
    private int page = 0;           // Page number
    private int size = 20;          // Page size

    // Constructors
    public SearchFilterCriteria() {}

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLyrics() { return lyrics; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }

    public String getArtistId() { return artistId; }
    public void setArtistId(String artistId) { this.artistId = artistId; }

    public String getComposer() { return composer; }
    public void setComposer(String composer) { this.composer = composer; }

    public String getLyricist() { return lyricist; }
    public void setLyricist(String lyricist) { this.lyricist = lyricist; }

    public Boolean getHasLyrics() { return hasLyrics; }
    public void setHasLyrics(Boolean hasLyrics) { this.hasLyrics = hasLyrics; }

    public Boolean getHasComposer() { return hasComposer; }
    public void setHasComposer(Boolean hasComposer) { this.hasComposer = hasComposer; }

    public Boolean getHasLyricist() { return hasLyricist; }
    public void setHasLyricist(Boolean hasLyricist) { this.hasLyricist = hasLyricist; }

    public SearchLanguage getLanguage() { return language; }
    public void setLanguage(SearchLanguage language) { this.language = language; }

    public SearchMatchType getMatchType() { return matchType; }
    public void setMatchType(SearchMatchType matchType) { this.matchType = matchType; }

    public SearchSortBy getSortBy() { return sortBy; }
    public void setSortBy(SearchSortBy sortBy) { this.sortBy = sortBy; }

    public SearchSortOrder getSortOrder() { return sortOrder; }
    public void setSortOrder(SearchSortOrder sortOrder) { this.sortOrder = sortOrder; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = Math.max(0, page); }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = Math.min(Math.max(1, size), 100); } // Limit max page size

    /**
     * Checks if any search criteria is provided
     */
    public boolean hasAnySearchCriteria() {
        return (query != null && !query.trim().isEmpty()) ||
               (artist != null && !artist.trim().isEmpty()) ||
               (title != null && !title.trim().isEmpty()) ||
               (lyrics != null && !lyrics.trim().isEmpty()) ||
               (artistId != null && !artistId.trim().isEmpty()) ||
               (composer != null && !composer.trim().isEmpty()) ||
               (lyricist != null && !lyricist.trim().isEmpty());
    }

    /**
     * Checks if any filter criteria is provided
     */
    public boolean hasAnyFilterCriteria() {
        return hasLyrics != null || hasComposer != null || hasLyricist != null ||
               language != null || matchType != null;
    }
}
