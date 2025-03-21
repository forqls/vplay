package pocopoco_vplay.ajax.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.exception.BoardException;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.cloudflare.model.service.R2Service;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@RestController
@RequestMapping({"/board", "/users", "/admin"})
@SessionAttributes("loginUser")
@RequiredArgsConstructor
public class AjaxController {
	
	private final BoardService bService;
	private final UsersService uService;
	private final R2Service r2Service;
	
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
	
	@PostMapping("{menuName:[a-zA-Z-]+}/{categoryTagName:[a-zA-Z가-힣0-9\\+&-]+}")
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
		
		if(menuName.equals("video Templates") || menuName.equals("Sound Effects") || menuName.equals("Music") || menuName.equals("Graphic Templates") ||
		   menuName.equals("Stock Video") || menuName.equals("Photos") || menuName.equals("Fonts")) {
			System.out.println("menuName 들어옴");
			map.put("menuName", menuName);
		}else {
			System.out.println("search 들어옴");
			map.put("search", menuName);
		}
			
		
		categoryTagName = categoryTagName.replace("-", " ");
		System.out.println(categoryTagName);
		
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
	public int updateProfile(@RequestParam(value = "profile", required = false) MultipartFile file, HttpSession session, @Value("${cloudflare.r2.public-url}") String publicUrl) {
		Users loginUser = (Users)session.getAttribute("loginUser");
		if(loginUser != null) {
			HashMap<String,String> map = new HashMap<String, String>();
			
			String fileUrl = null;
			
			System.out.println("여기까지 옴 ㅋㅋ");
			
			
			try {
				if(file != null) {
					fileUrl = r2Service.uploadFile(file);
					System.out.println(fileUrl);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			System.out.println(fileUrl);
			map.put("userId", loginUser.getUserId());
			
			String originalProfile = uService.selectProfile(map);
			System.out.println(originalProfile);
			if(originalProfile != null) {
				String toRemove = publicUrl + "/";
				String removeProfile = originalProfile.replace(toRemove, "");

				r2Service.deleteFile(removeProfile);
			}
			
			
			map.put("userProfile", fileUrl);
			int result = uService.updateProfile(map);
			if(result > 0) {
				loginUser.setUserProfile(fileUrl);
				session.setAttribute("loginUser", loginUser);
			}
			return result;
		}else {
			return 0;
		}
		
	}

	@GetMapping("writeContent/{menuNo}")
	public ArrayList<Content> menuCategoryList(@PathVariable("menuNo") int menuNo) {
        System.out.println(menuNo);

        ArrayList<Content> list = bService.menuCategoryList(menuNo);

        System.out.println(list);
        return list;
    }

	@PutMapping("mdRecommendation")
	public int updateRecommendation(@RequestBody HashMap<String, String> map) {
		System.out.println("contentNo: " + map.get("contentNo"));
		System.out.println("column 값: " + map.get("column"));

		int currentMdCount = bService.getMdRecommendationCount();

		if ("Y".equals(map.get("column")) && currentMdCount >= 8) {
			return -1;
		}

		int result = bService.updateRecommendation(map);

		if(result > 0) {
			return 1;
		}else{
			throw new BoardException("상태값 업데이트 중 오류 발생 컨트롤러를 보세용");
		}
	}

	@GetMapping("mdList")
	public ArrayList<Content>  selectmdList() {
		ArrayList<Content> list = bService.selectMdList();
		System.out.println("md추천리스트들 : " + list);
		return list;
	}

}
