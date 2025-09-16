package com.vishalrashmika.sinso.api.Artists;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
}
