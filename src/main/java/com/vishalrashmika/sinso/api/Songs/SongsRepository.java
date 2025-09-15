package com.vishalrashmika.sinso.api.Songs;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SongsRepository{
    private final JdbcTemplate jdbc;

    public SongsRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private static final RowMapper<Songs> SONG_ROW_MAPPER = new RowMapper<> () {
        @Override
        public Songs mapRow(ResultSet rs, int rowNum) throws SQLException{
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

    private static final RowMapper<SongsSummary> SONGS_SUMMARY_ROW_MAPPER = new RowMapper<> () {
        @Override
        public SongsSummary mapRow(ResultSet rs, int rowNum) throws SQLException{
            return new SongsSummary(
                rs.getString("songId"),
                rs.getString("songName"),
                rs.getString("songNameSinhala"),
                rs.getString("artistId"),
                rs.getString("lyricId")
            );
        }
    };

    public List<SongsSummary> findAll(){
        return jdbc.query(
                "SELECT s.SongID, s.SongName, s.SongNameSinhala, s.ArtistID, l.LyricID FROM Songs s LEFT JOIN Lyrics l ON s.SongID = l.SongID ORDER BY s.ID",
                SONGS_SUMMARY_ROW_MAPPER
        );
    }

    public Songs findById(String songId) {
        return jdbc.query(
                "SELECT s.ID, s.SongID, s.SongName, s.SongNameSinhala, s.Composer, s.Lyricist, a.ArtistID, a.ArtistName, a.ArtistNameSinhala, l.LyricID, l.LyricContent, l.LyricContentSinhala FROM Songs s LEFT JOIN Artists a ON s.ArtistID = a.ArtistID LEFT JOIN Lyrics l ON s.SongID = l.SongID WHERE s.SongID = ?",
                SONG_ROW_MAPPER,
                songId
        ).stream().findFirst().orElse(null);
    }
}