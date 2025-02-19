package pocopoco_vplay.admin.model.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.users.model.vo.Users;

@Mapper
public interface AdminMapper {

	int getUsersCount();

	ArrayList<Users> selectAllUser(RowBounds rowBounds);

	int getInquiryCount();

	ArrayList<Content> selectAllQuiry(RowBounds rowBounds);

}
