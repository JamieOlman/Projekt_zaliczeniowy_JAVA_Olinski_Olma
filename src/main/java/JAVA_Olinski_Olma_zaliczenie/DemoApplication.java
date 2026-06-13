package JAVA_Olinski_Olma_zaliczenie;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"JAVA_Olinski_Olma_zaliczenie.model"})
@EnableJpaRepositories(basePackages = {"JAVA_Olinski_Olma_zaliczenie.repository"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}