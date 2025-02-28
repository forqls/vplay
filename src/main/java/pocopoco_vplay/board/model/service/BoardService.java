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
		return mapper.selectCategory(menu,userNo,sort);
	}

	public ArrayList<Content> selectCategoryMyProjects(String menu, int userNo, String sort) {
		return mapper.selectCategoryMyProjects(menu,userNo,sort);
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
		return mapper.selectCategoryMyTrash(menu,userNo,sort);
	}

}
