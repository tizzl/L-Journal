package com.timo.Learning_Journal.controller;

import com.timo.Learning_Journal.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SessionService sessionService;

    @GetMapping("/")
    public String index(Model model, @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {
        if ("0".equals(cookieSessionID)) {
            //Kein cookie, keine Party, ergo -->/login
            return "redirect:/login";
        }
        return sessionService.findById(Long.parseLong(cookieSessionID)).map(session -> {
            model.addAttribute("person", session.getPerson());
            return "home";
        }).orElse("redirect:/login");
    }
}