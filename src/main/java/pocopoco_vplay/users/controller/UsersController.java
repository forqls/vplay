package pocopoco_vplay.users.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.users.model.service.UsersService;

@Controller
@RequestMapping("/users/")
@RequiredArgsConstructor
public class UsersController {
	private final UsersService uService;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("singUp")
	public String singUp() {
		return "signup";
	}
	
	@PostMapping("idCheck")
	public int checkId(@RequestParam("id") String id) {
		System.out.println("userId 는 " + id);
		return uService.checkId(id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
