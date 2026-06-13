package JAVA_Olinski_Olma_zaliczenie.services;

import JAVA_Olinski_Olma_zaliczenie.model.Course;
import JAVA_Olinski_Olma_zaliczenie.repository.CertificateRepository;
import JAVA_Olinski_Olma_zaliczenie.repository.CourseRepository;
import JAVA_Olinski_Olma_zaliczenie.model.Certificate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(new Course(), new Course()));
        assertEquals(2, courseService.getAllCourses().size());
    }

    @Test
    void shouldCreateCourse() {
        Course c = new Course(); c.setName("Java");
        when(courseRepository.save(any())).thenReturn(c);
        assertEquals("Java", courseService.createCourse(c).getName());
    }

    @Test
    void shouldUpdateCourse() {
        Course existing = new Course(); existing.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Course details = new Course(); details.setName("Nowy"); details.setUsers(new HashSet<>());
        Optional<Course> res = courseService.updateCourse(1L, details);

        assertTrue(res.isPresent());
        assertEquals("Nowy", res.get().getName());

        assertFalse(courseService.updateCourse(99L, details).isPresent());
    }

    @Test
    void shouldDeleteCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        assertTrue(courseService.deleteCourse(1L));
        assertFalse(courseService.deleteCourse(99L));
    }

    @Test
    void shouldGetCourseById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(new Course()));
        assertTrue(courseService.getCourseById(1L).isPresent());
    }

    @Test
    void shouldIssueCertificate() {
        // 1. Przygotowanie danych (Arrange)
        Course c = new Course();
        c.setId(1L);
        c.setName("Java");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(c));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        // 2. Testowanie pozytywnego scenariusza (Act & Assert)
        String successMessage = courseService.issueCertificate("Jan", 1L);
        assertTrue(successMessage.contains("CERTYFIKAT ukończenia kursu"));

        // NOWE i NAJWAŻNIEJSZE: Weryfikujemy, czy serwis faktycznie zapisał certyfikat do bazy!
        // Sprawdzamy czy na obiekcie certificateRepository wywołano metodę save() z dowolnym obiektem klasy Certificate.
        verify(certificateRepository, times(1)).save(any(Certificate.class));

        // 3. Testowanie negatytywnego scenariusza (Act & Assert)
        String failureMessage = courseService.issueCertificate("Jan", 99L);
        assertTrue(failureMessage.contains("Nie można wystawić"));

        // Upewniamy się, że przy błędzie NIE zapisano nic do bazy danych
        // Wcześniej wywołaliśmy save() raz (przy sukcesie), więc sprawdzamy, czy licznik wywołań to wciąż dokładnie 1.
        verify(certificateRepository, times(1)).save(any(Certificate.class));
    }
}