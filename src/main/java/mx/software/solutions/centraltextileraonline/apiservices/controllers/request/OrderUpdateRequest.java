package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;

@Data
public class OrderUpdateRequest {

	@NotNull(message = "order.id.not.null")
	private UUID order;
	@NotNull(message = "order.status.not.null")
	private OrderStatus orderStatus;

}
