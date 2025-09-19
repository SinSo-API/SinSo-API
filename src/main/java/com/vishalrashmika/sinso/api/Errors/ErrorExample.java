package com.vishalrashmika.sinso.api.Errors;

public class ErrorExample {
    public static final String INTERNAL_SERVER_ERROR_ALL_ARTISTS = "{ \"status\": 500, \"code\": \"INTERNAL_SERVER_ERROR\", \"message\": \"An unexpected error occurred\", \"details\": \"Database connection failed\", \"path\": \"/v1/artists/\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String INVALID_ARTIST_ID = 
        "{ \"status\": 400, \"code\": \"INVALID_ARTIST_ID\", \"message\": \"Invalid artist ID format\", \"details\": \"Artist ID must follow the pattern ART-00001 (ART- followed by 5 digits)\", \"path\": \"/v1/artists/invalid-id\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String ARTIST_NOT_FOUND = 
        "{ \"status\": 404, \"code\": \"ARTIST_NOT_FOUND\", \"message\": \"Artist not found\", \"details\": \"Artist with ID ART-99999 does not exist\", \"path\": \"/v1/artists/ART-99999\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static final String INTERNAL_SERVER_ERROR_ARTISTS = 
        "{ \"status\": 500, \"code\": \"INTERNAL_SERVER_ERROR\", \"message\": \"An unexpected error occurred\", \"details\": \"Database connection failed\", \"path\": \"/v1/artists/ART-00001\", \"timestamp\": \"2025-09-19T10:30:00\" }";

    public static ErrorResponse getAllArtistsErrorResponse(Exception e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(e.getMessage())
                .path("/v1/artists/")
                .build();
        return errorResponse;
    }
}
