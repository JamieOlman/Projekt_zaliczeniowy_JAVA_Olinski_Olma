package JAVA_Olinski_Olma_zaliczenie.services;

import JAVA_Olinski_Olma_zaliczenie.factory.AssignmentFactory;
import JAVA_Olinski_Olma_zaliczenie.model.Assignment;
import JAVA_Olinski_Olma_zaliczenie.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment createAssignment(Assignment assignmentDetails) {
        Assignment newAssignment = AssignmentFactory.createAssignment(
                assignmentDetails.getAssignmentType(),
                assignmentDetails.getTitle(),
                assignmentDetails.getDescription()
        );

        newAssignment.setCourse(assignmentDetails.getCourse());
        newAssignment.setUser(assignmentDetails.getUser());

        return assignmentRepository.save(newAssignment);
    }

    public Optional<Assignment> updateAssignment(Long id, Assignment assignmentDetails) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isPresent()) {
            Assignment existingAssignment = assignmentOpt.get();
            existingAssignment.setTitle(assignmentDetails.getTitle());
            existingAssignment.setDescription(assignmentDetails.getDescription());
            existingAssignment.setAssignmentType(assignmentDetails.getAssignmentType());
            existingAssignment.setCourse(assignmentDetails.getCourse());
            existingAssignment.setUser(assignmentDetails.getUser());

            Assignment temporary = AssignmentFactory.createAssignment(
                    assignmentDetails.getAssignmentType(),
                    assignmentDetails.getTitle(),
                    assignmentDetails.getDescription()
            );
            existingAssignment.setPriority(temporary.getPriorityLevel());

            return Optional.of(assignmentRepository.save(existingAssignment));
        }
        return Optional.empty();
    }

    public boolean deleteAssignment(Long id) {
        if (assignmentRepository.existsById(id)) {
            assignmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public Optional<Assignment> gradeAssignment(Long id, String grade) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isPresent()) {
            Assignment assignment = assignmentOpt.get();

            assignment.setDescription("OCENA: " + grade + " | " + assignment.getDescription());
            return Optional.of(assignmentRepository.save(assignment));
        }
        return Optional.empty();
    }


    public String getCourseProgress(Long courseId, Long userId) {
        List<Assignment> all = assignmentRepository.findAll();
        List<Assignment> userCourseAssignments = all.stream()
                .filter(a -> a.getCourse() != null && a.getCourse().getId().equals(courseId))
                .filter(a -> a.getUser() != null && a.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        if (userCourseAssignments.isEmpty()) {
            return "0%";
        }

        long graded = userCourseAssignments.stream()
                .filter(a -> a.getDescription() != null && a.getDescription().contains("OCENA:"))
                .count();

        long progressPercentage = (graded * 100) / userCourseAssignments.size();
        return progressPercentage + "%";
    }
}