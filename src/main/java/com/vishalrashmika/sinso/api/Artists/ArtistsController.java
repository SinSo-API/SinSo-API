package com.vishalrashmika.sinso.api.Artists;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.vishalrashmika.sinso.api.Errors.ErrorResponse;
import com.vishalrashmika.sinso.api.Utils.Utils;
import com.vishalrashmika.sinso.api.Errors.ErrorExample;

@RestController
@RequestMapping("/v1/artists")
@Tag(name = "Artists")
public class ArtistsController {
    private final ArtistsService svc;

    public ArtistsController(ArtistsService svc){
        this.svc = svc;
    }

    @GetMapping({"", "/"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All artists info returned successfully",
        content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ArtistsSummary.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                     examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                         value = ErrorExample.INTERNAL_SERVER_ERROR_ALL_ARTISTS)))
    })
    public ResponseEntity<?> allArtistsInfo() {
        try {
            List<ArtistsSummary> artists = svc.list();
            return ResponseEntity.ok(artists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorExample.getAllArtistsErrorResponse(e));
        }
    }

    @GetMapping({"/{artistId}", "/{artistId}/"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ArtistsWithSongs.class))),
        @ApiResponse(responseCode = "400", description = "Invalid artist ID format",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorExample.INVALID_ARTIST_ID))),
        @ApiResponse(responseCode = "404", description = "Artist not found",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorExample.ARTIST_NOT_FOUND))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorExample.INTERNAL_SERVER_ERROR_ARTISTS)))
    })
    public ResponseEntity<?> one(@PathVariable String artistId) {
        String requestPath = "/v1/artists/" + artistId;
        
        try {
            // Validate artist ID format
            if (!Utils.isValidArtistId(artistId)) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(400)
                    .code("INVALID_ARTIST_ID")
                    .message("Invalid artist ID format")
                    .details("Artist ID must follow the pattern ART-00001 (ART- followed by 5 digits)")
                    .path(requestPath)
                    .build();
                
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Optional<ArtistsWithSongs> artist = svc.getArtistWithSongs(artistId);
            
            if (artist.isPresent()) {
                return ResponseEntity.ok(artist.get());
            } else {
                ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(404)
                    .code("ARTIST_NOT_FOUND")
                    .message("Artist not found")
                    .details("Artist with ID " + artistId + " does not exist")
                    .path(requestPath)
                    .build();
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .code("INVALID_REQUEST")
                .message("Invalid request parameters")
                .details(e.getMessage())
                .path(requestPath)
                .build();
            
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .path(requestPath)
                .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    

}
