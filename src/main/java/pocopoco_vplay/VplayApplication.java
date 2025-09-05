package pocopoco_vplay;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VplayApplication {
	public static void main(String[] args) {
		SpringApplication.run(VplayApplication.class, args);
	}
}
