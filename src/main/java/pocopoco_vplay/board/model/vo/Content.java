package pocopoco_vplay.board.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Content {
	private int contentNo;
	private String contentTitle;
	private String contentDetail;
	private Date createDate;
	private Date modifyDate;
	private String contentStatus;
	private String deleteStatus;
	private int userNo;
	private String userId;
	private String userNickName;
	private String categoryName;
	private String menuName;
	private String hashtagName;
	private int views;
	private int likeCount;
	private int likeTo;
	private int menuNo;
	private Reply reply;
}
