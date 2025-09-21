package com.vishalrashmika.sinso.api.Songs;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vishalrashmika.sinso.api.Config.IDPatterns;
import com.vishalrashmika.sinso.api.Errors.ErrorsSongs;
import com.vishalrashmika.sinso.api.Utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/songs")
@Tag(name = "Songs", description = "Song information endpoint")
public class SongsController {
    private final SongsService svc;

    public SongsController(SongsService svc){
        this.svc = svc;
    }

    @GetMapping({"", "/"})
    @Operation(
        summary = "Get all song info",
        description = "Get a summary of all songs' information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All Songs info returned successfully",
        content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SongsSummary.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsSongs.INTERNAL_SERVER_ERROR_ALL_SONGS)))
    })
    public ResponseEntity<?> allSongsInfo() {
        try {
            List<SongsSummary> songs = svc.list();
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorsSongs.getAllSongsErrorResponse(e));
        }
    }

    @GetMapping({"/{songId}", "/{songId}/"})
    @Operation(
        summary = "Get song information by song ID",
        description = "Retrieve the full information of a song from song ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Song found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Songs.class))),
        @ApiResponse(responseCode = "400", description = "Invalid song ID format",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsSongs.INVALID_SONG_ID))),
        @ApiResponse(responseCode = "404", description = "Song not found",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsSongs.SONG_NOT_FOUND))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsSongs.INTERNAL_SERVER_ERROR_SONGS)))
    })
    public ResponseEntity<?> getSongInfo(@PathVariable String songId){
        try {
            if (!Utils.isValidId(songId, IDPatterns.SONG_ID_PATTERN)){
                return ResponseEntity.badRequest().body(ErrorsSongs.invalidSongIdErrorResponse(songId));
            }

            Optional<Songs> song = svc.getSongInfo(songId);

            if(song.isPresent()){
                return ResponseEntity.ok(song.get());
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorsSongs.songNotFoundErrorResponse(songId));
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ErrorsSongs.invalidRequestErrorResponse(e, songId));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorsSongs.invalidServerErrorResponse(e, songId));
        }
    }
}