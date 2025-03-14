package pocopoco_vplay.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.mapper.BoardMapper;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.commom.model.vo.PageInfo;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardMapper mapper;

	public ArrayList<HashMap<String, Object>> selectCategory(String menu, int userNo, String sort) {
		return mapper.selectCategory(menu, userNo, sort);
	}

	public ArrayList<Content> selectCategoryMyProjects(String menu, int userNo, String sort) {
		return mapper.selectCategoryMyProjects(menu, userNo, sort);
	}

	public ArrayList<Content> selectMyInquiry(int userNo) {
		return mapper.selectMyInquiry(userNo);
	}

	public ArrayList<Content> selectMyCommission(int userNo) {
		return mapper.selectMyCommission(userNo);
	}

	public int throwBoardTrash(int contentNo) {
		return mapper.throwBoardTrash(contentNo);
	}

	public ArrayList<Content> selectMyTrash(int userNo) {
		return mapper.selectMyTrash(userNo);
	}

	public ArrayList<Content> selectCategoryMyTrash(String menu, int userNo, String sort) {
		return mapper.selectCategoryMyTrash(menu, userNo, sort);
	}

	public int insertInquiry(Content inquiry) {
		int result1 = mapper.insertInquiry(inquiry);
		int result2 = mapper.insertBoard(inquiry);

		if (result1 > 0 && result2 > 0) {
			return result1;
		} else {
			throw new RuntimeException("문의 작성 실패: CONTENT 또는 BOARD 테이블 INSERT 실패");
		}
	}

	public Content selectInquiry(int contentNo) {
		return mapper.selectInquiry(contentNo);
	}

	public Reply selectReply(int contentNo) {
		return mapper.selectReply(contentNo);
	}

	public int updateInquiry(Content inquiry) {
		int result1 = mapper.updateInquiry(inquiry);
		int result2 = mapper.updateBoard(inquiry);

		if (result1 > 0 && result2 > 0) {
			return result1;
		} else {
			throw new RuntimeException("문의 작성 실패: CONTENT 또는 BOARD 테이블 UPDATE 실패");
		}
	}

	public ArrayList<Content> allCategory(int i) {
		return mapper.allCategory(i);
	}

	public ArrayList<Content> allPopularCate(int i) {
		return mapper.allPopularCate(i);
	}

	public int getrequestPostCount(Content content) {
		return mapper.getrequestPostCount(content);
	}

	public ArrayList<Content> selectAllRequestPost(Content content, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

		return mapper.selectAllRequestPost(content,rowBounds);
	}

	public String selectUser(int userNo) {
		return mapper.selectUser(userNo);
	}

	public int insertRequest(Content content) {
		return mapper.insertRequest(content);
	}

	public int insertRequestBoard(Content content) {
		return mapper.insertRequestBoard(content);
	}

	public Content selectRequest(int bId, int id) {
		Content c = mapper.selectRequest(bId);
		if (c != null && id != 0 && c.getUserNo() != id) {
			int result = mapper.updateCount(bId);
			if (result > 0) {
				c.setViews(c.getViews() + 1);
			}
		}
		return c;
	}

	public Content allMenuDetail(int contentNo) {
		return mapper.allMenuDetail(contentNo);
	}

	public ArrayList<Reply> selectReplyList(int bId) {
		return mapper.selectReplyList(bId);
	}

	public int allTempLike(HashMap<String, Integer> map) {
		System.out.println(map);
		return mapper.allTempLike(map);
	}

	public int menuLikeTo(int num, int userNo) {
		return mapper.menuLikeTo(num, userNo);
	}

	public int unAllTempLike(HashMap<String, Integer> map) {
		return mapper.unAllTempLike(map);
	}

	public int updateRequest(Content c) {
		return mapper.updateRequest(c);
	}

	public int updateRequestMenu(Content c) {
		return mapper.updateRequestMenu(c);
	}

	public int deleteBoard(int bId) {
		return mapper.deleteBoard(bId);
	}

	public ArrayList<Content> selectContentTop() {
		return mapper.selectContentTop();
	}

	public int insertReply(Reply reply) {
		return mapper.insertReply(reply);
	}

	public ArrayList<Content> allTemplateList(HashMap<String, Object> map) {
		return mapper.allTemplateList(map);
	}

	public Reply countReply(int contentNo) {
		return mapper.countReply(contentNo);
	}

	public int updateReply(Reply r) {
		return mapper.updateReply(r);
	}

	public int deleteReply(int replyNo) {
		return mapper.deleteReply(replyNo);
	}

	public ArrayList<Content> selectRequestList(Content content) {
		return mapper.selectRequestList(content);
	}

	public List<Content> searchRequest(Map<String, Object> searchValue) {
		return mapper.searchRequest(searchValue);
	}

}