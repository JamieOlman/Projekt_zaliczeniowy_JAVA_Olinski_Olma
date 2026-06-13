package JAVA_Olinski_Olma_zaliczenie.services;

import JAVA_Olinski_Olma_zaliczenie.model.User;
import JAVA_Olinski_Olma_zaliczenie.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void shouldCreateUser() {
        User u = new User(); u.setUsername("Jan");
        when(userRepository.save(any())).thenReturn(u);
        assertEquals("Jan", userService.createUser(u).getUsername());
    }

    @Test
    void shouldUpdateUser() {
        User existing = new User(); existing.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User details = new User(); details.setUsername("Nowy"); details.setCourses(new HashSet<>());
        Optional<User> res = userService.updateUser(1L, details);

        assertTrue(res.isPresent());
        assertEquals("Nowy", res.get().getUsername());

        assertFalse(userService.updateUser(99L, details).isPresent());
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        assertTrue(userService.deleteUser(1L));
        assertFalse(userService.deleteUser(99L));
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        assertTrue(userService.getUserById(1L).isPresent());
    }
}