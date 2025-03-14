package pocopoco_vplay.users.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.vo.Users;

@Mapper
public interface UsersMapper {

	int checkId(String id);

	Users signIn(Users user);

	int insertUser(Users user);

	ArrayList<HashMap<String, Object>> selectMyProject(int id);

	ArrayList<Content> selectMyRealProjects(int userNo);

	int selectuserIdPhone(@Param("userName") String userName, @Param("userPhone") String userPhone);

	int findfollow(Users user);

	String findId(Users users);

	int updateInfo(Users user);

	int changePw(Users user);

	int findPw(Users Users);

	int tempPwd(Users Users);

	String findName(Users users);

	Users existUsers(String kakaoId);

	ArrayList<Users> selectTopUser();

	int updateProfile(HashMap<String, String> map);

	String selectProfile(HashMap<String, String> map);

}
