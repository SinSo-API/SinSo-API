package com.vishalrashmika.sinso.api.Search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vishalrashmika.sinso.api.Errors.ErrorsSearch;
import com.vishalrashmika.sinso.api.Songs.Songs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;

@RestController
@RequestMapping("/v1/search")
@CrossOrigin(origins = "*")
@Tag(name = "Search", description = "Search endpoint to search information")
public class SearchController {
    
    @Autowired
    private SearchService searchService;

    @Operation(
        summary = "Search songs, artists, and lyrics",
        description = """
            Comprehensive search across songs, artists, and lyrics with support for both English and Sinhala content.
            
            **Search Types:**
            - **Global Search**: Use `q` parameter to search across all fields (songs, artists, lyrics)
            - **Field-Specific Search**: Use individual parameters (`artist`, `title`, `lyrics`) for targeted searches
            - **Combined Search**: Mix multiple parameters for precise results
            """,
        tags = {"Search"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Search.class)
            )),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - At least one search parameter is required",
            content = @Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsSearch.INVALID_REQUEST))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                        value = ErrorsSearch.INTERNAL_SERVER_ERROR)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> searchSongs(
        @Parameter(
            name = "all",
            description = "Global search query. Search across all fields including song names, artist names, composers, lyricists, and lyrics content in both English and Sinhala.",
            schema = @Schema(type = "string")
        )
        @RequestParam(required = false) String all,
        
        @Parameter(
            name = "artist",
            description = "Search by artist name. Search both English and Sinhala artist names.",
            schema = @Schema(type = "string")
        )
        @RequestParam(required = false) String artist,
        
        @Parameter(
            name = "title",
            description = "Search by song title. Search both English and Sinhala song names.",
            schema = @Schema(type = "string")
        )
        @RequestParam(required = false) String title,
        
        @Parameter(
            name = "lyrics",
            description = "Search by lyrics content. Search both English and Sinhala lyrics.",
            schema = @Schema(type = "string")
        )
        @RequestParam(required = false) String lyrics,
        
        @Parameter(
            name = "page",
            description = "Page number for pagination (0-based). Determines which chunk of results to return.",
            schema = @Schema(type = "integer", minimum = "0", defaultValue = "0")
        )
        @RequestParam(defaultValue = "0") int page,
        
        @Parameter(
            name = "size",
            description = "Number of results per page. Controls how many songs are returned in each page.",
            schema = @Schema(type = "integer", minimum = "1", maximum = "100", defaultValue = "20")
        )
        @RequestParam(defaultValue = "20") int size) {
        
        try {
            if (isAllEmpty(all, artist, title, lyrics)) {
                return buildErrorResponse("At least one search parameter is required", HttpStatus.BAD_REQUEST);
            }
            
            List<Songs> songs = searchService.searchSongs(all, artist, title, lyrics);
            
            int start = Math.min(page * size, songs.size());
            int end = Math.min(start + size, songs.size());
            List<Songs> paginatedSongs = songs.subList(start, end);
            
            Map<String, Object> response = new HashMap<>();
            response.put("songs", paginatedSongs);
            response.put("total", songs.size());
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", (int) Math.ceil((double) songs.size() / size));
            response.put("hasNext", end < songs.size());
            response.put("hasPrevious", page > 0);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return buildErrorResponse("Search failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private boolean isAllEmpty(String... params) {
        for (String param : params) {
            if (param != null && !param.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        response.put("songs", Collections.emptyList());
        response.put("total", 0);
        return ResponseEntity.status(status).body(response);
    }
}