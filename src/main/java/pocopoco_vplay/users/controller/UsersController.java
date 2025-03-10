package pocopoco_vplay.users.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/users/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class UsersController {
	private final UsersService uService;
	private final BoardService bService;
	private final BCryptPasswordEncoder bcrypt;
	private final JavaMailSender mailSender;

	@GetMapping("home")
	public String goHome(HttpSession session) {
		session.removeAttribute("kakaoUser");
		return "redirect:/";
	}

	@GetMapping("price")
	public String price() {
		return "price";
	}

	@GetMapping("signUp")
	public String singUp() {
		return "signup";
	}

	@PostMapping("idCheck")
	@ResponseBody
	public int checkId(@RequestParam("id") String id) {
		System.out.println(1);
		System.out.println("userId 는 " + id);
		int result = uService.checkId(id);
		System.out.println(result);
		return result;
	}

	@GetMapping("emailCheck")
	@ResponseBody
	public String emailSend(@RequestParam("email") String email) {
		System.out.println("email");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		System.out.println("email은 " + email);
		String random = "";
		for (int i = 0; i < 5; i++) {
			random += (int) (Math.random() * 10);
		}
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setFrom("poco.vplay@gmail.com");
			mimeMessageHelper.setSubject(random);
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setText("인증번호는 : " + random + " 입니다.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(mimeMessage);
		System.out.println(random);
		return random;
	}

	@PostMapping("signUp")
	public String joinUser(@ModelAttribute Users user, HttpSession session) {
		user.setUserPw(bcrypt.encode(user.getUserPw()));

		Users kakaoUser = (Users) session.getAttribute("kakaoUser");
		if (kakaoUser != null) {
			user.setKakaoId(kakaoUser.getKakaoId());
			user.setLoginType(kakaoUser.getLoginType());
		}
		System.out.println(user);
		int result = uService.insertUser(user);
		System.out.println("결과 값은 : " + result);
		return "signup_success";

	}

	@GetMapping("signIn")
	public String signIn() {
		return "signIn";
	}

	@PostMapping("signIn")
	public String login(Users user, Model model, @RequestParam("beforeURL") String beforeURL, HttpSession session) {
		Users loginUser = uService.signIn(user);
		if (loginUser != null && bcrypt.matches(user.getUserPw(), loginUser.getUserPw())) {
			session.setAttribute("loginUser", loginUser);
			if (loginUser.getIsAdmin().equals("Y")) {
				return "redirect:/admin/dashboard";
			} else {
				if (beforeURL.contains("/signIn") || beforeURL.isEmpty()) {
					return "redirect:/";
				} else {
					return "redirect:" + beforeURL;
				}
			}
		} else {
			throw new UsersException("로그인을 실패하였습니다.");
		}
	}

	@GetMapping("logout")
	public String logout(SessionStatus session) {
		session.setComplete();
		return "redirect:/";
	}

	@GetMapping("findId")
	public String findId() {
		return "find_id";
	}

	@PostMapping("findId")
	@ResponseBody
	public int findId(@RequestParam(required = false, value = "userName") String userName, @RequestParam(required = false, value = "userPhone") String userPhone) {
		System.out.println(userPhone);
		System.out.println(userName);
		int result = uService.selectIdPhone(userName, userPhone);
		System.out.println(result);
		return result;
	}

	@PostMapping("findIdSuccess")
	public String findIdSuccess(@ModelAttribute Users users, Model model) {
		String usersId = uService.findId(users);
		model.addAttribute("users", users);
		model.addAttribute("usersId", usersId);
		return "find_id_success";
	}

	@GetMapping("findPw")
	public String findPw() {
		return "find_pw";
	}

	@PostMapping("findPw")
	@ResponseBody
	public int findPw(@ModelAttribute Users users) {
		System.out.println("Controller 실행됨: " + users);
		int result = uService.findPw(users);
		System.out.println("결과: " + result);
		return result;
	}

	@PostMapping("findPwSuccess")
	public String findPwSuccess(@ModelAttribute Users users, Model model) {
		String tempPwd = tempPwdMk();
		String encodePwd = bcrypt.encode(tempPwd);
		String userName = uService.findName(users);
		users.setUserPw(encodePwd);
		int encodeUserPwd = uService.encodePwd(users);
		System.out.println(userName);
		if (encodeUserPwd == 1) {
			model.addAttribute("userName", userName);
			model.addAttribute("tempPwd", tempPwd);
			System.out.println("업데이트 완료");
			return "find_pw_success";
		} else {
			throw new UsersException("비밀번호 업데이트 실패");
		}
	}

	// 임시 비번 생성 메소드
	private String tempPwdMk() {
		int length = 10;
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder password = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			password.append(chars.charAt(random.nextInt(chars.length())));
		}
		return password.toString();
	}

	@GetMapping("findfollow")
	@ResponseBody
	public int findfollow(@ModelAttribute Users user) {
		return uService.findfollow(user);
	}

	@GetMapping("my_projects")
	public String myProjects(Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			int userNo = loginUser.getUserNo();
			ArrayList<Content> list = uService.selectMyRealProjects(userNo); // 자기가 한 프로젝트 가져오기
			System.out.println("리스트 사이즈는 : " + list.size());
			model.addAttribute("list", list);
		} else {
			throw new UsersException("로그인이 풀렸습니다.");
		}
		return "my_projects";
	}

	@GetMapping("my_favorites")
	public String myFavorites(Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			int id = loginUser.getUserNo();
			ArrayList<HashMap<String, Object>> list = uService.selectMyProjects(id); // 좋아요 한 목록가져오기
			System.out.println(list);
			model.addAttribute("list", list);
		} else {
			throw new UsersException("로그인이 풀렸습니다.");
		}
		return "my_favorites";
	}

	@GetMapping("my_downloads")
	public String myDownloads(Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			return "my_downloads";
		} else {
			throw new UsersException("로그인이 풀렸습니다.");
		}
	}

	@GetMapping("my_account")
	public String myAccount(Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			return "my_account";
		} else {
			throw new UsersException("로그인이 풀렸습니다.");
		}
	}

	@GetMapping("checkPw")
	@ResponseBody
	public int checkPassword(@RequestParam("password") String password, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (bcrypt.matches(password, loginUser.getUserPw())) {
			return 1;
		} else {
			return 0;
		}
	}

	@PostMapping("updateInfo")
	public String updateInfo(@ModelAttribute Users user, Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		user.setUserNo(loginUser.getUserNo());
		int result = uService.updateInfo(user);
		if (result > 0) {
			model.addAttribute("loginUser", uService.signIn(loginUser));
			return "redirect:/users/my_account";
		} else {
			throw new UsersException("회원수정을 실패하였습니다.");
		}
	}

	@GetMapping("changeCheckPw")
	@ResponseBody
	public int changeCheckPw(@RequestParam("currentPwd") String password, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (bcrypt.matches(password, loginUser.getUserPw())) {
			return 1;
		} else {
			return 0;
		}
	}

	@PostMapping("changePw")
	public String changePw(@RequestParam("newPwd") String newPassword, Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		String encodedPassword = bcrypt.encode(newPassword);
		Users user = new Users();
		user.setUserNo(loginUser.getUserNo());
		user.setUserPw(encodedPassword);
		int result = uService.changePw(user);
		if (result > 0) {
			model.addAttribute("loginUser", uService.signIn(loginUser));
		} else {
			throw new UsersException("비밀번호 변경을 실패하였습니다.");
		}
		return "redirect:/users/my_account";
	}

	@GetMapping("my_payments")
	public String myPayments(Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			model.addAttribute("loginUser", loginUser);
			return "my_payments";
		} else {
			throw new UsersException("로그인이 필요합니다.");
		}
	}

	@GetMapping("my_inquiry")
	public String myInquiry(HttpSession session, Model model) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			int userNo = loginUser.getUserNo();
			ArrayList<Content> list = bService.selectMyInquiry(userNo);
			for (Content content : list) {
				Reply reply = bService.selectReply(content.getContentNo());
				content.setReply(reply);
			}
			model.addAttribute("list", list);
			return "my_inquiry";
		} else {
			throw new UsersException("로그인이 필요합니다.");
		}
	}

	@GetMapping("my_commission")
	public String myCommission(HttpSession session, Model model) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser != null) {
	        int userNo = loginUser.getUserNo();
	        ArrayList<Content> list = bService.selectMyCommission(userNo);
	        for (Content content : list) {
	            Reply reply = bService.selectReply(content.getContentNo());
	            content.setReply(reply);
	        }
	        model.addAttribute("list", list);
	        return "my_commission";
	    } else {
	        throw new UsersException("로그인이 필요합니다.");
	    }
	}


	@GetMapping("my_trash")
	private String myTrashPage(HttpSession session, Model model) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			int userNo = loginUser.getUserNo();
			ArrayList<Content> list = bService.selectMyTrash(userNo);
			model.addAttribute("list", list);
			return "my_trash";
		} else {
			throw new UsersException("로그인이 풀렸습니다.");
		}
	}

}