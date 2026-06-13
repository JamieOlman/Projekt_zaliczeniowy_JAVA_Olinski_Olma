package JAVA_Olinski_Olma_zaliczenie.repository;

import JAVA_Olinski_Olma_zaliczenie.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}