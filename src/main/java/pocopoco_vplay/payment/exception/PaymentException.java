package pocopoco_vplay.payment.exception;

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
public class PaymentException {
	private int paymentNo;
	private Date paymentTimestamp;
	private int paymentAmount;
	private String paymentStatus;
	private int userNo;
}
