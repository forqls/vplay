package pocopoco_vplay.ajax.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.vo.Users;

@RestController
@RequestMapping("{/board/, /users/}")
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
	
	@GetMapping("{menuName:[a-zA-Z-]+}/{categoryTagName:[a-zA-Z\\+\\s&]+}")
	public ArrayList<Content> selectCategory(@PathVariable("menuName") String menuName, @PathVariable("categoryTagName") String categoryTagName){
		System.out.println(categoryTagName);
		System.out.println("수정 전 : " + menuName);
		
		HashMap<String, Object> map = new HashMap<>();
		String[] result = {};
		
		switch(menuName) {
		case "video-template-list": menuName = "video Templates"; break;
		case "sound-effects-list": menuName = "Sound Effects"; break;
		case "music-list": menuName = "Music"; break;
		case "graphic-template-list": menuName = "Graphic Templates"; break;
		case "stock-video-list": menuName = "Stock Video"; break;
		case "photo-list": menuName = "Photos"; break;
		case "font-list": menuName = "Fonts";
		}
		
		System.out.println("수정 후 : " + menuName);
		
		map.put("menuName", menuName);
		
		if(!categoryTagName.equals("empty")) {
			if(categoryTagName.contains("+")) {
				System.out.println("문자열에 +가 포함");
				result = categoryTagName.split("\\+");
			}else {
				System.out.println("문자열에 +가 안포함");
				result = new String[] {categoryTagName};
			}
			
			map.put("categoryArray", result);
		}else {
			System.out.println("empty 맞음");
		}

		
		
		ArrayList<Content> cList = bService.allTemplateList(map);
		
		for(Content c : cList) {
			System.out.println(c);
		}
		
		return cList;
	}
	
	@PatchMapping("profile")
	public int updateProfile(@RequestParam("profile") MultipartFile file) {
		return 0;
	}
		
}
