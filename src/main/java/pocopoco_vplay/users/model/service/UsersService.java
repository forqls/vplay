package pocopoco_vplay.users.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
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
}