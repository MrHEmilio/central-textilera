package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;

@Data
public class SendEmailOrderCreatedRequest {

	private UUID id;

	private String email;

	private String clientName;

	private String numberOrder;

	private String statusOrder;

	private PaymentMethod paymentMethod;

	private DeliveryMethod deliveryMethod;

	private BigDecimal subtotal;

	private BigDecimal shippingTotal;

	private BigDecimal iva;

	private BigDecimal total;

	private String shippingName;

	private String productsName;

	private String productsDetail;

	private String productsAmount;

	private String trackingCode;

	private String priceUnit;// Añadí esta linea


}
