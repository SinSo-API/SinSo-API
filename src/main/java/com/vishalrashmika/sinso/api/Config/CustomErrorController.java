package com.vishalrashmika.sinso.api.Config;

import com.vishalrashmika.sinso.api.Errors.ErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                String requestPath = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
                
                if (requestPath == null) {
                    requestPath = request.getRequestURI();
                }
                
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .status(404)
                        .code(requestPath + " not found")
                        .message("URI is not defined")
                        .details(requestPath + " URI does not exist")
                        .path(requestPath)
                        .build();
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
        
        String requestPath = request.getRequestURI();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .code("Internal Server Error")
                .message("An unexpected error occurred")
                .details("Please contact support")
                .path(requestPath)
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}