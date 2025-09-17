package com.timo.Learning_Journal;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {


    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/")
    public String index(Model model, @CookieValue(value = "session-id", defaultValue = "0")String cookieSessionID) {
        if ("0".equals(cookieSessionID)) {
            //Kein cookie, keine Party, ergo -->/login
            return "redirect:/login";
        }
        return sessionRepository.findById(Long.parseLong(cookieSessionID)).map(session -> {
            model.addAttribute("person", session.getPerson());
            return "home";
        }).orElse("redirect:/login");
    }

    @GetMapping("/entry/{id}")

    public String viewEntry(Model model, @PathVariable Long id) {
        Entry entry = entryRepository.findById(id).get();
        model.addAttribute("title", entry.getTitle());
        model.addAttribute("body", entry.getBody());
        return "view-entry";
    }


    @PostMapping("/new-entry")
    public String newEntry(Model model, @RequestParam(name = "title", required = true) String formtitle,
                           @RequestParam(name = "body", required = true) String formbody, HttpServletRequest request) {

        String cookieSessionId = null;
        if (request.getCookies() != null){
            for (Cookie cookie : request.getCookies()) {
                if("session-id".equals(cookie.getName())) {
                    cookieSessionId = cookie.getValue();
                }
            }
        }

        if (cookieSessionId == null) {
            return "redirect:/";
        }
        Optional<Session> sessionOpt = sessionRepository .findById(Long.parseLong(cookieSessionId));
        if (sessionOpt.isEmpty()) {
            return "redirect:/login";
        }

        Session dbSession = sessionOpt.get();
        Person person = dbSession.getPerson(); //Autor wird angelegt, hoffentlich!


        //Entry anlegen
        Entry entry = new Entry();
        entry.setTitle(formtitle);
        entry.setBody(formbody);
        entry.setAuthor(person);
        entry.setDate(LocalDate.now());
        entryRepository.save(entry);
        return "redirect:/";

    }

    @GetMapping("/entries")
    public String viewEntries(Model model) {
        List<Entry> entries = entryRepository.findAll();
        model.addAttribute("entries", entries);
        return "entrylist";
    }

    @GetMapping("/register")
    public String showRegistration(Model model) {
        return "register";
    }

    @PostMapping("/new-person")
    public String newPerson(Model model, @RequestParam(name = "name", required = true) String formname,
                            @RequestParam(name = "email", required = true) String formemail,
                            @RequestParam(name = "password", required = true) String formpassword, @RequestParam(required = false) String adminCode, HttpServletResponse response) {


        Person person = new Person();
        person.setName(formname);
        person.setEmail(formemail);
        person.setPassword(formpassword);

        //Enum zuweisen
        if(adminCode != null &&"JimEuAs060924".equals(adminCode)) {
            person.setRole(Role.ADMIN);
        }else  {
            person.setRole(Role.USER);
        }
        personRepository.save(person);

        //Session für Newbies anlegen

        Session session = new Session();
        session.setPerson(person);
        sessionRepository.save(session);

        //Cookies verteilen

        Cookie cookie = new Cookie("session-id", Long.toString(session.getId()));
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/";
    }
    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    @PostMapping("/logInPerson")
    public String login(Model model, @RequestParam(name = "email", required = true) String formemail,
                        @RequestParam(name = "password", required = true) String formpassword, HttpServletResponse response) {


        Person person = personRepository.findByEmail(formemail);
        if (person != null && person.getPassword().equals(formpassword)) {


            Session session = new Session();
            session.setPerson(person);
            sessionRepository.save(session);

            //Cookie setzen
            Cookie cookie = new Cookie("session-id", session.getId().toString());
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/";

        } else {
            return "redirect:/login?error";
        }
    }
    @GetMapping("/person/{id}")
    public String viewPerson(@PathVariable Long id, Model model) {
        // Person aus der DB holen
        Person person = personRepository.findById(id).orElse(null);

        if (person == null) {
            return "redirect:/entries"; // fallback, falls Person nicht existiert
        }

        model.addAttribute("person", person);
        return "person"; // person.html muss existieren
    }

}