package pocopoco_vplay.admin.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

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

	String selectUser(int userNo);

	int getTemplatesCount();

	ArrayList<Content> selectAllTemplates(RowBounds rowBounds);

	int getrequestPostCount();

	ArrayList<Content> selectAllRequestPost(RowBounds rowBounds);

	int userUpdate(Users user);

	int inquiryUpdate(Content content);

}
