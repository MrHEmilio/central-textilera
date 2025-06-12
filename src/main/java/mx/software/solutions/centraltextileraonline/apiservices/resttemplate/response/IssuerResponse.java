package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IssuerResponse {

	@JsonProperty("FiscalRegime")
	private String fiscalRegime;

	@JsonProperty("Rfc")
	private String rfc;

	@JsonProperty("TaxName")
	private String taxName;

}
