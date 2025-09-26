package com.timo.Learning_Journal.controller;


import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final SessionService sessionService;

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
    @GetMapping("/person")
    public String viewOwnProfile(Model model, @CookieValue(value = "session-id") String sessionId) {
        model.addAttribute("person", sessionService.findById(Long.parseLong(sessionId))
                .orElseThrow().getPerson());
        return "person"; // person.html
    }
}
