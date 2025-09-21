package com.vishalrashmika.sinso.api.Songs;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SongsService {
    private final SongsRepository repo;

    public SongsService(SongsRepository repo){
        this.repo = repo;
    }

    public List<SongsSummary> list(){
        return repo.findAll();
    }

    public Optional<Songs> getSongInfo(String songId){
        return repo.findSongById(songId);
    }
}