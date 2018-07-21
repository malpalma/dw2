package mp.dw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DWApp {
	public static void main(String[] args) {
		SpringApplication.run(DWApp.class, args);
    }
}
