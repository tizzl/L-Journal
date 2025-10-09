package com.timo.Learning_Journal.service;

import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.repositories.SessionRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    public void loginUser(Person person){
        LOG.info("Nutzer {} hat sich eingelogged!", person.getName());
    }

    public void logoutUser(Person person){
        LOG.info("Nutzer {} hat sich ausgelogged!", person.getName());
    }
    private final SessionRepository sessionRepository;

    public Cookie createCookieSession(Session session) {

        if (session == null || session.getId() == null) {
            LOG.error("Fehler beim Erstellen des Cookies: Session oder ID ist null!");
            throw new IllegalArgumentException("Ungültige Session");
        }

        Cookie cookie = new Cookie("session-id", Long.toString(session.getId()));
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public Session createSession(Person person) {
        Session session = new Session();
        session.setPerson(person);
        Session savedSession = sessionRepository.save(session);
        LOG.info("Neue Session {} für Nutzer {} erstellt", savedSession.getId(), person.getName());
        return savedSession;
    }

    public Cookie endCookieSession(Session session) {

        try {
            sessionRepository.deleteById(session.getId());
            LOG.info("Session {} erfolgreich beendet", session.getId());
        } catch (Exception e) {
            LOG.error("Fehler beim Beenden der Session {}: {}", session.getId(), e.getMessage());
        }

        sessionRepository.deleteById(session.getId());
        Cookie cookie = new Cookie("session-id", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;

    }

    public Optional<Session> findById(long id) {
        return sessionRepository.findById(id);
    }

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public Optional<Person> getPersonFromSession(String cookieSessionId) {
        return findById(Long.parseLong(cookieSessionId))
                .map(Session::getPerson);
    }
}
