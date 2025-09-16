package com.vishalrashmika.sinso.api.Lyrics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/lyrics")
public class LyricsController {
    private final LyricsService svc;

    public LyricsController(LyricsService svc){
        this.svc = svc;
    }

    @GetMapping({"/{lyricId}", "/{lyricId}/"})
    public ResponseEntity<Lyrics> one(@PathVariable String lyricId) {
        Lyrics l = svc.get(lyricId);
        return l == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(l);
    }
    
}
