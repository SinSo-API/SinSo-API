package com.vishalrashmika.sinso.api.Search;

import com.vishalrashmika.sinso.api.Songs.Songs;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class SearchService {
    
    private final JdbcTemplate jdbcTemplate;
    
    public SearchService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private static final RowMapper<Songs> SEARCH_SONG_ROW_MAPPER = new RowMapper<>() {
        @Override
        public Songs mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Songs(
                rs.getString("songId"),
                rs.getString("songName"),
                rs.getString("songNameSinhala"),
                rs.getString("artistId"),
                rs.getString("artistName"),
                rs.getString("artistNameSinhala"),
                rs.getString("composer"),
                rs.getString("lyricist"),
                rs.getString("lyricId"),
                rs.getString("lyricContent"),
                rs.getString("lyricContentSinhala")
            );
        }
    };
    
    // Main search method that handles both global and field-specific searches
    public List<Songs> searchSongs(String query, String artist, String title, String lyrics) {
        if (query != null && !query.trim().isEmpty()) {
            return searchSongsGlobally(query.trim());
        }
        
        return searchSongsSpecific(artist, title, lyrics);
    }
    
    // Global search across all fields using a single query parameter
    private List<Songs> searchSongsGlobally(String query) {
        String sql = """
            SELECT DISTINCT 
                s.SongID as songId,
                s.SongName as songName, 
                s.SongNameSinhala as songNameSinhala,
                s.Composer as composer,
                s.Lyricist as lyricist,
                a.ArtistID as artistId,
                a.ArtistName as artistName, 
                a.ArtistNameSinhala as artistNameSinhala,
                l.LyricID as lyricId,
                l.LyricContent as lyricContent, 
                l.LyricContentSinhala as lyricContentSinhala
            FROM Songs s 
            LEFT JOIN Artists a ON s.ArtistID = a.ArtistID 
            LEFT JOIN Lyrics l ON s.SongID = l.SongID 
            WHERE 
                LOWER(s.SongName) LIKE LOWER(?) OR
                LOWER(s.SongNameSinhala) LIKE LOWER(?) OR
                LOWER(a.ArtistName) LIKE LOWER(?) OR
                LOWER(a.ArtistNameSinhala) LIKE LOWER(?) OR
                LOWER(s.Composer) LIKE LOWER(?) OR
                LOWER(s.Lyricist) LIKE LOWER(?) OR
                LOWER(l.LyricContent) LIKE LOWER(?) OR
                LOWER(l.LyricContentSinhala) LIKE LOWER(?)
            ORDER BY s.SongName
            """;
        
        String searchPattern = "%" + query + "%";
        return jdbcTemplate.query(sql, SEARCH_SONG_ROW_MAPPER, 
            searchPattern, searchPattern, searchPattern, searchPattern,
            searchPattern, searchPattern, searchPattern, searchPattern);
    }
    
    
    // Field-specific search with individual parameters
    private List<Songs> searchSongsSpecific(String artist, String title, String lyrics) {
        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        
        String baseSql = """
            SELECT DISTINCT 
                s.SongID as songId,
                s.SongName as songName, 
                s.SongNameSinhala as songNameSinhala,
                s.Composer as composer,
                s.Lyricist as lyricist,
                a.ArtistID as artistId,
                a.ArtistName as artistName, 
                a.ArtistNameSinhala as artistNameSinhala,
                l.LyricID as lyricId,
                l.LyricContent as lyricContent, 
                l.LyricContentSinhala as lyricContentSinhala
            FROM Songs s 
            LEFT JOIN Artists a ON s.ArtistID = a.ArtistID 
            LEFT JOIN Lyrics l ON s.SongID = l.SongID 
            """;
        
        if (title != null && !title.trim().isEmpty()) {
            conditions.add("(LOWER(s.SongName) LIKE LOWER(?) OR LOWER(s.SongNameSinhala) LIKE LOWER(?))");
            parameters.add("%" + title.trim() + "%");
            parameters.add("%" + title.trim() + "%");
        }
        
        if (artist != null && !artist.trim().isEmpty()) {
            conditions.add("(LOWER(a.ArtistName) LIKE LOWER(?) OR LOWER(a.ArtistNameSinhala) LIKE LOWER(?))");
            parameters.add("%" + artist.trim() + "%");
            parameters.add("%" + artist.trim() + "%");
        }
        
        if (lyrics != null && !lyrics.trim().isEmpty()) {
            conditions.add("(LOWER(l.LyricContent) LIKE LOWER(?) OR LOWER(l.LyricContentSinhala) LIKE LOWER(?))");
            parameters.add("%" + lyrics.trim() + "%");
            parameters.add("%" + lyrics.trim() + "%");
        }
        
        if (conditions.isEmpty()) {
            return Collections.emptyList();
        }
        
        String finalSql = baseSql + " WHERE " + String.join(" AND ", conditions) + " ORDER BY s.SongName";
        
        return jdbcTemplate.query(finalSql, SEARCH_SONG_ROW_MAPPER, parameters.toArray());
    }

    // Enhanced search method with comprehensive filtering capabilities
    public List<Songs> searchSongsWithFilters(SearchFilterCriteria criteria) {
        if (!criteria.hasAnySearchCriteria()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder();
        List<Object> parameters = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        // Base SELECT query
        sql.append("""
            SELECT DISTINCT
                s.SongID as songId,
                s.SongName as songName,
                s.SongNameSinhala as songNameSinhala,
                s.Composer as composer,
                s.Lyricist as lyricist,
                a.ArtistID as artistId,
                a.ArtistName as artistName,
                a.ArtistNameSinhala as artistNameSinhala,
                l.LyricID as lyricId,
                l.LyricContent as lyricContent,
                l.LyricContentSinhala as lyricContentSinhala
            FROM Songs s
            LEFT JOIN Artists a ON s.ArtistID = a.ArtistID
            LEFT JOIN Lyrics l ON s.SongID = l.SongID
            """);

        // Build WHERE conditions based on search criteria
        buildSearchConditions(criteria, conditions, parameters);

        // Build WHERE conditions based on filter criteria
        buildFilterConditions(criteria, conditions, parameters);

        // Add WHERE clause if conditions exist
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        // Add ORDER BY clause
        buildOrderByClause(criteria, sql);

        return jdbcTemplate.query(sql.toString(), SEARCH_SONG_ROW_MAPPER, parameters.toArray());
    }

    // Build search conditions (query, artist, title, lyrics)
    private void buildSearchConditions(SearchFilterCriteria criteria, List<String> conditions, List<Object> parameters) {
        String matchType = criteria.getMatchType() != null ? criteria.getMatchType().getValue() : "contains";

        // Global search query
        if (criteria.getQuery() != null && !criteria.getQuery().trim().isEmpty()) {
            String query = criteria.getQuery().trim();
            String searchPattern = getSearchPattern(query, matchType);

            conditions.add("(" +
                "LOWER(s.SongName) LIKE LOWER(?) OR " +
                "LOWER(s.SongNameSinhala) LIKE LOWER(?) OR " +
                "LOWER(a.ArtistName) LIKE LOWER(?) OR " +
                "LOWER(a.ArtistNameSinhala) LIKE LOWER(?) OR " +
                "LOWER(s.Composer) LIKE LOWER(?) OR " +
                "LOWER(s.Lyricist) LIKE LOWER(?) OR " +
                "LOWER(l.LyricContent) LIKE LOWER(?) OR " +
                "LOWER(l.LyricContentSinhala) LIKE LOWER(?)" +
                ")");

            for (int i = 0; i < 8; i++) {
                parameters.add(searchPattern);
            }
        }

        // Field-specific searches
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            String title = criteria.getTitle().trim();
            String searchPattern = getSearchPattern(title, matchType);

            conditions.add("(" +
                "LOWER(s.SongName) LIKE LOWER(?) OR " +
                "LOWER(s.SongNameSinhala) LIKE LOWER(?)" +
                ")");

            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        if (criteria.getArtist() != null && !criteria.getArtist().trim().isEmpty()) {
            String artist = criteria.getArtist().trim();
            String searchPattern = getSearchPattern(artist, matchType);

            conditions.add("(" +
                "LOWER(a.ArtistName) LIKE LOWER(?) OR " +
                "LOWER(a.ArtistNameSinhala) LIKE LOWER(?)" +
                ")");

            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        if (criteria.getLyrics() != null && !criteria.getLyrics().trim().isEmpty()) {
            String lyrics = criteria.getLyrics().trim();
            String searchPattern = getSearchPattern(lyrics, matchType);

            conditions.add("(" +
                "LOWER(l.LyricContent) LIKE LOWER(?) OR " +
                "LOWER(l.LyricContentSinhala) LIKE LOWER(?)" +
                ")");

            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }
    }

    // Build filter conditions (composer, lyricist, boolean filters, etc.)
    private void buildFilterConditions(SearchFilterCriteria criteria, List<String> conditions, List<Object> parameters) {
        // Artist ID filter
        if (criteria.getArtistId() != null && !criteria.getArtistId().trim().isEmpty()) {
            conditions.add("a.ArtistID = ?");
            parameters.add(criteria.getArtistId().trim());
        }

        // Composer filter
        if (criteria.getComposer() != null && !criteria.getComposer().trim().isEmpty()) {
            String composer = criteria.getComposer().trim();
            conditions.add("(" +
                "LOWER(s.Composer) LIKE LOWER(?) OR " +
                "LOWER(s.Composer) = LOWER(?)" +
                ")");

            parameters.add("%" + composer + "%");
            parameters.add(composer);
        }

        // Lyricist filter
        if (criteria.getLyricist() != null && !criteria.getLyricist().trim().isEmpty()) {
            String lyricist = criteria.getLyricist().trim();
            conditions.add("(" +
                "LOWER(s.Lyricist) LIKE LOWER(?) OR " +
                "LOWER(s.Lyricist) = LOWER(?)" +
                ")");

            parameters.add("%" + lyricist + "%");
            parameters.add(lyricist);
        }

        // Boolean filters
        if (Boolean.TRUE.equals(criteria.getHasLyrics())) {
            conditions.add("l.LyricID IS NOT NULL");
        } else if (Boolean.FALSE.equals(criteria.getHasLyrics())) {
            conditions.add("l.LyricID IS NULL");
        }

        if (Boolean.TRUE.equals(criteria.getHasComposer())) {
            conditions.add("s.Composer IS NOT NULL AND s.Composer != ''");
        } else if (Boolean.FALSE.equals(criteria.getHasComposer())) {
            conditions.add("(s.Composer IS NULL OR s.Composer = '')");
        }

        if (Boolean.TRUE.equals(criteria.getHasLyricist())) {
            conditions.add("s.Lyricist IS NOT NULL AND s.Lyricist != ''");
        } else if (Boolean.FALSE.equals(criteria.getHasLyricist())) {
            conditions.add("(s.Lyricist IS NULL OR s.Lyricist = '')");
        }

        // Language filter
        if (criteria.getLanguage() != null) {
            switch (criteria.getLanguage()) {
                case ENGLISH:
                    conditions.add("(s.SongName IS NOT NULL OR a.ArtistName IS NOT NULL OR l.LyricContent IS NOT NULL)");
                    conditions.add("(s.SongNameSinhala IS NULL OR s.SongNameSinhala = '')");
                    conditions.add("(a.ArtistNameSinhala IS NULL OR a.ArtistNameSinhala = '')");
                    conditions.add("(l.LyricContentSinhala IS NULL OR l.LyricContentSinhala = '')");
                    break;
                case SINHALA:
                    conditions.add("(s.SongNameSinhala IS NOT NULL OR a.ArtistNameSinhala IS NOT NULL OR l.LyricContentSinhala IS NOT NULL)");
                    break;
                case BOTH:
                    // No additional conditions needed for BOTH
                    break;
            }
        }
    }

    // Build ORDER BY clause
    private void buildOrderByClause(SearchFilterCriteria criteria, StringBuilder sql) {
        SearchSortBy sortBy = criteria.getSortBy() != null ? criteria.getSortBy() : SearchSortBy.SONG_NAME;
        SearchSortOrder sortOrder = criteria.getSortOrder() != null ? criteria.getSortOrder() : SearchSortOrder.ASC;

        String sortField;
        switch (sortBy) {
            case SONG_NAME_SINHALA:
                sortField = "s.SongNameSinhala";
                break;
            case ARTIST_NAME:
                sortField = "a.ArtistName";
                break;
            case ARTIST_NAME_SINHALA:
                sortField = "a.ArtistNameSinhala";
                break;
            case COMPOSER:
                sortField = "s.Composer";
                break;
            case LYRICIST:
                sortField = "s.Lyricist";
                break;
            default:
                sortField = "s.SongName";
                break;
        }

        sql.append(" ORDER BY ").append(sortField);
        sql.append(sortOrder == SearchSortOrder.DESC ? " DESC" : " ASC");
        sql.append(", s.SongID"); // Secondary sort for consistent results
    }

    // Get search pattern based on match type
    private String getSearchPattern(String query, String matchType) {
        return switch (matchType.toLowerCase()) {
            case "exact" -> query;
            case "starts_with" -> query + "%";
            default -> "%" + query + "%";
        };
    }
}