package pocopoco_vplay.users.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.SessionAttributes;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("singUp")
	public String singUp() {
		return "signup";
	}
	
	@PostMapping("idCheck")
	public int checkId(@RequestParam("id") String id) {
		System.out.println("userId 는 " + id);
		return uService.checkId(id);
	}



























































    @PostMapping("/users/signIn")
    public String login(Users user, Model model, @RequestParam("beforeURL") String beforeURL){
        Users loginUser = uService.login(user);
        if(loginUser != null && bcrypt.matches(user.getUserPw(), loginUser.getUserPw())){
            model.addAttribute("loginUser", loginUser);

            if(loginUser.getIsAdmin().equals("Y")){
                return "redirect:/admin/index";
            }else{
                return "redirect:" + beforeURL;
            }
        } else{
            throw new UsersException("로그인을 실패하였습니다.");
        }
    }


}
