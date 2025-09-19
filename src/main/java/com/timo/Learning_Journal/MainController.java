package com.timo.Learning_Journal;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Value("${adminCode}")
    private String adminCode;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @GetMapping("/entry/{id}")

    public String viewEntry(Model model, @PathVariable Long id) {
        Entry entry = entryService.findById(id).get();
        model.addAttribute("title", entry.getTitle());
        model.addAttribute("body", entry.getBody());
        return "view-entry";
    }


    @PostMapping("/new-entry")
    public String newEntry(Model model, @RequestParam(name = "title", required = true) String formtitle,
                           @RequestParam(name = "body", required = true) String formbody, HttpServletRequest request) {

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
        Person person = dbSession.getPerson(); //Autor wird angelegt, hoffentlich!


        //Entry anlegen
        Entry entry = new Entry();
        entry.setTitle(formtitle);
        entry.setBody(formbody);
        entry.setAuthor(person);
        entry.setDate(LocalDate.now());
        entryService.save(entry);
        return "redirect:/";

    }

    @GetMapping("/entries")
    public String viewEntries(Model model) {
        List<Entry> entries = entryService.findAll();
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

        String hashedPassword = passwordEncoder.encode(formpassword);

        Person person = new Person();
        person.setName(formname);
        person.setEmail(formemail);
        person.setPassword(hashedPassword);

        //Enum zuweisen
        if (this.adminCode.equals(adminCode)) {

            person.setRole(Role.ADMIN);

        } else {
            person.setRole(Role.USER);
        }
        personService.save(person);

        //Session für Newbies anlegen

        Session session = new Session();
        session.setPerson(person);
        sessionService.save(session);

        //Cookies verteilen

        Cookie cookie = sessionService.createCookieSession(session);
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

        Person person = personService.findByEmail(formemail);

        if (person != null && passwordEncoder.matches(formpassword, person.getPassword())) {


            Session session = sessionService.createSession(person);

            //Cookie setzen
            Cookie cookie = sessionService.createCookieSession(session);
            response.addCookie(cookie);

            return "redirect:/";

        } else {
            return "redirect:/login?error";
        }
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

    @GetMapping("/logout")

    public String logout(Model model, @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID, HttpServletResponse response) {

        if ("0".equals(cookieSessionID)) {
            //Kein cookie, keine Party, ergo -->/login
        } else {
            sessionService.findById(Long.parseLong(cookieSessionID)).ifPresent(session -> {
                Cookie cookie = sessionService.endCookieSession(session);
                response.addCookie(cookie);
            });
        }
        return "redirect:/login";
    }
}