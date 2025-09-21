package com.vishalrashmika.sinso.api.Config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import java.io.IOException;

@Controller
public class SwaggerUIController {

    @GetMapping("/v1/docs")
    public void swaggerUiDocs(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/swagger-ui/index.html");
        dispatcher.forward(request, response);
    }

    @GetMapping("/v1/docs/")
    public void swaggerUiDocsWithSlash(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/swagger-ui/index.html");
        dispatcher.forward(request, response);
    }

    @GetMapping("/v1/docs/**")
    public void swaggerUiResources(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getRequestURI().substring("/v1/docs".length());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/swagger-ui" + path);
        dispatcher.forward(request, response);
    }
}