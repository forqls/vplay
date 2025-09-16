package pocopoco_vplay.users.controller;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pocopoco_vplay.board.model.service.BoardService;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Message;
import pocopoco_vplay.users.model.vo.Users;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/users/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class UsersController {
	private final UsersService uService;
	private final BoardService bService;
	private final BCryptPasswordEncoder bcrypt;

	@Value("${SENDGRID_API_KEY}") // Railway 변수를 주입
	private String sendGridApiKey;

	@GetMapping("home")
	public String goHome(HttpSession session) {
		session.removeAttribute("kakaoUser");
		return "redirect:/";
	}

	@GetMapping("license")
	public String license() {
		return "license";
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
		System.out.println("SendGrid로 이메일 발송 시도: " + email);

		String random = "";
		for (int i = 0; i < 5; i++) {
			random += (int) (Math.random() * 10);
		}

		System.out.println("생성된 인증번호: " + random);

		// 이메일 발송 로직 (SendGrid)
		Email from = new Email("poco.vplay@gmail.com");
		String subject = "VPLAY 인증번호 안내";
		Email to = new Email(email); // 받는 사람 이메일
		com.sendgrid.helpers.mail.objects.Content content = new com.sendgrid.helpers.mail.objects.Content("text/plain", "인증번호는 " + random + " 입니다.");
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(sendGridApiKey);
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println("SendGrid 응답 코드: " + response.getStatusCode());
			if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
				System.out.println("이메일 발송 성공!");
				return random; // 성공 시 인증번호 반환
			} else {
				System.out.println("SendGrid 에러: " + response.getBody());
				return "error"; // 실패 시 "error" 문자열 반환
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return "error";
		}
	}

	@PostMapping("signUp")
	public String joinUser(@ModelAttribute Users user, HttpSession session) {

		// 1. 세션에서 소셜 로그인 정보가 있는지 먼저 확인 (카카오 또는 구글)
		Users socialUser = (Users) session.getAttribute("kakaoUser");
		if (socialUser == null) {
			socialUser = (Users) session.getAttribute("googleUser");
		}

		if (socialUser != null) {
			// --- 2-A. 소셜 로그인 사용자인 경우 ---
			user.setLoginType(socialUser.getLoginType());

			if ("K".equals(socialUser.getLoginType())) {
				user.setKakaoId(socialUser.getKakaoId());
				// 카카오 사용자는 user_id가 null이므로, 카카오 ID를 기반으로 고유한 user_id를 생성
				if (user.getUserId() == null || user.getUserId().isEmpty()) {
					user.setUserId("kakao_" + socialUser.getKakaoId());
				}
			} else if ("G".equals(socialUser.getLoginType())) {
				user.setGoogleId(socialUser.getGoogleId());
				// 구글 사용자는 user_id가 null이므로, 구글 ID를 기반으로 고유한 user_id를 생성
				if (user.getUserId() == null || user.getUserId().isEmpty()) {
					user.setUserId("google_" + socialUser.getGoogleId());
				}
				if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
					user.setUserEmail(socialUser.getUserEmail());
				}
			}

			// 소셜 유저는 비밀번호가 null이므로, DB 저장을 위해 더미 암호 생성
			user.setUserPw(bcrypt.encode("!SOCIAL_DUMMY_PW_" + UUID.randomUUID().toString()));

			// 사용 완료한 세션 정보는 즉시 제거
			session.removeAttribute("kakaoUser");
			session.removeAttribute("googleUser");

		} else {
			// --- 2 일반 회원
			user.setUserPw(bcrypt.encode(user.getUserPw()));
		}

		// --- 3. 공통 로직: DB 저장 ---
		System.out.println("DB 저장 시도: " + user);
		int result = uService.insertUser(user);
		System.out.println("결과 값은 : " + result);

		// 4. 완료 페이지로 이동
		return "signup_success";
	}

	@PostMapping("phoneCheck")
	@ResponseBody
	public int checkPhone(@RequestParam("userPhone") String userPhone) {
		int result = uService.checkPhone(userPhone);
		return result;
	}

	@PostMapping("emailDuplicateCheck")
	@ResponseBody
	public int emailDuplicateCheck(@RequestParam("userEmail") String userEmail) {
		int result = uService.checkEmail(userEmail);
		return result;
	}

	@GetMapping("signIn")
	public String signIn(HttpServletRequest request, Model model) {
		String beforeURL = request.getHeader("Referer");
		if (beforeURL != null && !beforeURL.contains("/signIn")) {
			request.getSession().setAttribute("beforeURL", beforeURL);
		}
		return "signIn";
	}
	@PostMapping("signIn")
	public String login(Users user, Model model, @RequestParam("beforeURL") String beforeURL, HttpSession session, RedirectAttributes redirectAttributes) {
		Users loginUser = uService.signIn(user);
		if (loginUser != null && bcrypt.matches(user.getUserPw(), loginUser.getUserPw())) {
			session.setAttribute("loginUser", loginUser);
			int dayResult = 0;
//			System.out.println("반환값은 : " + uService.getPaymentDate(loginUser));
			// Timestamp 이새기는 Date보다 좋고 db저장도 쉬운데 , 직접적으로 날짜를 더해주는 함수가 존재하지 않음 그래서 그냥
			// localDateTime으로 형변환해준후에 plusdays 메소드 써서 30일 더해줘야됨
			// 그 과정에서 스는 함수들이 toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			// 얘네들인데
			// toInstant() 얘는 Timestamp를 Instant(UTC기준)으로 변환해주고, localDate는 시간정보를 포함하지않음
			// 데이터형이라 atZone(ZoneId.systemDefault())얘를 써줘서
			// 현재 시스템의 기본날짜로 세팅해준 후 , toLocalDateTime()을 이용해서 최종적으로 LocalDateTime 객체로 변환해주는
			// 삼단계가 필요함
			// 30일날짜만 더하기
			Timestamp loginUserPaymentDate = (Timestamp) uService.getPaymentDate(loginUser);
			if (loginUserPaymentDate == null) {

			} else {

				LocalDateTime localDateTime = loginUserPaymentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

				Timestamp loginUserPaymentEndDate = Timestamp.valueOf(localDateTime.plusDays(30));
				// 오늘 날짜 가져오기
				Timestamp today = Timestamp.valueOf(LocalDateTime.now());

				boolean isPAymentExpired = today.after(loginUserPaymentEndDate);
				boolean hasSeenAlert = loginUser.isAlertShown();
				//System.out.println(loginUser.isAlertShown());

				System.out.println("ddddddd 테스트용 : " + hasSeenAlert + "테스트용 : " + isPAymentExpired);

				if (isPAymentExpired && !hasSeenAlert) {
					System.out.println("결제일 한달 경과");

					dayResult = uService.deleteUserPlan(loginUser);
					uService.updateAlertShown(loginUser.getUserNo());

					loginUser = uService.signIn(user);
					session.setAttribute("loginUser", loginUser);

					redirectAttributes.addFlashAttribute("showAlert", true);
				} else {
					redirectAttributes.addFlashAttribute("showAlert", false);
				}
			}

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
			System.out.println("리스트 사이즈는 : " + list);
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
			ArrayList<Content> list = uService.selectMyProjects(id); // 좋아요 한 목록가져오기
			System.out.println("ddd" + list);
			model.addAttribute("list", list);
		} else {
			throw new UsersException("로그인이 풀렸습니다.");
		}
		return "my_favorites";
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
	public String myInquiry(Model model, HttpSession session) {
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
	public String myCommission(Model model, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser != null) {
			int userNo = loginUser.getUserNo();
			ArrayList<Content> list = bService.selectMyCommission(userNo);
			for (Content content : list) {
				Reply reply = bService.countReply(content.getContentNo());
				content.setReply(reply);
			}
			model.addAttribute("list", list);
			return "my_commission";
		} else {
			throw new UsersException("로그인이 필요합니다.");
		}
	}

	@GetMapping("my_trash")
	private String myTrashPage(Model model, HttpSession session) {
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

	@GetMapping("/messages/{userNo}")
	public String messagePage(@PathVariable("userNo") String userNo, HttpSession session, Model model) {
		System.out.println("들어왔음 " + userNo);
		Users loginUser = (Users) session.getAttribute("loginUser");

		if (loginUser == null || loginUser.getUserNo() != Integer.parseInt(userNo)) {
			return "redirect:/";
		}

		ArrayList<Message> list = uService.selectMyMessage(userNo);

		for (Message m : list) {
			String timeout = getTimeAgo(m.getSentTime());
			m.setTimeout(timeout);
		}
		System.out.println(list);

		model.addAttribute("list", list);

		return "my_message";
	}


	// 시간 변환 메소드 듀레이션사용함
	private String getTimeAgo(LocalDateTime sentTime) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(sentTime, now);

		if (duration.toMinutes() < 1) {
			return "방금 전";
		} else if (duration.toMinutes() < 60) {
			return duration.toMinutes() + "분 전";
		} else if (duration.toHours() < 24) {
			return duration.toHours() + "시간 전";
		} else {
			return sentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}
	}

	@GetMapping("updateMessageStatus")
	@ResponseBody
	public int updateMessageStatus(@RequestParam("messageNo") String messageNo) {
//		System.out.println("메세지 상태값 변경 잘 들어옴." + messageNo);
		int result = uService.updateMessageStatus(messageNo);
		return result;
	}

	@PostMapping("sendMessage")
	@ResponseBody
	public int sendMessage(@RequestBody Message msg, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		msg.setSenderNo(loginUser.getUserNo());
		msg.setSenderName(loginUser.getUserNickname());
		System.out.println("전달받은 쪽지의 구성은2 = " + msg);

		msg.setReceiverNo(uService.getReceiverNo(msg.getReceiverName()));

		System.out.println(msg);
		System.out.println("잘 들어옴");
//		System.out.println("전달받은 쪽지의 구성은2 = " + msg);
		int result = uService.insertMessage(msg);

		return result;
	}

	@GetMapping("existReceiver")
	@ResponseBody
	public int existReceiver(@RequestParam("receiver") String receiverName) {
//		System.out.println(receiverName);
		int result = uService.existReceiver(receiverName);

		return result;
	}
	@GetMapping("unreadMessages")
	@ResponseBody
	public int getUnreadMessageCount(HttpSession session) {
		Users loginUser = (Users) session.getAttribute("loginUser");
		//System.out.println("요청 들어오긴함");
		if (loginUser == null) {
			return 0;
		}
		return uService.getUnreadMessageCount(loginUser.getUserNo());
	}

	@GetMapping("creator/{userNo}")
	public String joinCreatorPage(@PathVariable("userNo")int userNo, Model model) {
		Users u = new Users();
		u.setUserNo(userNo);
		Users user = uService.signIn(u);

		model.addAttribute("Users", user);
		return "creator_page";
	}

	@GetMapping("my_downloads")
	public String myDownloads(Model model , HttpSession session) {
		Users loginUser = (Users)session.getAttribute("loginUser");
		if(loginUser!=null) {
			int userNo = loginUser.getUserNo();
			ArrayList<Content> list = uService.selectMyDownloads(userNo);
			System.out.println("다운로드 list = " + list);
			model.addAttribute("list",list);
		}else {
			throw new UsersException("로그인이 풀렸3");
		}
		return "my_downloads";
	}


	@GetMapping("subscribe")
	public String subscribe(HttpSession session, Model model) {
		Users loginUser = (Users)session.getAttribute("loginUser");
		if(loginUser != null){
			int userNo = loginUser.getUserNo();
			ArrayList<Users> l = uService.selectSubscribeList(userNo);
			HashMap<Integer,Users> map = new HashMap<Integer,Users>();

			for(Users u : l) {
				map.put(u.getUserNo(), u);
			}
			ArrayList<Users> list = new ArrayList<Users>(map.values());

//			for(Users i : list) {
//				System.out.println("dddddddddd" + i.getUserNo());
//			}
//			System.out.println(list);
//				System.out.println(list);
			model.addAttribute("list",list);
		}else {
			throw new UsersException("로그인이 풀렸3");
		}
		return "subscribe";
	}

	@GetMapping("createrPage")
	public String goToCreaterPage(@RequestParam("createrNo") int createrNo,
								  HttpSession session,
								  Model model) {
		try {
			Users loginUser = (Users) session.getAttribute("loginUser");
			boolean isSubscribed = false;

			if (loginUser != null) {
				int userNo = loginUser.getUserNo();
				System.out.println("로그인 유저 번호: " + userNo);
				System.out.println("크리에이터 번호: " + createrNo);

				isSubscribed = uService.isSubscribed(createrNo, userNo);
				System.out.println("구독 상태 (true/false): " + isSubscribed);
			}
			ArrayList<Content> list = uService.selectMyRealProjects(createrNo);
			Users createrUser = uService.getInfoUser(createrNo);

			Users tempUser = new Users();
			tempUser.setUserNo(createrNo);
			int subscriberCount = uService.findfollow(tempUser);

			model.addAttribute("list", list)
					.addAttribute("createrUser", createrUser)
					.addAttribute("subscriberCount", subscriberCount)
					.addAttribute("isSubscribed", isSubscribed)
					.addAttribute("createrNo", createrNo);

			return "createrPage";
		} catch (Exception e) {
			throw new UsersException("크리에이터 페이지 조회 실패: " + e.getMessage());
		}
	}







}