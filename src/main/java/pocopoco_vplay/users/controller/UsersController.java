package pocopoco_vplay.users.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import pocopoco_vplay.users.exception.UsersException;
import pocopoco_vplay.users.model.service.UsersService;
import pocopoco_vplay.users.model.vo.Users;

@Controller
@RequestMapping("/users/")
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class UsersController {
	private final UsersService uService;

    private final BCryptPasswordEncoder bcrypt;
	private final JavaMailSender mailSender;
	
	
	
	
	
	
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
		System.out.println("email은 "+email);
		String subject = "인증번호 입니다.";
		String random = "";
		for(int i=0;i<5;i++) {
			random += (int)(Math.random()*10);
		}
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		
		
		try {
			mimeMessageHelper.setSubject(random);
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setText("인증번호는 : "+ random + " 입니다.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(mimeMessage);
		System.out.println(random);
		return random;
		
	}
	
	@PostMapping("signUp")
	public int joinUser(@ModelAttribute Users user) {
		user.setUserPw(bcrypt.encode(user.getUserPw()));
		
		Date userBirthday = user.getUserBirth();
		
		if(userBirthday != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String birthDateString = sdf.format(userBirthday);
			String realBirthday = birthDateString.replace("-", "/");
			
			SimpleDateFormat StringtoDate = new SimpleDateFormat("yyyy/MM/dd");
			try {
				Date formattedDate = StringtoDate.parse(realBirthday);
				user.setUserBirth(formattedDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		int result = uService.insertUser(user);
		System.out.println("결과 값은 : " + result);
		
		return result;
	}






















































    @GetMapping("signIn")
    public String signIn() {
        System.out.println(bcrypt.encode("vplay"));
        return "signIn"; }

    @PostMapping("signIn")
    public String login(Users user, Model model, @RequestParam("beforeURL") String beforeURL){
        Users loginUser = uService.signIn(user);

        if(loginUser != null && bcrypt.matches(user.getUserPw(), loginUser.getUserPw())){

            model.addAttribute("loginUser", loginUser);
            if(loginUser.getIsAdmin().equals("Y")){
                return "redirect:/admin/dashboard";
            }else{
                return "redirect:" + beforeURL;
            }
        } else{
            throw new UsersException("로그인을 실패하였습니다.");
        }
    }


}
