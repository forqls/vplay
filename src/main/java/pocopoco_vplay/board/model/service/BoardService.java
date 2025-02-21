package pocopoco_vplay.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.model.mapper.BoardMapper;

@Service
@RequiredArgsConstructor
public class BoardService {
		
	private final BoardMapper mapper;
	
	public ArrayList<HashMap<String, Object>> selectCategory(String menu) {
		return mapper.selectCategory(menu);
	}

}
