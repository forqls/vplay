package pocopoco_vplay.ajax.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.vo.Users;

@RestController
@RequestMapping("/board/")
@SessionAttributes("loginUser")
@RequiredArgsConstructor
public class AjaxController {
	
	private final BoardService bService;
	
	@PutMapping("like")
	public int likeAllTemp(@RequestBody HashMap<String, Integer> map, HttpSession session){
		System.out.println(map);

		int userNo = ((Users)session.getAttribute("loginUser")).getUserNo();
		map.put("userNo", userNo);
		
		System.out.println(map);
		
		
		return bService.allTempLike(map);
	}
	
	@DeleteMapping("like")
	public int unLikeAllTemp(@RequestBody HashMap<String, Integer> map, HttpSession session) {
		int userNo = ((Users)session.getAttribute("loginUser")).getUserNo();
		map.put("userNo", userNo);
		
		return bService.unAllTempLike(map);
	}
	
	@GetMapping("category")
	public ArrayList<Content> selectCategory(@RequestBody String[] checkboxVal){
		System.out.println(checkboxVal);
		ArrayList<Content> list = null;
		return list;
	}
		
}
