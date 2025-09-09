package pocopoco_vplay.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.board.Video;
import pocopoco_vplay.board.VideoResponseDto;
import pocopoco_vplay.board.model.mapper.BoardMapper;
import pocopoco_vplay.board.model.vo.Content;
import pocopoco_vplay.board.model.vo.Files;
import pocopoco_vplay.board.model.vo.Reply;
import pocopoco_vplay.common.model.vo.PageInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import pocopoco_vplay.board.model.vo.Content;

@Service
@RequiredArgsConstructor
public class BoardService {

	@Value("${cloud.aws.s3.bucket-url}")
	private String bucketBaseUrl;

	private final BoardMapper mapper;

	public ArrayList<Content> selectCategory(String menu, int userNo, String sort) {
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

	public int throwBoardTrash(HashMap<String, Integer> map) {
		return mapper.throwBoardTrash(map);
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

	public int getrequestPostCount(HashMap<String, String> map) {
		return mapper.getrequestPostCount(map);
	}

	public ArrayList<Content> selectAllRequestPost(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

		return mapper.selectAllRequestPost(map, rowBounds);
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


	public ArrayList<Content> menuCategoryList(int menuNo) {
		return mapper.menuCategoryList(menuNo);
	}

	public int insertContent(Content content) {
		return mapper.insertContent(content);
	}

	public int insertContentCategory(ArrayList<Integer> categoryNo, int contentNo) {
		return mapper.insertContentCategory(categoryNo, contentNo);
	}

	public int insertThumbnailFile(String tFileUrl, int contentNo, String tFileOriginalName) {
		return mapper.insertThumbnailFile(tFileUrl, contentNo, tFileOriginalName);
	}

	public int insertContentFile(String cFileUrl, int contentNo, String cFileOriginalName) {
		return mapper.insertContentFile(cFileUrl, contentNo, cFileOriginalName);
	}

	public int updateRecommendation(HashMap<String, String> map) {
		return mapper.updateRecommendation(map);
	}


	public int getMdRecommendationCount() {
		return mapper.getMdRecommendationCount();
	}

	public ArrayList<Content> selectMdList() {
		return mapper.selectMdList();
	}

	public ArrayList<Files> contentFile(int contentNo) {
		return mapper.contentFile(contentNo);
	}

	public int checkDownload(int contentNo, int userNo) {
		return mapper.checkDownload(contentNo, userNo);
	}

	public int downloadRecord(int contentNo, int userNo) {
		return mapper.downloadRecord(contentNo, userNo);
	}

	public int updateContent(Content content) {
		return mapper.updateContent(content);
	}

	public ArrayList<Files> selectFiles(int contentNo) {
		return mapper.selectFiles(contentNo);
	}

	public void updateTFile(String newTFileURL, String tFileOriginalName, int contentNo) {
		mapper.updateTFile(newTFileURL, tFileOriginalName, contentNo);
	}

	public void updateCFile(String newCFileURL, String cFileOriginalName, int contentNo) {
		mapper.updateCFile(newCFileURL, cFileOriginalName, contentNo);
	}

	public int deleteContentCategory(int contentNo) {
		return mapper.deleteContentCategory(contentNo);
	}


	public ArrayList<Content> selectDownloadsCategorySort(String menu, int userNo, String sort) {
		return mapper.selectDownloadsCategorySort(menu, userNo, sort);
	}

	/**
	 * MD 추천 비디오 목록을 가져와서,
	 * 완전한 비디오 URL을 포함한 DTO 리스트로 변환하는 메소드
	 */
	public List<VideoResponseDto> findRecommendedVideos() {

		// 1. Mapper 호출: DB에서 CONTENT와 FILES 테이블을 JOIN한 추천 콘텐츠 목록을 가져온다.
		// (이 시점에서 각 Content 객체에는 'storedFileName' 필드에 파일명 정보가 채워져 있음)
		ArrayList<Content> contentList = mapper.selectRecommendedVideos();

		// 2. DTO로 변환: 가져온 Content 리스트를 스트림으로 변환하여
		//    각각의 Content 객체를 VideoResponseDto로 변환한다.
		//    이 과정에서 '전체 비디오 URL'이 만들어진다.
		return contentList.stream()
				.map(content -> new VideoResponseDto(content, bucketBaseUrl))
				.collect(Collectors.toList());
	}

	public Files selectThumbnailFile(int contentNo) {
		return mapper.selectThumbnailFile(contentNo);
	}

	public String getBucketBaseUrl() {
		return this.bucketBaseUrl;
	}
}
