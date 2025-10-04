package com.vishalrashmika.sinso.api.Artists;

import java.util.List;

public record ArtistsWithSongs(
    String artistName,
    String artistNameSinhala,
    Integer songCount,
    List<ArtistsSongs> songs
) {}