package JAVA_Olinski_Olma_zaliczenie;

import JAVA_Olinski_Olma_zaliczenie.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class IntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAllEndpoints() throws Exception {
        // --- COURSES ---
        Course course = new Course();
        course.setName("Java");

        String cRes = mockMvc.perform(post("/api/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Course createdCourse = objectMapper.readValue(cRes, Course.class);

        mockMvc.perform(get("/api/courses/all")).andExpect(status().isOk());
        mockMvc.perform(get("/api/courses/" + createdCourse.getId())).andExpect(status().isOk());

        createdCourse.setName("Java 2");
        mockMvc.perform(put("/api/courses/" + createdCourse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdCourse)))
                .andExpect(status().isOk());

        // --- USERS ---
        User user = new User();
        user.setUsername("TestUser");
        user.setRole("USER");

        String uRes = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(uRes, User.class);

        mockMvc.perform(get("/api/users/all")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/" + createdUser.getId())).andExpect(status().isOk());

        createdUser.setUsername("TestUser2");
        mockMvc.perform(put("/api/users/" + createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser)))
                .andExpect(status().isOk());

        // --- ASSIGNMENTS ---
        Assignment assignment = new Assignment();
        assignment.setTitle("Zadanie");
        assignment.setAssignmentType(AssignmentType.EXAM);
        assignment.setCourse(createdCourse);
        assignment.setUser(createdUser);

        String aRes = mockMvc.perform(post("/api/assignments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignment)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Assignment createdAssignment = objectMapper.readValue(aRes, Assignment.class);

        mockMvc.perform(get("/api/assignments/all")).andExpect(status().isOk());
        mockMvc.perform(get("/api/assignments/" + createdAssignment.getId())).andExpect(status().isOk());

        createdAssignment.setTitle("Zadanie 2");
        mockMvc.perform(put("/api/assignments/" + createdAssignment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdAssignment)))
                .andExpect(status().isOk());

        // --- DELETE ALL ---
        mockMvc.perform(delete("/api/assignments/" + createdAssignment.getId())).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/courses/" + createdCourse.getId())).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/users/" + createdUser.getId())).andExpect(status().isNoContent());

        // --- NOT FOUND TESTS  ---
        mockMvc.perform(get("/api/courses/9999")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/courses/9999")).andExpect(status().isNotFound());
        mockMvc.perform(put("/api/courses/9999").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isNotFound());

        mockMvc.perform(get("/api/users/9999")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/users/9999")).andExpect(status().isNotFound());
        mockMvc.perform(put("/api/users/9999").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isNotFound());

        mockMvc.perform(get("/api/assignments/9999")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/assignments/9999")).andExpect(status().isNotFound());
        mockMvc.perform(put("/api/assignments/9999").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isNotFound());
    }
}