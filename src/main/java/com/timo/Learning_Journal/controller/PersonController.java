package com.timo.Learning_Journal.controller;


import com.timo.Learning_Journal.entity.*;
import com.timo.Learning_Journal.service.CourseService;
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

import java.util.*;

@Controller
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final SessionService sessionService;
    private final EntryService entryService;
    private final CourseService courseService;

    @GetMapping("/person/{id}")
    public String viewPerson(@PathVariable Long id,
                             Model model,
                             @CookieValue(value = "session-id", required = false) String cookieSessionID) {

        // Session aus Cookie holen (korrekt)
        if (cookieSessionID == null || cookieSessionID.isBlank()) {
            return "redirect:/login";
        }

        Optional<Session> sessionOpt;
        try {
            sessionOpt = sessionService.findById(Long.parseLong(cookieSessionID));
        } catch (NumberFormatException e) {
            return "redirect:/login";
        }
        if (sessionOpt.isEmpty()) return "redirect:/login";

        if (sessionOpt == null) {
            return "redirect:/entries"; // fallback, falls Person nicht existiert
        }
        Session session = sessionOpt.get();
        Person loggedInPerson = session.getPerson();
        Person person = personService.findById(id).orElse(null);
        if (person == null) {
            return "redirect:/entries";
        }


        //Einträge und Kurse  des Nutzers
        List<Entry> entries = entryService.findByAuthor(person);

        Collection<Course> courses = (person.getRole() == Role.TEACHER)
                ? courseService.findByTeacher(person)
                : person.getCourses();
        model.addAttribute("person", person);
        model.addAttribute("loggedInPerson", loggedInPerson);
        model.addAttribute("entries", entries);
        model.addAttribute("courses", courses);

        return "person"; // person.html
    }

    @GetMapping("/person")
    public String viewOwnProfile(Model model,
                                 @CookieValue(value = "session-id") String cookieSessionID) {

        // Session der Person aus Session holen
        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
        if (session == null) return "redirect:/login";

        Person loggedInPerson = session.getPerson();
        Person person = loggedInPerson;

        //Einträge des Nutzers
        List<Entry> entries = entryService.findByAuthor(person);
        //Kurse laden
        Collection<Course> courses = new ArrayList<>();
        if (person.getRole() == Role.STUDENT) {
            courses = person.getCourses();
        } else if (person.getRole() == Role.TEACHER) {
            courses = person.getCourseOwner();
        }
        model.addAttribute("person", person);
        model.addAttribute("loggedInPerson", loggedInPerson);
        model.addAttribute("entries", entries);
        model.addAttribute("courses", courses);

        return "person"; // person.html
    }
}
