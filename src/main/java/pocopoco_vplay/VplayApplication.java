package pocopoco_vplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import java.util.Map;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class VplayApplication {

	public static void main(String[] args) {
		// ======================================================
		//               환경 변수 확인을 위한 탐정 코드
		// ======================================================
		System.out.println("!!!!!!!!!! 탐정 시작: 서버의 모든 환경 변수를 출력합니다 !!!!!!!!!!");
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}
		System.out.println("!!!!!!!!!! 탐정 종료: 여기까지 모든 환경 변수 목록입니다 !!!!!!!!!!");
		// ======================================================

		SpringApplication.run(VplayApplication.class, args);

	}

}
