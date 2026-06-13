package JAVA_Olinski_Olma_zaliczenie.model;

import JAVA_Olinski_Olma_zaliczenie.priority.PriorityLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_type")
    private AssignmentType assignmentType;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "priority")
    private String priority;

    @Transient
    @JsonIgnore
    private PriorityLevel priorityLevel;

    @JsonIgnore
    public void setPriority(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        if (priorityLevel != null) {
            this.priority = priorityLevel.getPriority();
        } else {
            this.priority = "NONE";
        }
    }
}