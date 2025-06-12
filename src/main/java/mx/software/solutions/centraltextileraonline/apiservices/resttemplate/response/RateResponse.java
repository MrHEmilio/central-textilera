package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RateResponse {
	
	@JsonProperty("rate_id")
	private String rateId;
	@JsonProperty("shipment_id")
	private String  shipmentId;
	@JsonProperty("dynamic_service_name")
	private String dynamicServiceName;
	private String carrier;
	@JsonProperty("carrier_service_name")
	private String carrierServiceName;
	@JsonProperty("carrier_service_code")
	private String carrierServiceCode;
	@JsonProperty("carrier_logo_url")
	private String carrierLogoUrl;
	@JsonProperty("carrier_logo_url_svg")
	private String carrierLogoUrlSvg;
	@JsonProperty("estimated_delivery")
	private String estimatedDelivery;
	@JsonProperty("est_transit_time_hours")
	private String estTransitTimeHours;
	@JsonProperty("net_shipping_amount")
	private BigDecimal netShippingAmount;
	@JsonProperty("net_surcharges_amount")
	private BigDecimal netSurchargesAmount;
	@JsonProperty("net_total_amount")
	private BigDecimal netTotalAmount;
	@JsonProperty("vat_amount")
	private BigDecimal vatAmount;
	@JsonProperty("vat_rate")
	private BigDecimal vatRate;
	@JsonProperty("total_amount")
	private BigDecimal totalAmount;
	private String currency;
	@JsonProperty("list_total_amount")
	private BigDecimal listTotalAmount;
	@JsonProperty("list_net_amount")
	private BigDecimal listNetAmount;
	@JsonProperty("list_vat_amount")
	private BigDecimal listVatAmount;
	@JsonProperty("list_total_amount_currency")
	private String listTotalAmountCurrency;
	@JsonProperty("service_terms")
	private String serviceTerms;
	@JsonProperty("enviaya_service_name")
	private String serviceName;
	@JsonProperty("enviaya_service_code")
	private String ServiceCode;
	
}
