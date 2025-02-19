package pocopoco_vplay.commom.intercepter;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import pocopoco_vplay.users.model.vo.Users;

//로그인 검사
public class CheckLoginIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Users loginUser = (Users) session.getAttribute("loginUser");
        if(loginUser == null){
            String url = request.getRequestURI();
            String msg="로그인 세션이 만료되어 로그인 화면으로 넘어갑니다.";

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<script>alert('"+msg+"'); location.href='/users/signIn';</script>");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
