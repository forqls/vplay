package pocopoco_vplay.home.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final UsersService uService;
	private final BoardService bService;
	
	@GetMapping("/")
	public String goIndex(Model model) {
		ArrayList<Users> user = uService.selectTopUser();
		System.out.println(user);
		model.addAttribute("ulist",user);
//		ArrayList<Content> content = bService.selectOrderByViews();
		
		return "index";
	}
}
