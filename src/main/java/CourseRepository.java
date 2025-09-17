import com.timo.Learning_Journal.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseName(String courseName);
}
