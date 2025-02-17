package pocopoco_vplay.admin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.admin.model.service.AdminService;
import pocopoco_vplay.admin.model.vo.PageInfo;
import pocopoco_vplay.commom.Pagination;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class AdminController {
	
	private final AdminService aService;
	
	@GetMapping("dashboard")
	public String joinDashboard(){
		return "dashboard";
	}
	
	@GetMapping("users")
	public ModelAndView joinUsers(@RequestParam(value="page", defaultValue="1") int currentPage, ModelAndView mv) {
		
		int listCount = aService.getUsersCount();
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		
		ArrayList<Users> list = aService.selectAllUser(pi);
		
		SimpleDateFormat formmatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(!list.isEmpty()) {
			for(Users u : list) {
//				String 
			}
		}
		
		mv.addObject("list", list).setViewName("users_management");
		return mv;
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
