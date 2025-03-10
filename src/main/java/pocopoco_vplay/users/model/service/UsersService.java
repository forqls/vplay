package pocopoco_vplay.users.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.mapper.UsersMapper;
import pocopoco_vplay.users.model.vo.Users;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersMapper mapper;

	public int checkId(String id) {
		return mapper.checkId(id);
	}

	public Users signIn(Users user) {
		return mapper.signIn(user);
	}

	public int insertUser(Users user) {
		return mapper.insertUser(user);
	}

	public ArrayList<HashMap<String, Object>> selectMyProjects(int id) {
		return mapper.selectMyProject(id);
	}

	public ArrayList<Content> selectMyRealProjects(int userNo) {
//		System.out.println(mapper.selectMyRealProjects(userNo));
		return mapper.selectMyRealProjects(userNo);
	}

	public int selectIdPhone(String userName, String userPhone) {
		return mapper.selectuserIdPhone(userName, userPhone);
	}

	public int findfollow(Users user) {
		return mapper.findfollow(user);
	}

	public int updateInfo(Users user) {
		return mapper.updateInfo(user);
	}

	public String findId(Users users) {
		return mapper.findId(users);
	}

	public int changePw(Users user) {
		return mapper.changePw(user);
	}

//	public int findPw(Users users) {
//    return mapper.findPw(users);
//  }

	public int findPw(Users users) {
		return mapper.findPw(users);
	}

	public int encodePwd(Users users) {
		return mapper.tempPwd(users);
	}

	public String findName(Users users) {
		return mapper.findName(users);
	}

	public Users existUsers(String kakaoId) {
		return mapper.existUsers(kakaoId);
	}

	public ArrayList<Users> selectTopUser() {
		return mapper.selectTopUser();
	}

}