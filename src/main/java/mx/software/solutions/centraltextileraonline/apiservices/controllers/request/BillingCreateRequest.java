package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BillingCreateRequest {

	@NotNull(message = "billing.order.not.null")
	private UUID order;

	@NotNull(message = "billing.cfdi.use.not.null")
	private String cfdiUse;

}
