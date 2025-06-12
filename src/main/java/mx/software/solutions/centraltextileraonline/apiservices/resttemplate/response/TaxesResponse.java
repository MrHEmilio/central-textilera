package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TaxesResponse {

	@JsonProperty("Total")
	private BigDecimal total;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Rate")
	private BigDecimal rate;

	@JsonProperty("Type")
	private String type;

}
