package JAVA_Olinski_Olma_zaliczenie.services;

import JAVA_Olinski_Olma_zaliczenie.model.Certificate;
import JAVA_Olinski_Olma_zaliczenie.model.Course;
import JAVA_Olinski_Olma_zaliczenie.repository.CertificateRepository;
import JAVA_Olinski_Olma_zaliczenie.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CertificateRepository certificateRepository;


    public CourseService(CourseRepository courseRepository, CertificateRepository certificateRepository) {
        this.courseRepository = courseRepository;
        this.certificateRepository = certificateRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> updateCourse(Long id, Course courseDetails) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course existingCourse = courseOpt.get();
            existingCourse.setName(courseDetails.getName());
            if (courseDetails.getUsers() != null) {
                existingCourse.setUsers(courseDetails.getUsers());
            }
            return Optional.of(courseRepository.save(existingCourse));
        }
        return Optional.empty();
    }

    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }


    public String issueCertificate(String username, Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            String text = "CERTYFIKAT ukończenia kursu: " + course.getName() + " dla studenta: " + username;


            Certificate certificate = new Certificate();
            certificate.setUsername(username);
            certificate.setCourse(course);
            certificate.setCertificateText(text);
            certificateRepository.save(certificate);

            return text;
        }
        return "Nie można wystawić certyfikatu - brak takiego kursu.";
    }
}