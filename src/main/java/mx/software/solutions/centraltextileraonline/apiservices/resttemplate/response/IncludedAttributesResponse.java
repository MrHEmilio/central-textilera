package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IncludedAttributesResponse {

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("amount_local")
	private BigDecimal amountLocal;

	@JsonProperty("currency_local")
	private String currencyLocal;

	private String provider;

	@JsonProperty("service_level_name")
	private String serviceLevelName;

	@JsonProperty("service_level_code")
	private String serviceLevelCode;

	private int days;

	@JsonProperty("ferri_price")
	private BigDecimal ferriPrice;

	@JsonProperty("insurance_fee")
	private BigDecimal insuranceFee;

	@JsonProperty("out_of_area_service")
	private boolean outOfAreaService;

	@JsonProperty("out_of_area_pricing")
	private BigDecimal outOfAreaPricing;

	@JsonProperty("total_pricing")
	private BigDecimal totalPricing;

	@JsonProperty("is_ocurre")
	private boolean isOcurre;

	@JsonProperty("extra_dimension_price")
	private BigDecimal extraDimensionPrice;

}
