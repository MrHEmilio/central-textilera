package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateCfdiRequest {

	@JsonProperty("NameId")
	private int nameId;

	@JsonProperty("Date")
	private String date;

	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("ExpeditionPlace")
	private String expeditionPlace;

	@JsonProperty("Folio")
	private String folio;

	@JsonProperty("CfdiType")
	private String cfdiType;

	@JsonProperty("PaymentForm")
	private String paymentForm;

	@JsonProperty("PaymentMethod")
	private String paymentMethod;

	@JsonProperty("Receiver")
	private ReceiverRequest receiver;

	@JsonProperty("Items")
	private List<ItemRequest> items;

}
