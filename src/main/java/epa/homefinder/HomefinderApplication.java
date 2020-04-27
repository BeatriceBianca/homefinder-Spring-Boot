package epa.homefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("epa.homefinder")
@EntityScan("epa.homefinder.entity")
public class HomefinderApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomefinderApplication.class, args);
	}
}
