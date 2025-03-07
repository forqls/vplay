package pocopoco_vplay.kakao.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.kakao.model.Kakao;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class KakaoAurhController {
	private final String CLIENT_ID = "ffd6b91df4ad805e542c6a8a450195b3";
    private final String REDIRECT_URI = "http://localhost:8080/oauth/kakao";
    private final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final UsersService uService;
    @GetMapping("/kakao")
    public String kakaoLogin(@RequestParam("code") String code , HttpSession session){
    	String accessToken = getAccessToken(code); // 토큰 요청
    	
    	if(accessToken == null) {
    		throw new UsersException("토큰 못가져옴 ㅋㅋ");
    	}
    	
    	Map<String,Object> userInfo = getUserInfo(accessToken);
    	
    	if(userInfo == null) {
    		throw new UsersException("사용자 정보 못가져옴 ㅋㅋ");
    	}
    	Kakao loginUser = new Kakao();
    	
    	String kakaoId = userInfo.get("id").toString(); // 카카오 고유 ID
	    String nickname = userInfo.get("nickname") != null ? userInfo.get("nickname").toString() : "닉네임 없음";
	    
	    Users result = uService.existUsers(kakaoId);
	    
	    Users users = new Users();
	    
	    
	    if(result != null) {
	    	session.setAttribute("loginUser", result);
	    	
	    	return "redirect:/";
	    	
	    }else {
	    	users.setKakaoId(kakaoId);
	    	users.setLoginType("K");
	    	session.setAttribute("kakaoUser", users);
	    	
	    	return "redirect:/users/signUp";
	    	
	    }
    	
    }
    
    
    
    
    
    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        // HTTP 클라이언트 이며 RESTful API 와 쉽게 상호작용이 가능함 , 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        //HttpEntitiy 는 HTTP 의 본문과 헤더를 캡슐화 하는 클래스임. 제네릭 타입을 사용해서 어떤 데이터든 담을 수 있도록 설계 되어있음.
        // 보통 RestTemplate랑 함께 사용함 동기방식이라 비동기방식(논블로킹 방식) 보다 성능이 떨어질 수 있음.근데 카카오 로그인같은 허접 코드는
        // RestTemplate로 10가능 ㅋㅋ 
        ResponseEntity<Map> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        return responseBody != null ? (String) responseBody.get("access_token") : null;
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, request, Map.class);

        return response.getBody();
    }
    
    
    
    
    
}
