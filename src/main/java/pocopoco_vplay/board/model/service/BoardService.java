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
	
	public ArrayList<HashMap<String, Object>> selectCategory(String menu) {
		return mapper.selectCategory(menu);
	}

	public ArrayList<Content> selectCategoryMyProjects(String menu) {
		return mapper.selectCategoryMyProjects(menu);
	}

}
