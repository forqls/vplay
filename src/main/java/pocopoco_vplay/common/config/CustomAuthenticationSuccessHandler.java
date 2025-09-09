// pocopoco_vplay/common/config/CustomAuthenticationSuccessHandler.java
package pocopoco_vplay.common.config;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsersService uService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();
        Users user = new Users();
        user.setUserId(userId);
        Users loginUser = uService.signIn(user);

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

        Timestamp loginUserPaymentDate = (Timestamp) uService.getPaymentDate(loginUser);

        // loginUserPaymentDate가 null이 아닐 때만 실행
        if (loginUserPaymentDate != null) {
            LocalDateTime localDateTime = loginUserPaymentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Timestamp loginUserPaymentEndDate = Timestamp.valueOf(localDateTime.plusDays(30));
            Timestamp today = Timestamp.valueOf(LocalDateTime.now());

            boolean isPaymentExpired = today.after(loginUserPaymentEndDate);
            boolean hasSeenAlert = loginUser.isAlertShown();

            if (isPaymentExpired && !hasSeenAlert) {
                uService.deleteUserPlan(loginUser);
                uService.updateAlertShown(loginUser.getUserNo());

                loginUser = uService.signIn(user);
                session.setAttribute("loginUser", loginUser);
            }
        }

        String targetUrl = "/";
        String beforeURL = (String) session.getAttribute("beforeURL");
        if (beforeURL != null && !beforeURL.contains("/signIn")) {
            targetUrl = beforeURL;
        } else if ("Y".equals(loginUser.getIsAdmin())) {
            targetUrl = "/admin/dashboard";
        }

        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}