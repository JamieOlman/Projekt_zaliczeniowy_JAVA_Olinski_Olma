package JAVA_Olinski_Olma_zaliczenie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @ManyToMany(mappedBy = "users")
    @JsonIgnoreProperties("users")
    private Set<Course> courses;

    @Column(nullable = false)
    private String role = "USER";
}