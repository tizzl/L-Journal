package com.timo.Learning_Journal.controller;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.service.EntryService;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private PersonService personService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EntryService entryService;

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

    @GetMapping("/person/{id}")
    public String viewPerson(@PathVariable Long id, Model model) {
        // Person aus der DB holen
        Person person = personService.findById(id).orElse(null);

        if (person == null) {
            return "redirect:/entries"; // fallback, falls Person nicht existiert
        }
        model.addAttribute("person", person);
        return "person"; // person.html muss existieren
    }
}