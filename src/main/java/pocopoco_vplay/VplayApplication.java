package pocopoco_vplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class VplayApplication {

	public static void main(String[] args) {


		SpringApplication.run(VplayApplication.class, args);
	}

}
