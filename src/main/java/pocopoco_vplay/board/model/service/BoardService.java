package pocopoco_vplay.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.mapper.BoardMapper;
import pocopoco_vplay.board.model.vo.Content;

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

}
