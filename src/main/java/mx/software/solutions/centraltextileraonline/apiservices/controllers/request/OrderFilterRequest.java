package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;

@Data
public class OrderFilterRequest {

	private String search;
	private OrderStatus orderStatus;
	private DeliveryMethod deliveryMethod;
	private PaymentMethod paymentMethod;

}
