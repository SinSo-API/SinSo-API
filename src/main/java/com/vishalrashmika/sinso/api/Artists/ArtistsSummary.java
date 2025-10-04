package com.vishalrashmika.sinso.api.Artists;

public record ArtistsSummary(
    String artistId,
    String artistName,
    String artistNameSinhala,
    Integer songCount
) {}
