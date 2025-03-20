package pocopoco_vplay.users.model.vo;

import java.time.LocalDateTime;

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
public class Message {
	private int messageNo;
	private int senderNo;
	private int receiverNo;
	private String content;
	private LocalDateTime sentTime;
	private String readStatus;
	private String conversationId;
	private String userName;
	private String timeout;
}
