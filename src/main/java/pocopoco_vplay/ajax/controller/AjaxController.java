package pocopoco_vplay.ajax.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.exception.BoardException;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Files;
import pocopoco_vplay.cloudflare.model.service.R2Service;
import pocopoco_vplay.users.exception.UsersException;
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
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser == null) {
			// 로그인 안했으면 -1 리턴 (프론트에서 "로그인 필요" 처리)
			return -1;
		}

		int userNo = loginUser.getUserNo();
		map.put("userNo", userNo);
		return bService.allTempLike(map);
	}

	@DeleteMapping("like")
	public int unLikeAllTemp(@RequestBody HashMap<String, Integer> map, HttpSession session) {
		int userNo = ((Users)session.getAttribute("loginUser")).getUserNo();
		map.put("userNo", userNo);
		
		return bService.unAllTempLike(map);
	}
	@Value("${CLOUDFLARE_R2_PUBLIC_URL}")
	private String r2PublicUrl;

	// AjaxController.java의 selectThumbnail 메소드를 이렇게 수정해줘

	// AjaxController.java의 selectThumbnail 메소드

	@GetMapping("/select-thumbnail/{contentNo:[0-9]+}")
	public HashMap<String, String> selectThumbnail(@PathVariable("contentNo") int contentNo) {
		HashMap<String, String> map = new HashMap<>();
		String thumbnailLocation = null;

		ArrayList<Files> fileList = bService.selectFiles(contentNo);

		if (fileList != null && !fileList.isEmpty()) {
			for (Files f : fileList) {
				if (f.getFileLevel() == 1) {
					thumbnailLocation = f.getFileLocation();
					break;
				}
			}
		}

		String fullUrl = "";
		if (thumbnailLocation != null) {
			fullUrl = thumbnailLocation.replace("https:--", "https://");
		}

		map.put("thumbnail", fullUrl);
		return map;
	}
	
	@PostMapping("{menuName:[a-zA-Z-]+}")
	public ArrayList<Content> selectCategoryEmpty(@PathVariable("menuName") String menuName){
		System.out.println("수정 전 : " + menuName);
		
		HashMap<String, Object> map = new HashMap<>();
		
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
		
		ArrayList<Content> cList = bService.allTemplateList(map);
		
		for(Content c : cList) {
			System.out.println(c);
		}
		
		return cList;
	}
	
	@PostMapping("{menuName:[a-zA-Z-]+}/{categoryTagName:[a-zA-Z가-힣0-9\\+&-]+}")
	public ArrayList<Content> selectCategory(@PathVariable("menuName") String menuName, @PathVariable("categoryTagName") String categoryTagName, HttpSession session){
		Users loginUser = (Users) session.getAttribute("loginUser");
		int userNo = 0;
		if(loginUser != null) {
			 userNo = loginUser.getUserNo();
		}
		
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
		for (int v = 0; v < cList.size(); v++) {
			int num = cList.get(v).getContentNo();
			int result2 = bService.menuLikeTo(num, userNo);
			cList.get(v).setLikeTo(result2);
		}
		
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
			
			System.out.println(file);
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
			System.out.println(loginUser.toString());
			System.out.println(map);
			
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

		if ("Y".equals(map.get("column")) && currentMdCount >= 6) {
			return -1;
		}

		int result = bService.updateRecommendation(map);

		if(result > 0) {
			return 1;
		}else{
			throw new BoardException("상태값 업데이트 중 오류 발생 컨트롤러를 보세용");
		}
	}
	
	@GetMapping("download/{fileName}/{contentNo}/{userNo}")
	public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable("fileName") String fileName,@PathVariable("contentNo") int contentNo,@PathVariable("userNo") int userNo) {


		System.out.println(fileName);
		
		InputStream fileStream = r2Service.downloadFileStream(fileName);
		
		if(fileStream == null) {
			throw new BoardException("파일 다운로드 중 오류 발생");
		}
		
		int count = bService.checkDownload(contentNo, userNo);
		if(count == 0) {
			bService.downloadRecord(contentNo, userNo);
		}
		
		StreamingResponseBody responseBody = outputStream -> {
	        byte[] buffer = new byte[8192];
	        int bytesRead;
	        while ((bytesRead = fileStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        fileStream.close();
	    };
		
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(responseBody);
	}
	
	@PostMapping("post/updateSubscribe")
	public int updateSubscribe(@RequestBody HashMap<String,Object> map , HttpSession session) {
		System.out.println("dddd");
		int userNo = ((Users)session.getAttribute("loginUser")).getUserNo();
		int createrNo = Integer.parseInt(map.get("createrNo").toString());
		boolean isCancel = (Boolean)map.get("isCancel");
		
//		System.out.println("여기 전부 다 있어요 " + userNo + createrNo + isCancel);
		map.put("userNo", userNo);
		
		int result = uService.updateSubscribe(map);
		System.out.println(map);
		if(result == 1 ) {
			return result;
		}else {
			throw new UsersException("ㅋㅋ");
		}
		
	}
	
	
	
	
	
	
	
	
	

}
