package com.vishalrashmika.sinso.api.Songs;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/songs")
public class SongsController {
    private final SongsService svc;

    public SongsController(SongsService svc){
        this.svc = svc;
    }

    @GetMapping
    public List<SongsSummary> all() {
        return svc.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Songs> one(@PathVariable Long id){
        Songs s = svc.get(id);
        return s == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(s);
    }
}