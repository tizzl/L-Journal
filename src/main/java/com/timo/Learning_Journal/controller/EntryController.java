package com.timo.Learning_Journal.controller;

import com.timo.Learning_Journal.entity.Entry;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.service.EntryService;
import com.timo.Learning_Journal.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;
    private final SessionService sessionService;

    @GetMapping("/entry/{id}")
    public String viewEntry(Model model, @PathVariable Long id) {
        Entry entry = entryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found " + id));
        model.addAttribute("entry", entry);
        return "view-entry";
    }

    @PostMapping("/new-entry")
    public String newEntry(@RequestParam(name = "title") String formtitle,
                           @RequestParam(name = "body") String formbody, HttpServletRequest request) {

        String cookieSessionId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("session-id".equals(cookie.getName())) {
                    cookieSessionId = cookie.getValue();
                }
            }
        }
        if (cookieSessionId == null) {
            return "redirect:/";
        }
        Optional<Session> sessionOpt = sessionService.findById(Long.parseLong(cookieSessionId));
        if (sessionOpt.isEmpty()) {
            return "redirect:/login";
        }
        Session dbSession = sessionOpt.get();
        Person person = dbSession.getPerson(); //Autor wird angelegt!

        //Entry anlegen
        Entry entry = new Entry();
        entry.setTitle(formtitle);
        entry.setBody(formbody);
        entry.setAuthor(person);
        entry.setDate(LocalDate.now());
        Entry savedEntry = entryService.save(entry);
        return "redirect:/entry/" + savedEntry.getId();
    }

    @GetMapping("/entries")
    public String viewEntries(Model model, @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {

        if ("0".equals(cookieSessionID)) {
            //Kein cookie, keine Party, ergo -->/login
            return "redirect:/login";
        }
        return sessionService.findById(Long.parseLong(cookieSessionID)).map(session -> {
            List<Entry> entries = entryService.findAll();
            model.addAttribute("person", session.getPerson());
            model.addAttribute("entries", entries);
            model.addAttribute("activeParagraph", "entries");
            return "entrylist";
        }).orElse("redirect:/login");

    }
}