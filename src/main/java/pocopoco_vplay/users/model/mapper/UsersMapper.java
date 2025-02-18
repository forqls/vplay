package pocopoco_vplay.users.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import pocopoco_vplay.users.model.vo.Users;

import pocopoco_vplay.users.model.vo.Users;

@Mapper
public interface UsersMapper {

	int checkId(String id);

  Users signIn(Users user);

	int insertUser(Users user);

}

