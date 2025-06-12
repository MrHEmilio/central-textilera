package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.CatalogDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CatalogResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountryCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.service.CatalogService;
import mx.software.solutions.centraltextileraonline.apiservices.service.CountryCodeService;

@RestController
@RequestMapping("/catalog")
public class CatalogController implements CatalogDocumentation {

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private CountryCodeService countryCodeService;

	@Override
	@GetMapping("/country/code")
	public GetResponse<CountryCodeResponse> getAllCountryCode(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.countryCodeService.getAllCountryCode(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/delivery/method")
	public GetResponse<CatalogResponse<DeliveryMethod>> getAllDeliveryMethod() {
		return this.catalogService.getAllDeliveryMethod();
	}

	@Override
	@GetMapping("/payment/method")
	public GetResponse<CatalogResponse<PaymentMethod>> getAllPaymentMethod() {
		return this.catalogService.getAllPaymentMethod();
	}

	@Override
	@GetMapping("/order/status")
	public GetResponse<CatalogResponse<OrderStatus>> getAllOrderStatus() {
		return this.catalogService.getAllOrderStatus();
	}

}
