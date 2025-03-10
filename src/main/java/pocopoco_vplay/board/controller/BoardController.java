package pocopoco_vplay.board.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.exception.BoardException;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.commom.Pagination;
import pocopoco_vplay.commom.model.vo.PageInfo;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/board/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class BoardController {
	private final BoardService bService;

	@GetMapping("all_menu")
	public ModelAndView joinVideoTemplatesList(ModelAndView mv, HttpSession session) {
		String[] menuName = { "video Templates", "Music", "Sound Effects", "Graphic Templates", "Stock Video", "Photos", "Fonts" };

		ArrayList<Content> videoTemplateList = bService.allTemplateList(menuName[0]);
		ArrayList<Content> musicList = bService.allTemplateList(menuName[1]);
		ArrayList<Content> soundEffectsList = bService.allTemplateList(menuName[2]);
		ArrayList<Content> graphicTemplateList = bService.allTemplateList(menuName[3]);
		ArrayList<Content> stockVideoList = bService.allTemplateList(menuName[4]);
		ArrayList<Content> photosList = bService.allTemplateList(menuName[5]);
		ArrayList<Content> fontList = bService.allTemplateList(menuName[6]);

//		for(int i =0; i<menuName.length ; i++) {
//			for(int j =0; j<videoTemplateList.size(); j++) {
//
//			}
//		}
		Users u = (Users) session.getAttribute("loginUser");
		int userNo = 0;

		if (u != null) {
			userNo = u.getUserNo();
		}

		int num = 0;

		int result = 0;

		for (int v = 0; v < videoTemplateList.size(); v++) {
			num = videoTemplateList.get(v).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			videoTemplateList.get(v).setLikeTo(result);
		}

		for (int m = 0; m < musicList.size(); m++) {
			num = musicList.get(m).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			musicList.get(m).setLikeTo(result);
		}

		for (int s = 0; s < soundEffectsList.size(); s++) {
			num = soundEffectsList.get(s).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			soundEffectsList.get(s).setLikeTo(result);
		}

		for (int g = 0; g < graphicTemplateList.size(); g++) {
			num = graphicTemplateList.get(g).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			graphicTemplateList.get(g).setLikeTo(result);
		}

		for (int s = 0; s < stockVideoList.size(); s++) {
			num = stockVideoList.get(s).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			stockVideoList.get(s).setLikeTo(result);
		}

		for (int p = 0; p < photosList.size(); p++) {
			num = photosList.get(p).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			photosList.get(p).setLikeTo(result);
		}

		for (int f = 0; f < fontList.size(); f++) {
			num = fontList.get(f).getContentNo();
			result = bService.menuLikeTo(num, userNo);

			fontList.get(f).setLikeTo(result);

		}

		mv.addObject("videoTemplateList", videoTemplateList).addObject("musicList", musicList).addObject("soundEffectsList", soundEffectsList).addObject("graphicTemplateList", graphicTemplateList);
		mv.addObject("stockVideoList", stockVideoList).addObject("photosList", photosList).addObject("fontList", fontList);

		mv.setViewName("all_menu");

		return mv;
	}

	@GetMapping("selectCategory")
	@ResponseBody
	public ArrayList<HashMap<String, Object>> selectCategory(@RequestParam("value") String menu, @RequestParam("sortValue") String sort, Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser == null)
			throw new UsersException("로그인하삼");

		int userNo = loginUser.getUserNo();
		ArrayList<HashMap<String, Object>> list = bService.selectCategory(menu, userNo, sort);
		System.out.println(list);
		return list;
	}

	@GetMapping("selectCategoryMyProjects")
	@ResponseBody
	public ArrayList<Content> selectCategoryMyProjects(@RequestParam("value") String menu, HttpSession session, @RequestParam("sortValue") String sort) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		int userNo = loginUser.getUserNo();
		ArrayList<Content> list = bService.selectCategoryMyProjects(menu, userNo, sort);
		System.out.println("리스트는 " + list);
		if (list != null) {
			return list;
		} else {
			throw new UsersException("로그인 하셈");
		}
	}

	@PostMapping("throwBoardTrash")
	@ResponseBody
	public int throwBoardTrash(@RequestParam("contentNo") int contentNo) {
		System.out.println(contentNo);
		int result = bService.throwBoardTrash(contentNo);
		if (result > 0) {
			return result;
		} else {
			throw new UsersException("ㅋㅋ");
		}
	}

	@GetMapping("inquiryDetail")
	public String selectInquiry(@RequestParam("contentNo") int contentNo, Model model) {
		Content inquiry = bService.selectInquiry(contentNo);
		Reply reply = bService.selectReply(contentNo);
		if (inquiry != null) {
			model.addAttribute("inquiry", inquiry);
			model.addAttribute("reply", reply);
			return "inquiry_detail";
		} else {
			throw new UsersException("문의 불러오기 실패.");
		}
	}

	@GetMapping("inquiry_writer")
	public String inquiryWriter() {
		return "inquiry_writer";
	}

	@PostMapping("write_inquiry")
	public String writeInquiry(@RequestParam("userNo") int userNo, @RequestParam("menuNo") int menuNo, @RequestParam("contentTitle") String contentTitle, @RequestParam("contentDetail") String contentDetail) {
		Content inquiry = new Content();
		inquiry.setUserNo(userNo);
		inquiry.setMenuNo(menuNo);
		inquiry.setContentTitle(contentTitle);
		inquiry.setContentDetail(contentDetail);
		int result = bService.insertInquiry(inquiry);
		if (result > 0) {
			return "redirect:/users/my_inquiry";
		} else {
			throw new UsersException("문의 작성 실패");
		}
	}

	@GetMapping("inquiryUpdate")
	public String inquiryUpdateView(@RequestParam("contentNo") int contentNo, Model model) {
		Content inquiry = bService.selectInquiry(contentNo);
		if (inquiry != null) {
			model.addAttribute("inquiry", inquiry);
			return "inquiry_update";
		} else {
			throw new UsersException("문의 불러오기 실패.");
		}
	}

	@PostMapping("update_inquiry")
	public String updateInquiry(@RequestParam("contentNo") int contentNo, @RequestParam("menuNo") int menuNo, @RequestParam("contentTitle") String contentTitle, @RequestParam("contentDetail") String contentDetail) {
		Content inquiry = new Content();
		inquiry.setContentNo(contentNo);
		inquiry.setMenuNo(menuNo);
		inquiry.setContentTitle(contentTitle);
		inquiry.setContentDetail(contentDetail);
		int result = bService.updateInquiry(inquiry);
		if (result > 0) {
			return "redirect:/users/my_inquiry";
		} else {
			throw new UsersException("문의 수정 실패");
		}
	}

	@GetMapping("selectCategoryMyTrash")
	@ResponseBody
	public ArrayList<Content> selectCategoryMyTrash(@RequestParam("value") String menu, HttpSession session, @RequestParam("sortValue") String sort) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		System.out.println(sort);
		System.out.println(menu);
		int userNo = loginUser.getUserNo();
		ArrayList<Content> list = bService.selectCategoryMyTrash(menu, userNo, sort);
		System.out.println("리스트는 " + list);
		if (list != null) {
			return list;
		} else {
			throw new UsersException("로그인 하셈");
		}
	}

//	@GetMapping("request_list")
//	public String requestList() {
//		return "request_list";
//	}

	@GetMapping("video-template-list")
	public ModelAndView videoTemplateList(ModelAndView mv) {
		ArrayList<Content> videoTemplateList = bService.allTemplateList("video Templates");
		ArrayList<Content> videoTempCategory = bService.allCategory(1);
		ArrayList<Content> videoTempPopularCate = bService.allPopularCate(1);

		mv.addObject("videoTemplateList", videoTemplateList).addObject("videoTempCategory", videoTempCategory).addObject("videoTempPopularCate", videoTempPopularCate);
		mv.setViewName("videoTemplates_list");
		return mv;
	}

	@GetMapping("sound-effects-list")
	public ModelAndView soundEffectsList(ModelAndView mv) {
		ArrayList<Content> soundEffectsTemplateList = bService.allTemplateList("Sound Effects");

		mv.addObject("soundEffectsTemplateList", soundEffectsTemplateList);
		mv.setViewName("soundEffects_list");
		return mv;
	}

	@GetMapping("music-list")
	public ModelAndView musicList(ModelAndView mv) {
		ArrayList<Content> musicList = bService.allTemplateList("Music");

		mv.addObject("musicList", musicList);
		mv.setViewName("music_list");
		return mv;
	}

	@GetMapping("graphic-template-list")
	public ModelAndView graphicTemplateList(ModelAndView mv) {
		ArrayList<Content> graphicTemplateList = bService.allTemplateList("Graphic Templates");

		mv.addObject("graphicTemplateList", graphicTemplateList);
		mv.setViewName("GraphicTemplates_list");
		return mv;
	}

	@GetMapping("stock-video-list")
	public ModelAndView stockVideoList(ModelAndView mv) {
		ArrayList<Content> stockVideoList = bService.allTemplateList("Stock Video");

		mv.addObject("stockVideoList", stockVideoList);
		mv.setViewName("stock-video_list");
		return mv;
	}

	@GetMapping("photo-list")
	public ModelAndView photoList(ModelAndView mv) {
		ArrayList<Content> photosList = bService.allTemplateList("Photos");

		mv.addObject("photosList", photosList);
		mv.setViewName("photo-list");
		return mv;
	}

	@GetMapping("font-list")
	public ModelAndView fontList(ModelAndView mv) {
		ArrayList<Content> fontList = bService.allTemplateList("Fonts");

		mv.addObject("fontList", fontList);
		mv.setViewName("fonts_list");
		return mv;
	}

	@GetMapping("request_list")
	public ModelAndView joinrequestPost(@RequestParam(value = "page", defaultValue = "1") int currentPage, ModelAndView mv) {

		int listCount = bService.getrequestPostCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Content> list = bService.selectAllRequestPost(pi);

		System.out.println("리스트 개수: " + list.size());

		for (Content c : list) {
			c.setUserId(bService.selectUser(c.getUserNo()));
		}

		mv.addObject("list", list).addObject("pi", pi);
		mv.setViewName("request_list");
		return mv;
	}

	@GetMapping("writeRequest")
	public String writeRequest() {
		return "request_write";
	}

	@PostMapping("writeRequest")
	public String writeRequest(@ModelAttribute Content content, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		int userNo = loginUser.getUserNo();
		content.setUserNo(userNo);
		System.out.println(content);
		int result = bService.insertRequest(content);
		int result2 = bService.insertRequestBoard(content);
		if (result + result2 == 2) {
			return "redirect:/board/request_list";
		} else {
			throw new BoardException("제작 의뢰 게시글 작성 실패");
		}
	}

	@GetMapping("/{id:\\d+}/{page:\\d+}")
	public ModelAndView show(@PathVariable("id") int bId, @PathVariable("page") int page, HttpSession session, ModelAndView mv) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		int id = 0;
		if (loginUser != null) {
			id = loginUser.getUserNo();
			System.out.println(loginUser.getUserNickname());
			mv.addObject("userNickname", loginUser.getUserNickname());
		}
		Content c = bService.selectRequest(bId, id);
		ArrayList<Reply> replyList = bService.selectReplyList(bId);/* 글 번호에 해당하는 댓글을 들고와야하기 때문 */

		//System.out.println(replyList);

		if (c != null) {
			mv.addObject("replyList", replyList);
			mv.addObject("c", c);
			mv.addObject("page", page).setViewName("request_detail");
			return mv;
		} else {
			throw new BoardException("의뢰 게시글 상세조회를 실패했습니다.");
		}
	}

	@GetMapping("/{menuName:[a-zA-Z-]+}/{no:\\d+}")
	public String videoTempDetail(@PathVariable("menuName") String menuName, @PathVariable("no") int contentNo, Model model) {

		Content content = bService.allMenuDetail(contentNo);
		model.addAttribute("content", content);

		String joinURL = null;

		switch (menuName) {
		case "video-templates":
			joinURL = "videoTemplates_detail";
			break;
		case "music":
			joinURL = "music_detail";
			break;
		case "sound-effect":
			joinURL = "soundEffects_detail";
			break;
		case "graphic-templates":
			joinURL = "graphicTemplates_detail";
			break;
		case "stock-video":
			joinURL = "stock-video_detail";
			break;
		case "photo":
			joinURL = "photo_detail";
			break;
		default:
			joinURL = "font_detail";
			break;
		}

		System.out.println(menuName);

		return joinURL;
	}

	@PostMapping("updateRequestForm")
	public String updateRequestForm(@RequestParam("contentNo") int bId, @RequestParam("page") int page, HttpSession session, Model model) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		int id = 0;
		if (loginUser != null) {
			id = loginUser.getUserNo();
		}
		Content content = bService.selectRequest(bId, id);
		model.addAttribute("c", content).addAttribute("page", page);
		return "edit_request";
	}

	@PostMapping("updateRequest")
	public String updateRequest(@ModelAttribute Content c, @RequestParam("page") int page) {
		int result1 = bService.updateRequest(c);
		int result2 = bService.updateRequestMenu(c);
		System.out.println(c);
		if (result1 + result2 == 2) {
			// return "redirect:/board/"+c.getContentNo()+ "/"+ page;
			return String.format("redirect:/board/%d/%d", c.getContentNo(), page);
		} else {
			throw new BoardException("게시글 수정을 실패하였습니다.");
		}
	}

	@PostMapping("deleteRequest")
	public String deleteRequest(@RequestParam("contentNo") int bId) {
		int result = bService.deleteBoard(bId);
		if (result > 0) {
			return "redirect:/board/list";
		} else {
			if (result > 0) {
				return "redirect:/board/request_list";
			} else {
				throw new BoardException("게시글 삭제를 실패했습니다.");
			}
		}
	}

	@PostMapping("insertReply")
	public String insertReply(@RequestParam(value = "page", defaultValue = "1") int currentPage, @ModelAttribute Reply reply, HttpSession session, Model model, @RequestParam("contentNo") String contentNo) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if(loginUser != null) {
			reply.setWriter(loginUser.getUserNickname());
			reply.setContentNo(contentNo);
		}

		int result = bService.insertReply(reply);

		if(result > 0){
			ArrayList<Reply> replyList = bService.selectReplyList(Integer.parseInt(contentNo));
			model.addAttribute("replyList", replyList);
			return "redirect:/board/" + contentNo + "/" +currentPage;

		}else{
			throw new BoardException("댓글 등록에 실패하였습니다.");
		}

	}

}