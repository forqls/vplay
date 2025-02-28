package pocopoco_vplay.commom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateResolverConfig {
	
	@Bean
	public ClassLoaderTemplateResolver usersResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/users/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver adminResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/admin/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver myPageResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/myPage/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	} 
	
	@Bean
	public ClassLoaderTemplateResolver inqueryResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/inquery/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver requestResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/request/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver allMenuResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver fontsResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/fonts/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver GraphicTemplatesResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/GraphicTemplates/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver musicResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/music/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver soundEffectsResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/soundEffects/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver stockVideoResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/stockVideo/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver videoTemplatesResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/board/content/videoTemplates/");
		mResolver.setSuffix(".html");
		
		mResolver.setTemplateMode(TemplateMode.HTML);
		//HTML 설정
		
		mResolver.setCharacterEncoding("UTF-8");
		//한글이니깐 UTF-8
		
		mResolver.setCacheable(false);
		//자동 새로고침 기능

		mResolver.setCheckExistence(true);
		//templates/views/member/ 의 각 목록마다 한개씩 다 비교해서 찾아주게 하는 기능
		
		return mResolver;
	}
}
