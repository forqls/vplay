package pocopoco_vplay.board.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/board/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class BoardController {
	private final BoardService bService;
	
	@GetMapping("all_menu")
	public String joinVideoTemplatesList() {
		return "all_menu";
	}
	
	@GetMapping("selectCategory")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> selectCategory(@RequestParam("value") String menu ,@RequestParam("sortValue") String sort, Model model , HttpSession session) {
		Users loginUser = (Users)session.getAttribute("loginUser");
//		System.out.println("메뉴 : "+menu);
//		System.out.println("정렬 : "+sort);
		if(loginUser == null) throw new UsersException("로그인하삼");
		
		int userNo = loginUser.getUserNo(); 
		ArrayList<HashMap<String,Object>> list =bService.selectCategory(menu,userNo,sort);
		System.out.println(list);
		return list;
	}
	
	@GetMapping("selectCategoryMyProjects")
	@ResponseBody
	public ArrayList<Content> selectCategoryMyProjects(@RequestParam("value") String menu,HttpSession session , @RequestParam("sortValue") String sort){
		Users loginUser = (Users)session.getAttribute("loginUser");
//		System.out.println(sort);
//		System.out.println(menu);
		int userNo = loginUser.getUserNo();
		ArrayList<Content> list = bService.selectCategoryMyProjects(menu,userNo,sort);
		System.out.println("리스트는 "  +list);
		if(list != null) {
			return list;
		}else {
			throw new UsersException("로그인 하셈");
		}
	}
	
	@PostMapping("throwBoardTrash")
	@ResponseBody
	public int throwBoardTrash(@RequestParam("contentNo") int contentNo) {
		System.out.println(contentNo);
		int result = bService.throwBoardTrash(contentNo);
		if(result >0) {
			return result;
		}else {
			throw new UsersException("ㅋㅋ");
		}
	}
	
    @GetMapping("/inquiry/inquiry_writer")
    public String inquiryWriter() {
        return "views/board/inquiry/inquiry_writer";
    }
	
	
	
	
	
	
	
	
	
	
}
