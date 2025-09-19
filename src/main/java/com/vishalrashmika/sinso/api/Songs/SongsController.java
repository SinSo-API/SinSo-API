package com.vishalrashmika.sinso.api.Songs;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/songs")
@Tag(name = "Songs")
public class SongsController {
    private final SongsService svc;

    public SongsController(SongsService svc){
        this.svc = svc;
    }

    @GetMapping({"", "/"})
    public List<SongsSummary> all() {
        return svc.list();
    }

    @GetMapping({"/{songId}", "/{songId}/"})
    public ResponseEntity<Songs> one(@PathVariable String songId){
        Songs s = svc.get(songId);
        return s == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(s);
    }
}