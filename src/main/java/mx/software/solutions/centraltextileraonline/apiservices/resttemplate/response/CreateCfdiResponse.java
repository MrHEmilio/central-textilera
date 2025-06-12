package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateCfdiResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("CfdiType")
	private String cfdiType;

	@JsonProperty("Serie")
	private String serie;

	@JsonProperty("Folio")
	private String folio;

	@JsonProperty("Date")
	private Date date;

	@JsonProperty("PaymentTerms")
	private String paymentTerms;

	@JsonProperty("PaymentConditions")
	private String paymentConditions;

	@JsonProperty("PaymentMethod")
	private String paymentMethod;

	@JsonProperty("ExpeditionPlace")
	private String expeditionPlace;

	@JsonProperty("ExchangeRate")
	private String exchangeRate;

	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("Subtotal")
	private BigDecimal subtotal;

	@JsonProperty("Discount")
	private BigDecimal discount;

	@JsonProperty("Total")
	private BigDecimal total;

	@JsonProperty("Observations")
	private String observations;

	@JsonProperty("Issuer")
	private IssuerResponse issuer;

	@JsonProperty("Receiver")
	private ReceiverResponse receiver;

	@JsonProperty("Items")
	private List<ItemsResponse> items;

	@JsonProperty("Taxes")
	private List<TaxesResponse> taxes;

	@JsonProperty("Complement")
	private ComplementResponse complement;

}
