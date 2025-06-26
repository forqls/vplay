package pocopoco_vplay.payment.model.vo;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Payment {
	private String paymentId;
	private int amount;
	private int userNo;
	private Timestamp paymentDate;
}
