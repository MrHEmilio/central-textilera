package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BillingCreateWithoutAccountRequest {

	@NotNull(message = "billing.order.not.null")
	private UUID order;

	@NotNull(message = "billing.cfdi.use.not.null")
	private String cfdiUse;

	@NotNull(message = "billing.rfc.not.null")
	private String rfc;

	@NotNull(message = "billing.company.name.not.null")
	private String companyName;

	@NotNull(message = "billing.fiscal.regimen.not.null")
	private String fiscalRegimen;

	@Valid
	@NotNull(message = "billing.address.not.null")
	private ClientAddressCreateWithoutAccountRequest billingAddress;

}
