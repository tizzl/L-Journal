package com.timo.Learning_Journal.service;

import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.repositories.SessionRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {


    private final SessionRepository sessionRepository;

    public Cookie createCookieSession(Session session) {

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
        sessionRepository.save(session);
        return session;
    }

    public Cookie endCookieSession(Session session) {

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

}
