package JAVA_Olinski_Olma_zaliczenie.services;

import JAVA_Olinski_Olma_zaliczenie.model.*;
import JAVA_Olinski_Olma_zaliczenie.repository.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllAssignments() {
        when(assignmentRepository.findAll()).thenReturn(Arrays.asList(new Assignment(), new Assignment()));
        assertEquals(2, assignmentService.getAllAssignments().size());
    }

    @Test
    void shouldCreateAssignmentAllTypes() {
        Assignment a1 = new Assignment(); a1.setAssignmentType(AssignmentType.EXAM);
        Assignment a2 = new Assignment(); a2.setAssignmentType(AssignmentType.QUIZ);
        Assignment a3 = new Assignment(); a3.setAssignmentType(AssignmentType.HOMEWORK);
        Assignment a4 = new Assignment(); a4.setAssignmentType(null);

        when(assignmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals("HIGH", assignmentService.createAssignment(a1).getPriority());
        assertEquals("MEDIUM", assignmentService.createAssignment(a2).getPriority());
        assertEquals("LOW", assignmentService.createAssignment(a3).getPriority());
        assertEquals("LOW", assignmentService.createAssignment(a4).getPriority());
    }

    @Test
    void shouldUpdateAssignment() {
        Assignment existing = new Assignment(); existing.setId(1L);
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(assignmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Assignment details = new Assignment(); details.setTitle("Nowe"); details.setAssignmentType(AssignmentType.QUIZ);

        Optional<Assignment> res = assignmentService.updateAssignment(1L, details);
        assertTrue(res.isPresent());
        assertEquals("Nowe", res.get().getTitle());
        assertEquals("MEDIUM", res.get().getPriority());

        assertFalse(assignmentService.updateAssignment(99L, details).isPresent());
    }

    @Test
    void shouldDeleteAssignment() {
        when(assignmentRepository.existsById(1L)).thenReturn(true);
        assertTrue(assignmentService.deleteAssignment(1L));
        assertFalse(assignmentService.deleteAssignment(99L));
    }

    @Test
    void shouldGetAssignmentById() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(new Assignment()));
        assertTrue(assignmentService.getAssignmentById(1L).isPresent());
    }

    @Test
    void shouldGradeAssignment() {
        Assignment a = new Assignment(); a.setDescription("Opis");
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(a));
        when(assignmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<Assignment> res = assignmentService.gradeAssignment(1L, "5.0");
        assertTrue(res.isPresent());
        assertTrue(res.get().getDescription().contains("OCENA: 5.0"));

        assertFalse(assignmentService.gradeAssignment(99L, "5.0").isPresent());
    }

    @Test
    void shouldGetCourseProgress() {
        Course c = new Course(); c.setId(1L);
        User u = new User(); u.setId(1L);

        Assignment a1 = new Assignment(); a1.setCourse(c); a1.setUser(u); a1.setDescription("OCENA: 5.0");
        Assignment a2 = new Assignment(); a2.setCourse(c); a2.setUser(u); a2.setDescription("Brak");
        Assignment a3 = new Assignment();

        when(assignmentRepository.findAll()).thenReturn(Arrays.asList(a1, a2, a3));

        assertEquals("50%", assignmentService.getCourseProgress(1L, 1L));
        assertEquals("0%", assignmentService.getCourseProgress(99L, 99L));
    }
}