package com.timo.Learning_Journal.controller;

import com.timo.Learning_Journal.entity.Entry;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Role;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.service.EntryService;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public class AuthController {
}
