package com.vishalrashmika.sinso.api.Artists;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.vishalrashmika.sinso.api.Utils.Utils;
import com.vishalrashmika.sinso.api.Config.IDPatterns;
import com.vishalrashmika.sinso.api.Errors.ErrorsArtists;

@RestController
@RequestMapping("/v1/artists")
@Tag(name = "Artists", description = "Artists information endpoint")
public class ArtistsController {
    private final ArtistsService svc;

    public ArtistsController(ArtistsService svc){
        this.svc = svc;
    }

    @GetMapping()
    @Operation(
        summary = "Get all artists info",
        description = "Get a summary of all artists' information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All artists info returned successfully",
        content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ArtistsSummary.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                     examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                         value = ErrorsArtists.INTERNAL_SERVER_ERROR_ALL_ARTISTS)))
    })
    public ResponseEntity<?> allArtistsInfo() {
        try {
            List<ArtistsSummary> artists = svc.list();
            return ResponseEntity.ok(artists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorsArtists.getAllArtistsErrorResponse(e));
        }
    }

    @Hidden
    @GetMapping("/")
    public ResponseEntity<?> allArtistsInfoSlash() {
        try {
            List<ArtistsSummary> artists = svc.list();
            return ResponseEntity.ok(artists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorsArtists.getAllArtistsErrorResponse(e));
        }
    }

    @GetMapping("/{artistId}")
    @Operation(
        summary = "Get artist information by artist ID",
        description = "Retrieve the full information of an artist from artist ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ArtistsWithSongs.class))),
        @ApiResponse(responseCode = "400", description = "Invalid artist ID format",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsArtists.INVALID_ARTIST_ID))),
        @ApiResponse(responseCode = "404", description = "Artist not found",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsArtists.ARTIST_NOT_FOUND))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsArtists.INTERNAL_SERVER_ERROR_ARTISTS)))
    })
    public ResponseEntity<?> getArtistInfo(@PathVariable String artistId) {        
        try{
            if (!Utils.isValidId(artistId, IDPatterns.ARTIST_ID_PATTERN)) {
                return ResponseEntity.badRequest().body(ErrorsArtists.invalidArtIdErrorResponse(artistId));
            }
            
            Optional<ArtistsWithSongs> artist = svc.getArtistWithSongs(artistId);
            
            if (artist.isPresent()){
                return ResponseEntity.ok(artist.get());
            }
            else{                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorsArtists.artistNotFoundErrorResponse(artistId));
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ErrorsArtists.invalidRequestErrorResponse(e, artistId));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorsArtists.invalidServerErrorResponse(e, artistId));
        }
    }

    @Hidden
    @GetMapping("/{artistId}/")
    public ResponseEntity<?> getArtistInfoSlash(@PathVariable String artistId) {
        try{
            if (!Utils.isValidId(artistId, IDPatterns.ARTIST_ID_PATTERN)) {
                return ResponseEntity.badRequest().body(ErrorsArtists.invalidArtIdErrorResponse(artistId));
            }
            
            Optional<ArtistsWithSongs> artist = svc.getArtistWithSongs(artistId);
            
            if (artist.isPresent()){
                return ResponseEntity.ok(artist.get());
            }
            else{                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorsArtists.artistNotFoundErrorResponse(artistId));
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ErrorsArtists.invalidRequestErrorResponse(e, artistId));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorsArtists.invalidServerErrorResponse(e, artistId));
        }
    }
}
