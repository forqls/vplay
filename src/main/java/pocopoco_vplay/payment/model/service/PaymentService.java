package pocopoco_vplay.payment.model.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.payment.model.mapper.PaymentMapper;
import pocopoco_vplay.payment.model.vo.Payment;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentMapper mapper;
	
	public int insertPaymentInfo(Payment payment) {
		return mapper.insertPaymentInfo(payment);
	}

}
