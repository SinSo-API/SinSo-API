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
    
    @Operation(
        summary = "Enhanced search songs with advanced filters",
        description = """
            **Enhanced Search API with Advanced Filtering Capabilities**

            This endpoint provides comprehensive search functionality with advanced filtering options:

            **Basic Search Parameters (same as original):**
            - `q` or `query`: Global search across all fields
            - `artist`: Search by artist name
            - `title`: Search by song title
            - `lyrics`: Search by lyrics content

            **Advanced Filter Parameters:**
            - `artistId`: Filter by specific artist ID
            - `composer`: Filter by composer name
            - `lyricist`: Filter by lyricist name
            - `hasLyrics`: Boolean - songs with/without lyrics (true/false)
            - `hasComposer`: Boolean - songs with/without composer info (true/false)
            - `hasLyricist`: Boolean - songs with/without lyricist info (true/false)
            - `language`: Language preference (english/sinhala/both)
            - `matchType`: Search match type (contains/exact/starts_with)
            - `sortBy`: Sort results by field (songName/artistName/composer/etc.)
            - `sortOrder`: Sort order (asc/desc)
            - `page`: Page number (0-based)
            - `size`: Results per page (1-100)

            **Example Usage:**
            - `/v1/search/enhanced?q=love&language=english&sortBy=songName&sortOrder=asc`
            - `/v1/search/enhanced?composer=Premadasa&hasLyrics=true&language=sinhala`
            - `/v1/search/enhanced?artist=Amith&matchType=exact&sortBy=artistName`
            """,
        tags = {"Search"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Enhanced search completed successfully",
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
    @GetMapping("/enhanced")
    public ResponseEntity<Map<String, Object>> searchSongsEnhanced(
        @Parameter(name = "q", description = "Global search query across all fields")
        @RequestParam(required = false) String q,

        @Parameter(name = "query", description = "Alternative global search query parameter")
        @RequestParam(required = false) String query,

        @Parameter(name = "artist", description = "Search by artist name")
        @RequestParam(required = false) String artist,

        @Parameter(name = "title", description = "Search by song title")
        @RequestParam(required = false) String title,

        @Parameter(name = "lyrics", description = "Search by lyrics content")
        @RequestParam(required = false) String lyrics,

        @Parameter(name = "artistId", description = "Filter by specific artist ID")
        @RequestParam(required = false) String artistId,

        @Parameter(name = "composer", description = "Filter by composer name")
        @RequestParam(required = false) String composer,

        @Parameter(name = "lyricist", description = "Filter by lyricist name")
        @RequestParam(required = false) String lyricist,

        @Parameter(name = "hasLyrics", description = "Filter songs with/without lyrics (true/false)")
        @RequestParam(required = false) Boolean hasLyrics,

        @Parameter(name = "hasComposer", description = "Filter songs with/without composer info (true/false)")
        @RequestParam(required = false) Boolean hasComposer,

        @Parameter(name = "hasLyricist", description = "Filter songs with/without lyricist info (true/false)")
        @RequestParam(required = false) Boolean hasLyricist,

        @Parameter(name = "language", description = "Language preference (english/sinhala/both)")
        @RequestParam(required = false) String language,

        @Parameter(name = "matchType", description = "Search match type (contains/exact/starts_with)")
        @RequestParam(required = false) String matchType,

        @Parameter(name = "sortBy", description = "Field to sort results by (songName/artistName/composer/lyricist)")
        @RequestParam(required = false) String sortBy,

        @Parameter(name = "sortOrder", description = "Sort order (asc/desc)")
        @RequestParam(required = false) String sortOrder,

        @Parameter(name = "page", description = "Page number for pagination (0-based)")
        @RequestParam(defaultValue = "0") int page,

        @Parameter(name = "size", description = "Number of results per page (1-100)")
        @RequestParam(defaultValue = "20") int size) {

        try {
            // Create filter criteria object
            SearchFilterCriteria criteria = new SearchFilterCriteria();

            // Set search parameters (use 'q' or 'query', whichever is provided)
            String searchQuery = q != null ? q : query;
            criteria.setQuery(searchQuery);
            criteria.setArtist(artist);
            criteria.setTitle(title);
            criteria.setLyrics(lyrics);

            // Set advanced filter parameters
            criteria.setArtistId(artistId);
            criteria.setComposer(composer);
            criteria.setLyricist(lyricist);
            criteria.setHasLyrics(hasLyrics);
            criteria.setHasComposer(hasComposer);
            criteria.setHasLyricist(hasLyricist);

            // Set enum values with fallbacks
            if (language != null) {
                criteria.setLanguage(SearchLanguage.fromString(language));
            }
            if (matchType != null) {
                criteria.setMatchType(SearchMatchType.fromString(matchType));
            }
            if (sortBy != null) {
                criteria.setSortBy(SearchSortBy.fromString(sortBy));
            }
            if (sortOrder != null) {
                criteria.setSortOrder(SearchSortOrder.fromString(sortOrder));
            }

            // Set pagination
            criteria.setPage(page);
            criteria.setSize(size);

            // Perform enhanced search
            List<Songs> songs = searchService.searchSongsWithFilters(criteria);

            // Apply pagination to results
            int start = Math.min(page * size, songs.size());
            int end = Math.min(start + size, songs.size());
            List<Songs> paginatedSongs = songs.subList(start, end);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("songs", paginatedSongs);
            response.put("total", songs.size());
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", (int) Math.ceil((double) songs.size() / size));
            response.put("hasNext", end < songs.size());
            response.put("hasPrevious", page > 0);

            // Add applied filters info for debugging/transparency
            Map<String, Object> appliedFilters = new HashMap<>();
            if (searchQuery != null) appliedFilters.put("query", searchQuery);
            if (artist != null) appliedFilters.put("artist", artist);
            if (title != null) appliedFilters.put("title", title);
            if (lyrics != null) appliedFilters.put("lyrics", lyrics);
            if (artistId != null) appliedFilters.put("artistId", artistId);
            if (composer != null) appliedFilters.put("composer", composer);
            if (lyricist != null) appliedFilters.put("lyricist", lyricist);
            if (hasLyrics != null) appliedFilters.put("hasLyrics", hasLyrics);
            if (hasComposer != null) appliedFilters.put("hasComposer", hasComposer);
            if (hasLyricist != null) appliedFilters.put("hasLyricist", hasLyricist);
            if (language != null) appliedFilters.put("language", language);
            if (matchType != null) appliedFilters.put("matchType", matchType);
            if (sortBy != null) appliedFilters.put("sortBy", sortBy);
            if (sortOrder != null) appliedFilters.put("sortOrder", sortOrder);

            response.put("appliedFilters", appliedFilters);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return buildErrorResponse("Enhanced search failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        response.put("songs", Collections.emptyList());
        response.put("total", 0);
        return ResponseEntity.status(status).body(response);
    }
}