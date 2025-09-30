package com.timo.Learning_Journal.repositories;

import com.timo.Learning_Journal.entity.Course;
import com.timo.Learning_Journal.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseName(String courseName);
    List<Course> findByTeacherId(Long id);
}
