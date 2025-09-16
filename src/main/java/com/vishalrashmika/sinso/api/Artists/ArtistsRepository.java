package com.vishalrashmika.sinso.api.Artists;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public List<ArtistsSummary> findAll(){
        return jdbc.query(
            "SELECT a.ArtistID, a.ArtistName, a.ArtistNameSinhala, COUNT(s.SongID) as songCount FROM Artists a LEFT JOIN Songs s ON a.ArtistID = s.ArtistID GROUP BY a.ArtistID, a.ArtistName, a.ArtistNameSinhala ORDER BY a.ArtistID",
            ARTISTS_SUMMARY_ROW_MAPPER
        );
    }
}

