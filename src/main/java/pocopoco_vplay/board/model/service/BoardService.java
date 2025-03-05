package pocopoco_vplay.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.mapper.BoardMapper;
import pocopoco_vplay.board.model.vo.Content;
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
		System.out.println(inquiry);
		int result1 = mapper.insertInquiry(inquiry);
		System.out.println(inquiry);
		int result2 = mapper.insertBoard(inquiry);
		System.out.println(inquiry);

		if (result1 > 0 && result2 > 0) {
			return result1;
		} else {
			throw new RuntimeException("문의 작성 실패: CONTENT 또는 BOARD 테이블 INSERT 실패");
		}
	}

	public Content selectInquiry(int contentNo) {
		return mapper.selectInquiry(contentNo);
	}

	public ArrayList<Content> allTemplateList(String menuName) {
		return mapper.allTemplateList(menuName);
	}

	public int updateInquiry(Content inquiry) {
		return mapper.updateInquiry(inquiry);
	}


	public ArrayList<Content> allCategory(int i) {
		return mapper.allCategory(i);
	}

	public ArrayList<Content> allPopularCate(int i) {
		return mapper.allPopularCate(i);
	}

  public int getrequestPostCount() {
  return mapper.getrequestPostCount();
  }

	public ArrayList<Content> selectAllRequestPost(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectAllRequestPost(rowBounds);
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
		if(c != null && id != 0 && c.getUserNo() != id) {
			int result =mapper.updateCount(bId);
			if(result >0) {
				c.setViews(c.getViews() +1 );
			}
		}
		return c;
  }
  
	public Content allMenuDetail(int contentNo) {
		return mapper.allMenuDetail(contentNo);
	}
}
