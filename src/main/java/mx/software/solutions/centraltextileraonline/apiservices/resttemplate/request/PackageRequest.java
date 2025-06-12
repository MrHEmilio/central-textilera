package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PackageRequest {
	
	private Integer quantity;
	private BigDecimal weight;
	@JsonProperty("weight_unit")
	private String weightUnit;
	private BigDecimal length;
	private BigDecimal height;
	private BigDecimal width;
	@JsonProperty("dimension_unit")
	private String dimensionUnit;
	
	

}
