package com.vishalrashmika.sinso.api.Errors;

public class ErrorsSongs {
    public static final String INTERNAL_SERVER_ERROR_ALL_SONGS = "{ \"status\": 500, \"code\": \"INTERNAL_SERVER_ERROR\", \"message\": \"An unexpected error occurred\", \"details\": \"Database connection failed\", \"path\": \"/v1/songs/\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String INVALID_SONG_ID = 
        "{ \"status\": 400, \"code\": \"INVALID_SONG_ID\", \"message\": \"Invalid song ID format\", \"details\": \"Song ID must follow the pattern SNG-0000001 (SNG- followed by 7 digits)\", \"path\": \"/v1/song/invalid-id\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String SONG_NOT_FOUND = 
        "{ \"status\": 404, \"code\": \"SONG_NOT_FOUND\", \"message\": \"Song not found\", \"details\": \"Song with ID SNG-9999999 does not exist\", \"path\": \"/v1/songs/SNG-9999999\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String INTERNAL_SERVER_ERROR_SONGS = 
        "{ \"status\": 500, \"code\": \"INTERNAL_SERVER_ERROR\", \"message\": \"An unexpected error occurred\", \"details\": \"Database connection failed\", \"path\": \"/v1/songs/SNG-0000001\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static ErrorResponse getAllSongsErrorResponse(Exception e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .path("/v1/songs/")
                .build();
        return errorResponse;
    }

    public static ErrorResponse invalidSongIdErrorResponse(String songId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .code("INVALID_SONG_ID")
                .message("Invalid song ID format")
                .details("Song ID must follow the pattern SNG-0000001 (SNG- followed by 7 digits)")
                .path("/v1/songs/" + songId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse songNotFoundErrorResponse(String songId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .code("SONG_NOT_FOUND")
                .message("Song not found")
                .details("Song with ID " + songId + " does not exist")
                .path("/v1/songs/" + songId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse invalidRequestErrorResponse(IllegalArgumentException e, String songId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .code("INVALID_REQUEST")
                .message("Invalid request parameters")
                .details(e.getMessage())
                .path("/v1/songs/" + songId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse invalidServerErrorResponse(Exception e, String songId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .path("/v1/songs/" + songId)
                .build();
        return errorResponse;
    }
}