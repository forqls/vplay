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
                    Users updatedUser = uService.signIn(user);
                    session.setAttribute("loginUser", updatedUser);
                }
            }
        } catch (Exception e) {
            System.err.println("결제 만료일 확인 중 오류 발생 (로그인 진행에는 영향 없음): " + e.getMessage());
        }

        Users finalLoginUser = (Users) session.getAttribute("loginUser");
        String isAdmin = finalLoginUser.getIsAdmin();

        // [디버깅 로그] isAdmin 변수의 실제 값을 로그로 출력
        System.out.println("===== DEBUGGING ADMIN CHECK =====");
        System.out.println("User ID: " + finalLoginUser.getUserId());
        System.out.println("isAdmin value from DB: [" + isAdmin + "]"); // 대괄호로 감싸서 공백까지 확인
        System.out.println("Is Admin Check Result: " + (isAdmin != null && "Y".equalsIgnoreCase(isAdmin.trim())));
        System.out.println("===============================");

        String targetUrl = "/";
        String beforeURL = (String) session.getAttribute("beforeURL");

        if (beforeURL != null && !beforeURL.contains("/signIn")) {
            targetUrl = beforeURL;
        } else if (isAdmin != null && "Y".equalsIgnoreCase(isAdmin.trim())) {
            targetUrl = "/admin/dashboard";
        }

        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}