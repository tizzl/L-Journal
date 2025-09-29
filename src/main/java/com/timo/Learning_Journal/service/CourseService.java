package com.timo.Learning_Journal.service;

import com.timo.Learning_Journal.entity.Course;
import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.entity.Role;
import com.timo.Learning_Journal.repositories.CourseRepository;
import com.timo.Learning_Journal.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    //Schüler zu Kurs hinzufügen
    public void addStudentToCourse(Long courseId, Long personId) {
        Course course = getCourse(courseId);
        Person student = getPerson(personId, "Schüler");
        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Nur Schüler können hinzugefügt werden!");
        }
        student.getCourses().add(course);
        course.getStudents().add(student);
        courseRepository.save(course);
    }

    //Schüler aus Kurs entfernen
    public void removeStudentFromCourse(Long courseId, Long personId) {
        Course course = getCourse(courseId);
        Person student = getPerson(personId, "Schüler");
        course.getStudents().remove(student);
        personRepository.save(student);
        courseRepository.save(course);
    }
    //Lehrer/Admin zuweisen
    public void assignTeacherToCourse(Long courseId, Long personId) {
        Course course = getCourse(courseId);
        Person teacher = getPerson(personId, "Lehrer");

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Nur Lehrer können Admin sein!");
        }
        course.setTeacher(teacher);
        courseRepository.save(course);
    }
    //Lehrer aus Kurs entfernen
    public void removeTeacherFromCourse(Long courseId, Long personId) {
        Course course = getCourse(courseId);
        Person teacher = getPerson(personId, "Lehrer");
        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Nur Lehrer können vom Admin entbunden werden!");
        }
        course.setTeacher(null);
        courseRepository.save(course);
    }
    //Kurs einsetzen
    private Course getCourse (Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs nicht gefunden!"));
    }
    //Person einsetzen
    private Person getPerson (Long personId, String personRole) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException(personRole + " nicht gefunden!"));
    }
}

