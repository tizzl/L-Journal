package com.timo.Learning_Journal.controller;

import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Role;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Value("${adminCode}")
    private String adminCode;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonService personService;

    @Autowired
    private SessionService sessionService;

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
            person.setRole(Role.TEACHER);
        } else {
            person.setRole(Role.STUDENT);
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
