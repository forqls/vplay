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

        // 2. [안전하게 수정된 결제 로직] 어떤 경우에도 오류가 나지 않도록 try-catch로 감쌈
        try {
            Timestamp loginUserPaymentDate = (Timestamp) uService.getPaymentDate(loginUser);
            if (loginUserPaymentDate != null) {
                LocalDateTime localDateTime = loginUserPaymentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Timestamp loginUserPaymentEndDate = Timestamp.valueOf(localDateTime.plusDays(30));
                Timestamp today = Timestamp.valueOf(LocalDateTime.now());

                boolean isPaymentExpired = today.after(loginUserPaymentEndDate);
                boolean hasSeenAlert = loginUser.isAlertShown();

                if (isPaymentExpired && !hasSeenAlert) {
                    uService.deleteUserPlan(loginUser);
                    uService.updateAlertShown(loginUser.getUserNo());

                    // 세션 정보를 최신 상태로 업데이트
                    Users updatedUser = uService.signIn(user);
                    session.setAttribute("loginUser", updatedUser);
                }
            }
        } catch (Exception e) {
            System.err.println("결제 만료일 확인 중 오류 발생 (로그인 진행에는 영향 없음): " + e.getMessage());
        }

        // 3. 세션에서 최신 사용자 정보를 다시 가져옴
        Users finalLoginUser = (Users) session.getAttribute("loginUser");
        String isAdmin = finalLoginUser.getIsAdmin();

        String targetUrl = "/";
        String beforeURL = (String) session.getAttribute("beforeURL");

        if (beforeURL != null && !beforeURL.contains("/signIn")) {
            targetUrl = beforeURL;
        } else if (isAdmin != null && "Y".equalsIgnoreCase(isAdmin.trim())) {
            // is_admin 값이 'Y' 또는 'y'이고, 공백이 있어도 관리자로 인식
            targetUrl = "/admin/dashboard";
        }

        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}