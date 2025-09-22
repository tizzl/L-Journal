package com.timo.Learning_Journal.controller;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private SessionService sessionService;

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