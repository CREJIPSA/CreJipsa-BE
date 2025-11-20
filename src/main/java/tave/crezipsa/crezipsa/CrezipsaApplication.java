package tave.crezipsa.crezipsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CrezipsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrezipsaApplication.class, args);
	}

}
