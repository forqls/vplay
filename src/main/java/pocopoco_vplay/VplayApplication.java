package pocopoco_vplay;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class VplayApplication {

	public static void main(String[] args) {

		// ======================================================
		//           '슈퍼 탐정 코드' - 이 부분을 붙여넣어 주세요!
		// ======================================================
		System.out.println("!!!!!!!!!! 슈퍼 탐정 시작: mappers 폴더 안의 파일을 찾아봅니다 !!!!!!!!!!");
		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:mappers/*.xml"); // 경로를 더 간단하게 변경
			if (resources.length == 0) {
				System.out.println("!!!!!!!!!! mappers 폴더에서 아무 XML 파일도 찾지 못했습니다. !!!!!!!!!!");
			} else {
				for (Resource resource : resources) {
					System.out.println("!!!!!!!!!! 발견된 파일: " + resource.getFilename() + " !!!!!!!!!!");
				}
			}
		} catch (IOException e) {
			System.out.println("!!!!!!!!!! 파일을 찾는 도중 에러 발생: " + e.getMessage() + " !!!!!!!!!!");
		}
		System.out.println("!!!!!!!!!! 슈퍼 탐정 종료 !!!!!!!!!!");
		// ======================================================

		SpringApplication.run(VplayApplication.class, args);
	}

}
