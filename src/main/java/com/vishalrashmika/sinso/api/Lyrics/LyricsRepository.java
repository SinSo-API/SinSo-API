package com.vishalrashmika.sinso.api.Lyrics;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LyricsRepository {
    private final JdbcTemplate jdbc;

    public LyricsRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private static final RowMapper<Lyrics> LYRIC_ROW_MAPPER = new RowMapper<> () {
        @Override
        public Lyrics mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Lyrics(
                rs.getString("lyricId"),
                rs.getString("songId"),
                rs.getString("songName"),
                rs.getString("songNameSinhala"),
                rs.getString("artistId"),
                rs.getString("artistName"),
                rs.getString("artistNameSinhala"),
                rs.getString("lyricContent"),
                rs.getString("lyricContentSinhala")
            );
        }
    };

    public Lyrics findByLyricId(String lyricId){
        return jdbc.query(
            "SELECT l.LyricID, s.SongID, s.SongName, s.SongNameSinhala, a.ArtistID, a.ArtistName, a.ArtistNameSinhala, l.LyricContent, l.LyricContentSinhala FROM Songs s JOIN Lyrics l ON s.SongID = l.SongID JOIN Artists a ON s.ArtistID = a.ArtistID WHERE l.LyricID = ?",
            LYRIC_ROW_MAPPER,
            lyricId
        ).stream().findFirst().orElse(null);
    }
}
