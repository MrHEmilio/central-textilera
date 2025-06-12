package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ItemRequest {

	@JsonProperty("ProductCode")
	private String productCode;

	@JsonProperty("IdentificationNumber")
	private String identificationNumber;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("Unit")
	private String unit;

	@JsonProperty("UnitCode")
	private String unitCode;

	@JsonProperty("UnitPrice")
	private BigDecimal unitPrice;

	@JsonProperty("Quantity")
	private BigDecimal quantity;

	@JsonProperty("Subtotal")
	private BigDecimal subtotal;

	@JsonProperty("TaxObject")
	private String taxObject;

	@JsonProperty("Taxes")
	private List<TaxesRequest> taxes;

	@JsonProperty("Total")
	private BigDecimal total;

}
