package com.vishalrashmika.sinso.api.Lyrics;

import org.springframework.stereotype.Service;

@Service
public class LyricsService {
    private final LyricsRepository repo;

    public LyricsService(LyricsRepository repo){
        this.repo = repo;
    }

    public Lyrics get(String lyricId){
        return repo.findByLyricId(lyricId);
    }
}
