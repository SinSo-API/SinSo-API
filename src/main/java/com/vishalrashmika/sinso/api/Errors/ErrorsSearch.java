package com.vishalrashmika.sinso.api.Errors;

public class ErrorsSearch {
    public static final String INVALID_REQUEST = "{\"error\":\"At least one search parameter is required\",\"songs\":[],\"total\":0}";
    public static final String INTERNAL_SERVER_ERROR = "{\"error\":\"Search failed: Internal Server Error\",\"songs\":[],\"total\":0}";
}
