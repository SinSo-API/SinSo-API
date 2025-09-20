package com.vishalrashmika.sinso.api.Lyrics;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LyricsService {
    private final LyricsRepository repo;

    public LyricsService(LyricsRepository repo){
        this.repo = repo;
    }

    public Optional<Lyrics> getLyricInfo(String lyricId){
        return repo.findByLyricId(lyricId);
    }
}
