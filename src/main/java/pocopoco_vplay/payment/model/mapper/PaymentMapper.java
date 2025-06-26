package pocopoco_vplay.payment.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import pocopoco_vplay.payment.model.vo.Payment;

@Mapper
public interface PaymentMapper {

	int insertPaymentInfo(Payment payment);

}
