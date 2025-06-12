package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ItemsResponse {

	@JsonProperty("Discount")
	private BigDecimal discount;

	@JsonProperty("Quantity")
	private BigDecimal quantity;

	@JsonProperty("Unit")
	private String unit;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("UnitValue")
	private BigDecimal unitValue;

	@JsonProperty("Total")
	private BigDecimal total;

}
