package pocopoco_vplay.board.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import pocopoco_vplay.board.model.vo.Content;

@Mapper
public interface BoardMapper {

	ArrayList<HashMap<String, Object>> selectCategory(String menu);

	ArrayList<Content> selectCategoryMyProjects(String menu);

}
