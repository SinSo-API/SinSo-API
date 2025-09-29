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
}