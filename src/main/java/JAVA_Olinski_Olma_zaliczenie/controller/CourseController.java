package JAVA_Olinski_Olma_zaliczenie.controller;

import JAVA_Olinski_Olma_zaliczenie.model.Course;
import JAVA_Olinski_Olma_zaliczenie.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "Operations on courses")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Retrieve all courses", description = "Fetches a list of all courses from the database")
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @Operation(summary = "Create a new course", description = "Creates a new course with the provided details")
    @PostMapping("/create")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing course", description = "Updates the course with the specified ID")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @Parameter(name = "id", description = "The unique identifier of the course to update", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody Course courseDetails) {
        var updatedCourse = courseService.updateCourse(id, courseDetails);
        return updatedCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a course by ID", description = "Deletes the course with the specified ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @Parameter(name = "id", description = "The unique identifier of the course to delete", required = true, example = "1")
            @PathVariable Long id) {
        if (courseService.deleteCourse(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieve a course by ID", description = "Fetches a single course by their unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
            @Parameter(name = "id", description = "The unique identifier of the course to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        var course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Issue and save a certificate", description = "Generates a certificate for a user, saves it to the DB and returns the text")
    @PostMapping("/{id}/issue-certificate")
    public ResponseEntity<String> issueCertificate(
            @Parameter(name = "id", description = "The unique identifier of the course", required = true, example = "1")
            @PathVariable Long id,
            @RequestParam String username) {

        String result = courseService.issueCertificate(username, id);

        if (result.startsWith("Nie można")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}