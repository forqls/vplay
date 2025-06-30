package pocopoco_vplay.home.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class HomeController {
	
	private final UsersService uService;
	private final BoardService bService;

	
	@GetMapping("/")
	public String goIndex(Model model, HttpSession session) {
		
		ArrayList<Users> user = uService.selectTopUser();
		Users loginUser = (Users) session.getAttribute("loginUser");
		int userNo = (loginUser != null) ? loginUser.getUserNo() : 0;

		for (Users u1 : user) {
			int createrNo = u1.getUserNo();
			int isSubscribed = uService.isSubscribed(createrNo, userNo);
			u1.setIsSubscribed(isSubscribed);  // 모델 add는 안해도 되면 생략
		}

		ArrayList<Content> mdList = bService.selectMdList();
		

		System.out.println(user);

		int num = 0;
		int result = 0;
		for (int v = 0; v < mdList.size(); v++) {
			num = mdList.get(v).getContentNo();
			result = bService.menuLikeTo(num, userNo);
			mdList.get(v).setLikeTo(result);
		}
		
		ArrayList<Content> content = bService.selectContentTop();
		for(int v =0; v< content.size();v++) {
			num = content.get(v).getContentNo();
			result = bService.menuLikeTo(num, userNo);
			content.get(v).setLikeTo(result);
		}
//		System.out.println("컨텐츠 보드의 조회수 뷰 12개 보드 : " + content + content.size() + "\n");
		int flag = 0;
		
		//System.out.println("select :" + content);
		
		for(Content c : content) {
			c.setOverallScore((c.getLikeCount()*70 + c.getViews()*30)/100);
		}
		
	    Collections.sort(content, Comparator.comparingInt(Content::getOverallScore).reversed());
	    for(Content c : content) {
	    	System.out.println(c.getOverallScore());
	    }
//	    System.out.println(content);
		
		model.addAttribute("clist",content).addAttribute("mdList", mdList).addAttribute("ulist",user);;
		return "index";
	}
}
