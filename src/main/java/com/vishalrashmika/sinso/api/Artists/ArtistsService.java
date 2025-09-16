package com.vishalrashmika.sinso.api.Artists;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArtistsService {
    private final ArtistsRepository repo;

    public ArtistsService(ArtistsRepository repo){
        this.repo = repo;
    }

    public List<ArtistsSummary> list(){
        return repo.findAll();
    }
}
