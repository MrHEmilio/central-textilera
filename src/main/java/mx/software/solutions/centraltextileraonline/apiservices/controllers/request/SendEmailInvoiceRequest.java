package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SendEmailInvoiceRequest {

	@NotNull(message = "billing.order.not.null")
	private UUID order;

}
