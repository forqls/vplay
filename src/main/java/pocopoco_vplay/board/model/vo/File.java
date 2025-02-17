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

public class File {
	private int fileNo;
	private String fileName;
	private String fileReName;
	private String thumbnail;
	private String fileLocation;
	private String fileStatus;
}
