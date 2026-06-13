package JAVA_Olinski_Olma_zaliczenie.controller;

import JAVA_Olinski_Olma_zaliczenie.model.Assignment;
import JAVA_Olinski_Olma_zaliczenie.services.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Assignments", description = "Operations on assignments")
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Operation(summary = "Retrieve all assignments", description = "Fetches a list of all assignments from the database")
    @GetMapping("/all")
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @Operation(summary = "Create a new assignment", description = "Creates a new assignment with the provided details")
    @PostMapping("/create")
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing assignment", description = "Updates the assignment with the specified ID")
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @Parameter(name = "id", description = "The unique identifier of the assignment to update", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody Assignment assignmentDetails) {
        var updatedAssignment = assignmentService.updateAssignment(id, assignmentDetails);
        return updatedAssignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete an assignment by ID", description = "Deletes the assignment with the specified ID from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @Parameter(name = "id", description = "The unique identifier of the assignment to delete", required = true, example = "1")
            @PathVariable Long id) {
        if (assignmentService.deleteAssignment(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieve an assignment by ID", description = "Fetches a single assignment by their unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(
            @Parameter(name = "id", description = "The unique identifier of the assignment to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        var assignment = assignmentService.getAssignmentById(id);
        return assignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}