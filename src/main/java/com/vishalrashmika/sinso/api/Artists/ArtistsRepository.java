package com.vishalrashmika.sinso.api.Artists;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Repository
public class ArtistsRepository {
    private final JdbcTemplate jdbc;

    public ArtistsRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private static final RowMapper<ArtistsSummary> ARTISTS_SUMMARY_ROW_MAPPER = new RowMapper<> () {
        @Override
        public ArtistsSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ArtistsSummary(
                rs.getString("artistId"),
                rs.getString("artistName"),
                rs.getString("artistNameSinhala"),
                rs.getInt("songCount")
            );
        }
    };

    private static final RowMapper<Artists> ARTISTS_ROW_MAPPER = new RowMapper<> () {
        @Override
        public Artists mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Artists(
                rs.getString("artistId"),
                rs.getString("artistName"),
                rs.getString("artistNameSinhala"),
                rs.getInt("songCount")
            );
        }
    };

    private static final RowMapper<ArtistsSongs> ARTISTS_SONGS_ROW_MAPPPER = new RowMapper<> () {
        @Override
        public ArtistsSongs mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ArtistsSongs(
                rs.getString("songId"),
                rs.getString("songName"),
                rs.getString("songNameSinhala")
            );
        }
    };

    public List<ArtistsSummary> findAll(){
        return jdbc.query(
            "SELECT a.ArtistID, a.ArtistName, a.ArtistNameSinhala, COUNT(s.SongID) as songCount FROM Artists a LEFT JOIN Songs s ON a.ArtistID = s.ArtistID GROUP BY a.ArtistID, a.ArtistName, a.ArtistNameSinhala ORDER BY a.ArtistID",
            ARTISTS_SUMMARY_ROW_MAPPER
        );
    }

    public Optional<Artists> findArtistById(String artistId){
        List<Artists> results = jdbc.query(
            "SELECT a.ArtistID, a.ArtistName, a.ArtistNameSinhala, COUNT(s.SongID) AS songCount " +
            "FROM Artists a " +
            "LEFT JOIN Songs s ON a.ArtistID = s.ArtistID " +
            "WHERE a.ArtistID = ? " +
            "GROUP BY a.ArtistID, a.ArtistName, a.ArtistNameSinhala",
            ARTISTS_ROW_MAPPER,
            artistId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public List<ArtistsSongs> findSongsByArtistId(String artistId){
        return jdbc.query(
            "SELECT SongID, SongName, SongNameSinhala " +
            "FROM Songs " +
            "WHERE ArtistID = ? " +
            "ORDER BY SongName",
            ARTISTS_SONGS_ROW_MAPPPER,
            artistId
        );
    }
    
    public boolean artistExists(String artistId) {
        String sql = "SELECT COUNT(*) FROM Artists WHERE ArtistID = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, artistId);
        return count != null && count > 0;
    }

    public Optional<ArtistsWithSongs> findArtistWithSongs(String artistId) {
        Optional<Artists> artistOpt = findArtistById(artistId);
        if (artistOpt.isEmpty()) {
            return Optional.empty();
        }
        
        List<ArtistsSongs> songs = findSongsByArtistId(artistId);
        
        Artists artist = artistOpt.get();
        return Optional.of(new ArtistsWithSongs(
            artist.artistName(),
            artist.artistNameSinhala(),
            artist.songCount(),
            songs
        ));
    }
}

