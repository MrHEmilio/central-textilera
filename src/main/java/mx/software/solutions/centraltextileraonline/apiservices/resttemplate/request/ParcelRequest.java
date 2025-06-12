package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ParcelRequest {

	private BigDecimal length;
	private BigDecimal height;
	private BigDecimal width;
	private BigDecimal weight;
	@JsonProperty("distance_unit")
	private String distanceUnit;
	@JsonProperty("mass_unit")
	private String massUnit;

}
