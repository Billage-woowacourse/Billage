package sogorae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BillageApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillageApplication.class, args);
    }

}
