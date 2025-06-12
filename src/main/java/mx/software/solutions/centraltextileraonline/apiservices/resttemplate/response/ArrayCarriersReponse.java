package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ArrayCarriersReponse {
	
	private List<RateResponse> UPS;
	private List<RateResponse> Estafeta;
	private List<RateResponse> Redpack;
	private List<RateResponse> Tresguerras;
	private List<RateResponse> FedEx;
	private List<RateResponse> Ampm;
	private List<RateResponse> Paquetexpress;
	private List<RateResponse> Sendex;
	@JsonProperty("99Minutos")
	private List<RateResponse> Minutos99;
	private List<RateResponse> Borzo;
	private List<RateResponse> Treggo;
	private List<RateResponse> JT;
	private List<RateResponse> Uber;
	private List<RateResponse> iVoy;

}
