package com.oognuyh.board.global;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController implements ErrorController {

    @GetMapping({"/", "/error"})
    public String home() {
        return "index.html";
    }
}