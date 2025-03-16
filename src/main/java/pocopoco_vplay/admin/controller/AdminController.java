package pocopoco_vplay.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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
import pocopoco_vplay.admin.model.service.AdminService;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.commom.Pagination;
import pocopoco_vplay.commom.model.vo.PageInfo;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class AdminController {
	private final AdminService aService;
	private final BoardService bService;

	@GetMapping("dashboard")
	public ModelAndView joinDashboard(ModelAndView mv) {
		int userCount = aService.getUsersCount();
		int templatesCount = aService.getTemplatesCount(null);
		mv.addObject("userCount", userCount).addObject("templatesCount", templatesCount);
		mv.setViewName("dashboard");
		return mv;
	}

	@GetMapping(value = "drawChart", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String drawChart() {
		int countVedio = 0;
		int countMusic = 0;
		int countSound = 0;
		int countGraphic = 0;
		int countStockV = 0;
		int countPhoto = 0;
		int countFont = 0;
		for (int i = 1; i <= 7; i++) {
			int result = aService.countMenuTemp(i);

			switch (i) {
			case 1:
				countVedio = result;
				break;
			case 2:
				countMusic = result;
				break;
			case 3:
				countSound = result;
				break;
			case 4:
				countGraphic = result;
				break;
			case 5:
				countStockV = result;
				break;
			case 6:
				countPhoto = result;
				break;
			case 7:
				countFont = result;
			}
		}
		List<JSONObject> dataList = new ArrayList<>();
		dataList.add(new JSONObject().put("menu", "Video Templates").put("count", countVedio));
		dataList.add(new JSONObject().put("menu", "Music").put("count", countMusic));
		dataList.add(new JSONObject().put("menu", "Sound Effects").put("count", countSound));
		dataList.add(new JSONObject().put("menu", "Graphic Templates").put("count", countGraphic));
		dataList.add(new JSONObject().put("menu", "Stock Video").put("count", countStockV));
		dataList.add(new JSONObject().put("menu", "Photos").put("count", countPhoto));
		dataList.add(new JSONObject().put("menu", "Fonts").put("count", countFont));
		JSONArray array = new JSONArray(dataList);
		return array.toString();
	}

	@GetMapping("users")
	public ModelAndView joinUsers(@RequestParam(value = "page", defaultValue = "1") int currentPage, ModelAndView mv) {
		int listCount = aService.getUsersCount();
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Users> list = aService.selectAllUser(pi);
		for (Users u : list) {
			System.out.println(u.getJoinDate());
		}
		mv.addObject("list", list).addObject("pi", pi).setViewName("management_users");
		return mv;
	}

	@GetMapping("inquiry")
	public ModelAndView joinInquiry(@RequestParam(value = "page", defaultValue = "1") int currentPage, ModelAndView mv) {
		int listCount = aService.getInquiryCount(null);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Content> list = aService.selectAllInquiry(null, pi);
		for (Content c : list) {
			c.setUserId(aService.selectUser(c.getUserNo()));
			c.setReply(bService.selectReply(c.getContentNo()));
		}
		mv.addObject("list", list).addObject("pi", pi);
		mv.setViewName("management_inquiry");
		return mv;
	}

	@GetMapping("inquiry/{menuName}")
	public String filterInquiry(@RequestParam(value = "page", defaultValue = "1") int currentPage, @PathVariable("menuName") String menuName, Model model) {
		System.out.println(menuName);
		Content content = new Content();

		switch (menuName) {
		case "system":
			content.setMenuNo(11);
			break;
		case "pay":
			content.setMenuNo(12);
			break;
		case "content":
			content.setMenuNo(13);
			break;
		case "other":
			content.setMenuNo(14);
			break;
		}
		int listCount = aService.getInquiryCount(content);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Content> list = aService.selectAllInquiry(content, pi);
		model.addAttribute("list", list).addAttribute("pi", pi).addAttribute("menuName", menuName);
		return "management_inquiry";
	}

	@GetMapping("templates")
	public ModelAndView joinTemplates(@RequestParam(value = "page", defaultValue = "1") int currentPage, ModelAndView mv) {
		int listCount = aService.getTemplatesCount(null);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Content> list = aService.selectAllTemplates(null, pi);
		for (Content c : list) {
			System.out.println(c);
			c.setUserId(aService.selectUser(c.getUserNo()));
		}
		mv.addObject("list", list).addObject("pi", pi);
		mv.setViewName("management_templates");
		return mv;
	}

	@GetMapping("templates/{menuName}")
	public String filterTemplates(@RequestParam(value = "page", defaultValue = "1") int currentPage, @PathVariable("menuName") String menuName, Model model) {
		System.out.println(menuName);
		Content content = new Content();
		switch (menuName) {
		case "video-Templates":
			content.setMenuNo(1);
			break;
		case "Graphic-Templates":
			content.setMenuNo(4);
			break;
		case "Stock-Video":
			content.setMenuNo(5);
			break;
		case "Photos":
			content.setMenuNo(6);
			break;
		case "Music":
			content.setMenuNo(2);
			break;
		case "Sound-Effects":
			content.setMenuNo(3);
			break;
		case "Fonts":
			content.setMenuNo(7);
			break;
		}
		int listCount = aService.getrequestPostCount(content);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Content> list = aService.selectAllTemplates(content, pi);
		model.addAttribute("list", list).addAttribute("pi", pi).addAttribute("menuName", menuName);
		return "management_templates";
	}

	@GetMapping("request")
	public ModelAndView joinRequest(@RequestParam(value = "page", defaultValue = "1") int currentPage, ModelAndView mv) {
		int listCount = aService.getrequestPostCount(null);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Content> list = aService.selectAllRequestPost(null, pi);
		for (Content c : list) {
			System.out.println(c);
			c.setUserId(aService.selectUser(c.getUserNo()));
		}
		mv.addObject("list", list).addObject("pi", pi);
		mv.setViewName("management_request");
		return mv;
	}

	@GetMapping("request/{menuName}")
	public String filterRequest(@RequestParam(value = "page", defaultValue = "1") int currentPage, @PathVariable("menuName") String menuName, Model model) {
		System.out.println(menuName);
		Content content = new Content();
		switch (menuName) {
		case "video-Templates":
			content.setMenuNo(1);
			break;
		case "Graphic-Templates":
			content.setMenuNo(4);
			break;
		case "Stock-Video":
			content.setMenuNo(5);
			break;
		case "Photos":
			content.setMenuNo(6);
			break;
		case "Music":
			content.setMenuNo(2);
			break;
		case "Sound-Effects":
			content.setMenuNo(3);
			break;
		case "Fonts":
			content.setMenuNo(7);
			break;
		}
		int listCount = aService.getTemplatesCount(content);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Content> list = aService.selectAllRequestPost(content, pi);
		model.addAttribute("list", list).addAttribute("pi", pi).addAttribute("menuName", menuName);
		return "management_request";
	}

	@GetMapping("mupdate")
	@ResponseBody
	public int userUpdate(@ModelAttribute Users user) {
		return aService.userUpdate(user);
	}

	@GetMapping("iupdate")
	@ResponseBody
	public int inquiryUpdate(@ModelAttribute Content content) {
		if (content.getContentStatus().equals("N")) {
			content.setDeleteStatus("Y");
		} else {
			content.setDeleteStatus("N");
		}

		return aService.inquiryUpdate(content);
	}

	@GetMapping("tupdate")
	@ResponseBody
	public int templatesUpdate(@ModelAttribute Content content) {
		if (content.getContentStatus().equals("N")) {
			content.setDeleteStatus("Y");
		} else {
			content.setDeleteStatus("N");
		}

		return aService.templatesUpdate(content);
	}

	@GetMapping("rupdate")
	@ResponseBody
	public int requestUpdate(@ModelAttribute Content content) {
		if (content.getContentStatus().equals("N")) {
			content.setDeleteStatus("Y");
		} else {
			content.setDeleteStatus("N");
		}

		return aService.requestUpdate(content);
	}

	@PostMapping("insertReply")
	@ResponseBody
	public int insertReply(@ModelAttribute Reply reply, HttpSession session, Model model, @RequestParam("contentNo") String contentNo) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			reply.setWriter(loginUser.getUserNickname());
			reply.setContentNo(contentNo);
		}
		try {
			int result = aService.insertReply(reply);
			if (result > 0) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}