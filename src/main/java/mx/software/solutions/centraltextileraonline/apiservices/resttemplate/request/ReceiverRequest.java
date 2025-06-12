package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReceiverRequest {

	@JsonProperty("Rfc")
	private String rfc;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("FiscalRegime")
	private String fiscalRegime;

	@JsonProperty("CfdiUse")
	private String cfdiUse;

	@JsonProperty("TaxZipCode")
	private String taxZipCode;

}
