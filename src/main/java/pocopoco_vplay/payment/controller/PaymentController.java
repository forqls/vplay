package pocopoco_vplay.payment.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.payment.model.service.PaymentService;
import pocopoco_vplay.payment.model.vo.Payment;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/payment/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class PaymentController {
	
	private final PaymentService pService;
	private final UsersService uService;
	
	@PostMapping("success")
	public ResponseEntity<String> paymentSuccess(@RequestBody Payment payment , HttpSession session , HttpServletRequest request){
		
		payment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now().withNano(0)));
		Users loginUser = (Users)session.getAttribute("loginUser");
		payment.setUserNo(loginUser.getUserNo());
		
		int result2 = 0;
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userNo", loginUser.getUserNo());
		
		if(payment.getAmount() == 26000) {
			map.put("userPlan", "All Rounder");
			result2 = uService.updateUserPlan(map);
		}else if(payment.getAmount() == 16500) {
			map.put("userPlan", "Frame Pro");
			result2 = uService.updateUserPlan(map);
		}else if(payment.getAmount() == 100) {
			map.put("userPlan", "Graphic Pro");
			result2 = uService.updateUserPlan(map);
		}else {
			System.out.println("값이 잘못 됨 ㅇㅇ@@@@@@@@@@@@@@@@@@@@@@@@@@");
		}
		
//		System.out.println(result2);
//		System.out.println(payment);
		int result = pService.insertPaymentInfo(payment);

		if(result > 0) {
			
			uService.resetAlertShown(loginUser.getUserNo());
			
			loginUser.setUserPlan((String) map.get("userPlan"));
	        session.setAttribute("loginUser", loginUser);

			return ResponseEntity.ok("결제 정보 저장 성공");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 정보 저장 실패");
		}
		
	}
}
