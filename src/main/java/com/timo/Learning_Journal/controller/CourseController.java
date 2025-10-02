package com.timo.Learning_Journal.controller;

import com.timo.Learning_Journal.entity.Course;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Role;
import com.timo.Learning_Journal.entity.Session;
import com.timo.Learning_Journal.repositories.CourseRepository;
import com.timo.Learning_Journal.service.CourseService;
import com.timo.Learning_Journal.service.PersonService;
import com.timo.Learning_Journal.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final PersonService personService;
    private final SessionService sessionService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @PostMapping("/new-course")
    public String createCourse(@RequestParam String courseName,
                               @RequestParam String courseDescription,
                               @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {

        // Session der Person aus Session holen
        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
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
    public String newCourseForm(Model model,
                                @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {

        // Session der Person aus Session holen
        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
        if (session == null) return "redirect:/login";


        // Liste aller Lehrer aus DB holen
        List<Person> teachers = personService.findAllByRole(Role.TEACHER);
        List<Person> students = personService.findAllByRole(Role.STUDENT);
        students.sort(Comparator.comparing(Person::getName));
        model.addAttribute("person", session.getPerson());
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        return "new-course"; // Thymeleaf Template
    }

    @PostMapping("/edit-course/{id}")
    public String editCourse(Model model,
                             @PathVariable Long id,
                             @RequestParam String courseName,
                             @RequestParam String courseDescription,
                             @RequestParam(required = false) List<Long> students,
                             @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {


        // Session der Person aus Session holen
        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
        if (session == null) return "redirect:/login";

        Person teacher = session.getPerson();
        if (teacher.getRole() != Role.TEACHER) return "redirect:/";
        model.addAttribute("person", session.getPerson());


        //Kurs laden
        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs nicht gefunden!"));

        //Kursname/Beschreibung updaten
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        courseService.save(course);

        //Schüler hinzufügen

        if (students != null) {
            for (Long studentId : students) {
                courseService.addStudentToCourse(course.getCourseID(), studentId);
            }
        }

        return "redirect:/edit-course/" + course.getCourseID();
    }


    @GetMapping("/edit-course/{id}")
    public String editCourseForm(Model model,
                                 @PathVariable Long id,
                                 @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {

        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
        if (session == null) return "redirect:/login";


        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs nicht gefunden!"));


        // Liste aller Nutzer aus DB holen
        List<Person> students = personService.findAllByRole(Role.STUDENT);
        model.addAttribute("person", session.getPerson());
        model.addAttribute("course", course);
        model.addAttribute("students", students);

        return "edit-course"; // Thymeleaf Template

    }

    @PostMapping("/edit-course/{courseID}/remove-student/{studentId}")
    public String removeStudentFromCourse(Model model,
                                          @PathVariable Long courseID,
                                          @PathVariable Long studentId,
                                          @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {

        // Session prüfen
        Session session = sessionService.findById(Long.parseLong(cookieSessionID)).orElse(null);
        if (session == null) return "redirect:/login";

        //Kurs laden
        Course course = courseService.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Kurs nicht gefunden!"));

        Person teacher = session.getPerson();
        if (teacher.getRole() != Role.TEACHER) return "redirect:/";

        // Schüler aus Kurs entfernen

        courseService.removeStudentFromCourse(courseID, studentId);




        // Zurück zur Kursbearbeitung
        return "redirect:/edit-course/" + courseID;
    }


    @GetMapping("/courses")
    public String courseList(Model model,
                             @CookieValue(value = "session-id", defaultValue = "0") String cookieSessionID) {
        Optional<Person> personOpt = sessionService.getPersonFromSession(cookieSessionID);
        if (personOpt.isEmpty())
            return "redirect:/login";

        Person person = personOpt.get();
        model.addAttribute("person", person);

        if (person.getRole() == Role.TEACHER){
            List<Course> courses = courseService.findByTeacher(person);
            model.addAttribute("courses", courses);
        }
        return "courses";
    }
    @GetMapping("/course/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs nicht gefunden!"));
        model.addAttribute("course", course);

        return "course";
    }
}
