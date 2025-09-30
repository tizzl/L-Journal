package com.timo.Learning_Journal.controller;


import com.timo.Learning_Journal.entity.Entry;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.service.EntryService;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final SessionService sessionService;
    private final EntryService entryService;

    @GetMapping("/person/{id}")
    public String viewPerson(@PathVariable Long id, Model model) {

        Session session = sessionService.findById(id).orElse(null);
        if(session == null) return "redirect:/login";

        // Person aus der DB holen
        Person person = personService.findById(id).orElse(null);

        if (person == null) {
            return "redirect:/entries"; // fallback, falls Person nicht existiert
        }


        //Einträge des Nutzers
        List<Entry> entries = entryService.findByAuthor(person);
        model.addAttribute("person", person);
        model.addAttribute("entries", entries);

        return "person"; // person.html
    }
    @GetMapping("/person")
    public String viewOwnProfile(Model model, @CookieValue(value = "session-id") String cookieSessionID) {

        // Session der Person aus Session holen
        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
        if (session == null) return "redirect:/login";

        model.addAttribute("person", sessionService.findById(Long.parseLong(cookieSessionID))
                .orElseThrow().getPerson());

        //Einträge des Nutzers
        List<Entry> entries = entryService.findByAuthor(session.getPerson());
        model.addAttribute("person", session.getPerson());
        model.addAttribute("entries", entries);

        return "person"; // person.html
    }
}
