package pocopoco_vplay.admin.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.admin.model.mapper.AdminMapper;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.common.model.vo.PageInfo;
import pocopoco_vplay.users.model.vo.Users;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AdminMapper mapper;

	public String selectUser(int userNo) {
		return mapper.selectUser(userNo);
	}

	public int getUsersCount(HashMap<String, String> map) {
		return mapper.getUsersCount(map);
	}

	public ArrayList<Users> selectAllUser(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectAllUser(map, rowBounds);
	}

	public int getInquiryCount(HashMap<String, String> map) {
		return mapper.getInquiryCount(map);
	}

	public ArrayList<Content> selectAllInquiry(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectAllInquiry(map, rowBounds);
	}

	public int getTemplatesCount(HashMap<String, String> map) {
		return mapper.getTemplatesCount(map);
	}

	public ArrayList<Content> selectAllTemplates(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectAllTemplates(map, rowBounds);
	}

	public int getrequestPostCount(HashMap<String, String> map) {
		return mapper.getrequestPostCount(map);
	}

	public ArrayList<Content> selectAllRequestPost(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectAllRequestPost(map, rowBounds);
	}

	public int userUpdate(Users user) {
		return mapper.userUpdate(user);
	}

	public int inquiryUpdate(Content content) {

		return mapper.inquiryUpdate(content);
	}

	public int templatesUpdate(Content content) {
		return mapper.templatesUpdate(content);
	}

	public int requestUpdate(Content content) {
		return mapper.requestUpdate(content);
	}

	public int countMenuTemp(int i) {
		return mapper.countMenuTemp(i);
	}

	public int insertReply(Reply reply) {
		return mapper.insertReply(reply);
	}
}