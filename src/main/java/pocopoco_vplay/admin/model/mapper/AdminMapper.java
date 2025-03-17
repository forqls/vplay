package pocopoco_vplay.admin.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.users.model.vo.Users;

@Mapper
public interface AdminMapper {

	int getUsersCount();

	ArrayList<Users> selectAllUser(RowBounds rowBounds);

	int getInquiryCount(Content content);

	ArrayList<Content> selectAllInquiry(HashMap<String, String> map, RowBounds rowBounds);

	String selectUser(int userNo);

	int getTemplatesCount(Content content);

	ArrayList<Content> selectAllTemplates(HashMap<String, String> map, RowBounds rowBounds);

	int getrequestPostCount(Content content);

	ArrayList<Content> selectAllRequestPost(HashMap<String, String> map, RowBounds rowBounds);

	int userUpdate(Users user);

	int inquiryUpdate(Content content);

	int templatesUpdate(Content content);

	int requestUpdate(Content content);

	int countMenuTemp(int i);

	int insertReply(Reply reply);

}
