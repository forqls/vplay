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

	String selectUser(int userNo);

	int getUsersCount(HashMap<String, String> map);

	ArrayList<Users> selectAllUser(HashMap<String, String> map, RowBounds rowBounds);

	int getInquiryCount(HashMap<String, String> map);

	ArrayList<Content> selectAllInquiry(HashMap<String, String> map, RowBounds rowBounds);

	int getTemplatesCount(HashMap<String, String> map);

	ArrayList<Content> selectAllTemplates(HashMap<String, String> map, RowBounds rowBounds);

	int getrequestPostCount(HashMap<String, String> map);

	ArrayList<Content> selectAllRequestPost(HashMap<String, String> map, RowBounds rowBounds);

	int userUpdate(Users user);

	int inquiryUpdate(Content content);

	int templatesUpdate(Content content);

	int requestUpdate(Content content);

	int countMenuTemp(int i);

	int insertReply(Reply reply);

}