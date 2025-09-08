package pocopoco_vplay.board.model.vo;

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

public class Files {
	private int fileNo;
	private int refContentNo;
	private String fileLocation;
	private String fileStatus;
	private int fileLevel;
	private String originalFileName;
}
