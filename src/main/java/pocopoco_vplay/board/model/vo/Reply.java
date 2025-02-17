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
public class Reply {
	private int replyNo;
	private String writer;
	private String replyDetail;
	private Date createDate;
	private Date modifyDate;
	private String replyStatus;
	private String contentNo;
}
