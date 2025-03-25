package pocopoco_vplay.home.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		ArrayList<Content> mdList = bService.selectMdList();
		System.out.println(user);
		
		model.addAttribute("ulist",user);
		ArrayList<Content> content = bService.selectContentTop();
//		System.out.println("컨텐츠 보드의 조회수 뷰 12개 보드 : " + content + content.size() + "\n");
		int flag = 0;
		
		for(Content c : content) {
			c.setOverallScore((c.getLikeCount()*70 + c.getViews()*30)/100);
		}
		
	    Collections.sort(content, Comparator.comparingInt(Content::getOverallScore).reversed());
	    for(Content c : content) {
	    	System.out.println(c.getOverallScore());
	    }
	    System.out.println(content);
		
		model.addAttribute("clist",content).addAttribute("mdList", mdList);
		return "index";
	}
}
