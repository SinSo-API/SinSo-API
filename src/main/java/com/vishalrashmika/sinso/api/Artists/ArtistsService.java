package com.vishalrashmika.sinso.api.Artists;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistsService {
    private final ArtistsRepository repo;

    public ArtistsService(ArtistsRepository repo){
        this.repo = repo;
    }

    public List<ArtistsSummary> list(){
        return repo.findAll();
    }

    public Optional<ArtistsWithSongs> getArtistWithSongs(String artistId) {
        return repo.findArtistWithSongs(artistId);
    }
    
    public Optional<Artists> get(String artistId) {
        return repo.findArtistById(artistId);
    }
}
