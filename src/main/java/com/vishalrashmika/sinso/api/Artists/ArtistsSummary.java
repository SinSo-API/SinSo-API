package com.vishalrashmika.sinso.api.Artists;

import io.swagger.v3.oas.annotations.media.Schema;

public record ArtistsSummary(
    String artistId,
    String artistName,
    String artistNameSinhala,
    Integer songCount
) {}
