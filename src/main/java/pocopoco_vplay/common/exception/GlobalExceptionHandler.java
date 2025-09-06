package pocopoco_vplay.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        e.printStackTrace(); // 로그에 Root Cause 출력
        model.addAttribute("errorMessage", e.getMessage());
        return "error/500"; // templates/error/500.html
    }
}

