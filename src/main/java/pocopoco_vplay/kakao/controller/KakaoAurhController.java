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
import pocopoco_vplay.kakao.model.Kakao;
import pocopoco_vplay.users.exception.UsersException;

@Controller
@RequestMapping("/oauth")
public class KakaoAurhController {
	private final String CLIENT_ID = "ffd6b91df4ad805e542c6a8a450195b3";
    private final String REDIRECT_URI = "http://localhost:8080/oauth/kakao";
    private final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    
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
    	loginUser.setEmail(userInfo.get("email") != null ? userInfo.get("email").toString() : "이메일 없음");
    	loginUser.setId(userInfo.get("id") != null ? userInfo.get("id").toString() : "아이디 없음");
    	loginUser.setNickName(userInfo.get("nickname") != null ? userInfo.get("nickname").toString() : "닉네임 없음");

    	System.out.println(userInfo);
    	
    	session.setAttribute("loginUser", loginUser);
    	
    	return "redirect:/";
    	
    	
    }
    
    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

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
