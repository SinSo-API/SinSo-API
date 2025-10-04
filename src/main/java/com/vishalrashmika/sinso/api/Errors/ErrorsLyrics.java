package com.vishalrashmika.sinso.api.Errors;

public class ErrorsLyrics {

    public static final String INVALID_LYRIC_ID = 
        "{ \"status\": 400, \"code\": \"INVALID_LYRIC_ID\", \"message\": \"Invalid lyric ID format\", \"details\": \"Lyric ID must follow the pattern LYR-0000001 (LYR- followed by 7 digits)\", \"path\": \"/v1/lyrics/invalid-id\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String LYRIC_NOT_FOUND = 
        "{ \"status\": 404, \"code\": \"LYRIC_NOT_FOUND\", \"message\": \"Lyric not found\", \"details\": \"Lyric with ID LYR-9999999 does not exist\", \"path\": \"/v1/lyrics/LYR-9999999\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String INTERNAL_SERVER_ERROR_LYRICS = 
        "{ \"status\": 500, \"code\": \"INTERNAL_SERVER_ERROR\", \"message\": \"An unexpected error occurred\", \"details\": \"Database connection failed\", \"path\": \"/v1/lyrics/LYR-0000001\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static ErrorResponse invalidLyricIdErrorResponse(String lyricId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .code("INVALID_LYRIC_ID")
                .message("Invalid lyric ID format")
                .details("Lyric ID must follow the pattern LYR-0000001 (LYR- followed by 7 digits)")
                .path("/v1/lyrics/" + lyricId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse lyricNotFoundErrorResponse(String lyricId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .code("LYRIC_NOT_FOUND")
                .message("lyric not found")
                .details("Lyric with ID " + lyricId + " does not exist")
                .path("/v1/lyrics/" + lyricId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse invalidRequestErrorResponse(IllegalArgumentException e, String lyricId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .code("INVALID_REQUEST")
                .message("Invalid request parameters")
                .details(e.getMessage())
                .path("/v1/lyrics/" + lyricId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse invalidServerErrorResponse(Exception e, String lyricId){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .path("/v1/lyrics/" + lyricId)
                .build();
        return errorResponse;
    }

    public static ErrorResponse UriNotFoundErrorResponse(){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .code("/v1/lyrics not found")
                .message("URI is not defined")
                .details("/v1/lyrics URI does not exist")
                .path("/v1/lyrics")
                .build();
        return errorResponse;
    }
}
