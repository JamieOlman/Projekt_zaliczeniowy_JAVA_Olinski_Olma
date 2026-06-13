package JAVA_Olinski_Olma_zaliczenie.repository;

import JAVA_Olinski_Olma_zaliczenie.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}