package com.vishalrashmika.sinso.api.Artists;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public record ArtistsWithSongs(
    String artistName,
    String artistNameSinhala,
    Integer songCount,
    List<ArtistsSongs> songs
) {}