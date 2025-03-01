package pocopoco_vplay.board.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import pocopoco_vplay.board.model.vo.Content;

@Mapper
public interface BoardMapper {

	ArrayList<HashMap<String, Object>> selectCategory(@Param("menu") String menu, @Param("userNo") int userNo, @Param("sort") String sort);

	ArrayList<Content> selectCategoryMyProjects(@Param("menu") String menu, @Param("userNo") int userNo, @Param("sort") String sort);

	ArrayList<Content> selectMyInquiry(int userNo);

	int throwBoardTrash(int contentNo);

	ArrayList<Content> selectMyTrash(int userNo);

	ArrayList<Content> selectCategoryMyTrash(@Param("menu") String menu, @Param("userNo") int userNo, @Param("sort") String sort);

	int insertInquiry(Content inquiry);

	int insertBoard(Content inquiry);

	Content selectInquiry(int contentNo);

}