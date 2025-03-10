package pocopoco_vplay.board.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.session.RowBounds;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Reply;

@Mapper
public interface BoardMapper {

	ArrayList<HashMap<String, Object>> selectCategory(@Param("menu") String menu, @Param("userNo") int userNo, @Param("sort") String sort);

	ArrayList<Content> selectCategoryMyProjects(@Param("menu") String menu, @Param("userNo") int userNo, @Param("sort") String sort);

	ArrayList<Content> selectMyInquiry(int userNo);

	ArrayList<Content> selectMyCommission(int userNo);

	int throwBoardTrash(int contentNo);

	ArrayList<Content> selectMyTrash(int userNo);

	ArrayList<Content> selectCategoryMyTrash(@Param("menu") String menu, @Param("userNo") int userNo, @Param("sort") String sort);

	int insertInquiry(Content inquiry);

	int insertBoard(Content inquiry);

	int updateInquiry(Content inquiry);

	int updateBoard(Content inquiry);

	Content selectInquiry(int contentNo);

	Reply selectReply(int contentNo);

	ArrayList<Content> allTemplateList(@Param("menuName") String menuName);

	ArrayList<Content> allCategory(@Param("menuNo") int i);

	ArrayList<Content> allPopularCate(int i);

	int getrequestPostCount();

	ArrayList<Content> selectAllRequestPost(RowBounds rowBounds);

	String selectUser(int userNo);

	int insertRequest(Content content);

	int insertRequestBoard(Content content);

	Content selectRequest(int bId);

	int updateCount(int bId);

	Content allMenuDetail(@Param("contentNo") int contentNo);

	ArrayList<Reply> selectReplyList(int bId);

	int allTempLike(HashMap<String, Integer> map);

	int menuLikeTo(@Param("contentNo") int num, @Param("userNo") int userNo);

	int unAllTempLike(HashMap<String, Integer> map);

	int updateRequest(Content c);

	int updateRequestMenu(Content c);

	int deleteBoard(int bId);

	ArrayList<Content> selectOrderByViews();

	int insertReply(Reply reply);
}
