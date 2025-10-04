package com.vishalrashmika.sinso.api.Lyrics;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vishalrashmika.sinso.api.Config.IDPatterns;
import com.vishalrashmika.sinso.api.Errors.ErrorsLyrics;
import com.vishalrashmika.sinso.api.Utils.Utils;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/v1/lyrics")
@Tag(name = "Lyrics", description = "Lyrics information endpoint")
public class LyricsController {
    private final LyricsService svc;

    public LyricsController(LyricsService svc){
        this.svc = svc;
    }

    @GetMapping({"","/"})
    public ResponseEntity<?> voidURI(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorsLyrics.UriNotFoundErrorResponse());
    }

    @GetMapping("/{lyricId}")
    @Operation(
        summary = "Get lyrics by lyric ID",
        description = "Retrieve the lyrics of a song using from lyric ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lyric found and returned successfully",
                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Lyrics.class))),
        @ApiResponse(responseCode = "400", description = "Invalid lyric ID format",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsLyrics.INVALID_LYRIC_ID))),
        @ApiResponse(responseCode = "404", description = "Lyric not found",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsLyrics.LYRIC_NOT_FOUND))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsLyrics.INTERNAL_SERVER_ERROR_LYRICS)))
    })
    public ResponseEntity<?> getLyricInfo(@PathVariable String lyricId) {
        try {
            if (!Utils.isValidId(lyricId, IDPatterns.LYRIC_ID_PATTERN)){
                return ResponseEntity.badRequest().body(ErrorsLyrics.invalidLyricIdErrorResponse(lyricId));
            }

            Optional<Lyrics> lyrics = svc.getLyricInfo(lyricId);

            if(lyrics.isPresent()){
                return ResponseEntity.ok(lyrics.get());
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorsLyrics.lyricNotFoundErrorResponse(lyricId));
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ErrorsLyrics.invalidRequestErrorResponse(e, lyricId));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorsLyrics.invalidServerErrorResponse(e, lyricId));
        }
    }

    @Hidden
    @GetMapping("/{lyricId}/")
    public ResponseEntity<?> getLyricInfoSlash(@PathVariable String lyricId) {
        try {
            if (!Utils.isValidId(lyricId, IDPatterns.LYRIC_ID_PATTERN)){
                return ResponseEntity.badRequest().body(ErrorsLyrics.invalidLyricIdErrorResponse(lyricId));
            }

            Optional<Lyrics> lyrics = svc.getLyricInfo(lyricId);

            if(lyrics.isPresent()){
                return ResponseEntity.ok(lyrics.get());
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorsLyrics.lyricNotFoundErrorResponse(lyricId));
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ErrorsLyrics.invalidRequestErrorResponse(e, lyricId));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorsLyrics.invalidServerErrorResponse(e, lyricId));
        }
    }
}
