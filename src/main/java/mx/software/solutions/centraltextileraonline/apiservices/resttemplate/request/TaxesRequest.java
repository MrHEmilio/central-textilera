package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TaxesRequest {

	@JsonProperty("Total")
	private BigDecimal total;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Base")
	private BigDecimal base;

	@JsonProperty("Rate")
	private BigDecimal rate;

}
