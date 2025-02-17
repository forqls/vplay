package pocopoco_vplay.users.model.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

	int checkId(String id);

}
