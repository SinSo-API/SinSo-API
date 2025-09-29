package com.vishalrashmika.sinso.api.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}