package pocopoco_vplay.users.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.vo.Users;

import pocopoco_vplay.users.model.vo.Users;

@Mapper
public interface UsersMapper {

	int checkId(String id);

  Users signIn(Users user);

	int insertUser(Users user);

	ArrayList<HashMap<String, Object>> selectMyProject(int id);

	ArrayList<Content> selectMyRealProjects(int userNo);

}

