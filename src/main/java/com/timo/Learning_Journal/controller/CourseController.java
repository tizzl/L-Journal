package com.timo.Learning_Journal.controller;

import com.timo.Learning_Journal.entity.Course;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Role;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.repositories.CourseRepository;
import com.timo.Learning_Journal.service.CourseService;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final PersonService personService;
    private final SessionService sessionService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @PostMapping("/new-course")
    public String createCourse(@RequestParam String courseName, @RequestParam String courseDescription, HttpServletRequest request) {

        // Lehrer aus Session holen
        String cookieSessionId = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("session-id".equals(cookie.getName())) cookieSessionId = cookie.getValue();
            }
        }
        if (cookieSessionId == null) return "redirect:/login";

        Session session = sessionService.findById(Long.parseLong(cookieSessionId)).orElse(null);
        if (session == null) return "redirect:/login";

        Person teacher = session.getPerson();
        if (teacher.getRole() != Role.TEACHER) return "redirect:/";


        // Course anlegen
        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        course.setTeacher(teacher);
        courseService.save(course);

        return "redirect:/edit-course/" + course.getCourseID();
    }

    @GetMapping("/new-course")
    public String newCourseForm(Model model) {
        // Liste aller Lehrer aus DB holen
        List<Person> teachers = personService.findAllByRole(Role.TEACHER);
        List<Person> students = personService.findAllByRole(Role.STUDENT);
        students.sort(Comparator.comparing(Person::getName));
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        return "new-course"; // Thymeleaf Template
    }

    @PostMapping("/edit-course/{id}")
    public String editCourse(
            @PathVariable Long id,
            @RequestParam String courseName,
            @RequestParam String courseDescription,
            @RequestParam(required = false) List<Long> students,
            HttpServletRequest request) {

        // Session prüfen
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("session-id".equals(cookie.getName())) {
                    break;
                }
            }
        }

        //Kurs laden
        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs nicht gefunden!"));

        //Kursname/Beschreibung updaten
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        courseService.save(course);

        //Schüler aktualisieren
        List<Person> currentStudents = List.copyOf(course.getStudents());
        for (Person student : currentStudents) {
            courseService.removeStudentFromCourse(course.getCourseID(), student.getId());
        }

        if (students != null) {
            for (Long studentId : students) {
                courseService.addStudentToCourse(course.getCourseID(), studentId);
            }
        }

        return "redirect:/edit-course/" + course.getCourseID();
    }


    @GetMapping("/edit-course/{id}")
    public String editCourseForm(Model model, @PathVariable Long id) {
        Course course = courseService.findById(id)
                .orElseThrow(()  -> new RuntimeException("Kurs nicht gefunden!"));

        // Liste aller Schueler aus DB holen
        List<Person> students = personService.findAllByRole(Role.STUDENT);

        model.addAttribute("course", course);
        model.addAttribute("students", students);

        return "edit-course"; // Thymeleaf Template

    }
}
