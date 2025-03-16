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
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.kakao.config.GoogleOAuthConfig;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class KakaoAurhController {
    private final UsersService uService;
    private final GoogleOAuthConfig googleOAuthConfig;
    private final String KAKAO_CLIENT_ID = "ffd6b91df4ad805e542c6a8a450195b3";
    private final String KAKAO_REDIRECT_URI = "http://localhost:8080/oauth/kakao";
    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";



    private final String GOOGLE_REDIRECT_URI = "http://localhost:8080/oauth/google";
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";



    @GetMapping("/kakao")
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session) {
        String accessToken = getAccessToken(code, KAKAO_TOKEN_URL, KAKAO_CLIENT_ID, KAKAO_REDIRECT_URI,null);
        if (accessToken == null) throw new UsersException("카카오 토큰 못 가져옴 ㅋㅋ");

        Map<String, Object> userInfo = getUserInfo(accessToken, KAKAO_USER_INFO_URL);
        if (userInfo == null) throw new UsersException("카카오 사용자 정보 못 가져옴 ㅋㅋ");

        String kakaoId = userInfo.get("id").toString();
        String nickname = userInfo.get("nickname") != null ? userInfo.get("nickname").toString() : "닉네임 없음";

        Users result = uService.existUsers(kakaoId);

        if (result != null) {
            session.setAttribute("loginUser", result);
            return "redirect:/";
        } else {
            Users users = new Users();
            users.setKakaoId(kakaoId);
            users.setUserId(null); 
            users.setLoginType("K"); 
            users.setUserNickname(nickname);
            session.setAttribute("kakaoUser", users);
            return "redirect:/users/signUp";
        }
    }

    @GetMapping("/google")
    public String googleLogin(@RequestParam("code") String code, HttpSession session) {
    	 String CLIENT_ID = googleOAuthConfig.getClientId();
    	 String CLIENT_SECRET = googleOAuthConfig.getClientSecret();
        String accessToken = getAccessToken(code, GOOGLE_TOKEN_URL, CLIENT_ID, GOOGLE_REDIRECT_URI,CLIENT_SECRET);
        if (accessToken == null) throw new UsersException("구글 토큰 못 가져옴 ㅋㅋ");

        Map<String, Object> userInfo = getUserInfo(accessToken, GOOGLE_USER_INFO_URL);
        if (userInfo == null) throw new UsersException("구글 사용자 정보 못 가져옴 ㅋㅋ");

        String googleId = userInfo.get("id").toString();
        String email = userInfo.get("email") != null ? userInfo.get("email").toString() : "이메일 없음";

        Users result = uService.existGoogleUsers(googleId);

        if (result != null) {
            session.setAttribute("loginUser", result);
            return "redirect:/";
        } else {
            Users users = new Users();
            users.setGoogleId(googleId);
            users.setUserId(null);
            users.setLoginType("G"); 
            users.setUserEmail(email);
            session.setAttribute("googleUser", users);
            return "redirect:/users/signUp";
        }
    }

    private String getAccessToken(String code, String tokenUrl, String clientId, String redirectUri,String clientSecret) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        
        if (clientSecret != null) {
            params.add("client_secret", clientSecret);
        }
        

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        return responseBody != null ? (String) responseBody.get("access_token") : null;
    }

    private Map<String, Object> getUserInfo(String accessToken, String userInfoUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
        return response.getBody();
    }
    
}
