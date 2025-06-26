package pocopoco_vplay.kakao.config;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class GoogleOAuthConfig {
    private final Dotenv dotenv;

    public GoogleOAuthConfig() {
        dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .ignoreIfMissing()
                .load();
    }
    public String getClientId() {
        return dotenv.get("GOOGLE_CLIENT_ID");
    }

    public String getClientSecret() {
        return dotenv.get("GOOGLE_CLIENT_SECRET");
    }

    public String getRedirectUri() {
        return dotenv.get("GOOGLE_REDIRECT_URI");
    }

    public String getTokenUrl() {
        return dotenv.get("GOOGLE_TOKEN_URL");
    }

    public String getUserInfoUrl() {
        return dotenv.get("GOOGLE_USER_INFO_URL");
    }
}