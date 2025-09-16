package com.vishalrashmika.sinso.api.Artists;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/artists")
public class ArtistsController {
    private final ArtistsService svc;

    public ArtistsController(ArtistsService svc){
        this.svc = svc;
    }

    @GetMapping({"", "/"})
    public List<ArtistsSummary> all() {
        return svc.list();
    }

    @GetMapping({"/{artistId}", "/{artistId}/"})
    public ResponseEntity<ArtistsWithSongs> one(@PathVariable String artistId) {
        Optional<ArtistsWithSongs> artist = svc.getArtistWithSongs(artistId);
        return artist.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

}
