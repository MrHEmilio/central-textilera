package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class OrderResponse {
	private final UUID id;
	private final ClientResponse client;
	private final int number;
	private final BigDecimal total;
	private final List<OrderProductResponse> products;
	private final String deliveryMethod;
	private final PaymentMethod paymentMethod;
	private final List<OrderStatusResponse> statusHistory;
	private final OrderShippingResponse orderShipping;
	private final OrderBillingResponse orderBilling;

}
