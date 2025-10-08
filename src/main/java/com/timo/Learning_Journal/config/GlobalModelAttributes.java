package com.timo.Learning_Journal.config;

import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;



@ControllerAdvice
public class GlobalModelAttributes {

    private final SessionService sessionService;

    @Autowired
    public GlobalModelAttributes(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @ModelAttribute("loggedInPerson")
    public Person addLoggedInPerson(@CookieValue(value = "session-id", required = false) String cookieSessionID) {

        if (cookieSessionID == null || cookieSessionID.isBlank()) {
            return createGuestPerson();
        }

        try {
            long sessionId = Long.parseLong(cookieSessionID);
            return sessionService.findById(sessionId)
                    .map(Session::getPerson)
                    .orElseGet(this::createGuestPerson);

        } catch (NumberFormatException e) {
            return createGuestPerson();
        }
    }

    private Person createGuestPerson() {
        Person guest = new Person();
        guest.setName("Gast");
        guest.setRole(null);
        guest.setId(-1L);
        return guest;
    }
}
