package pocopoco_vplay.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class AdminController {
	
	@GetMapping("dashboard")
	public String joinDashboard(){
		return "dashboard";
	}
	
	@GetMapping("users")
	public String joinUsers() {
		return "users_management";
	}
	
	@GetMapping("inquiry")
	public String joinInquiry() {
		return "Inquiry_management";
	}
	
	@GetMapping("templates")
	public String joinTemplates() {
		return "managing_templates";
	}
	
	@GetMapping("requestPost")
	public String joinrequestPost() {
		return "Managing_request_posts";
	}
	
	
}
