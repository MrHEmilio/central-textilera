package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Catalog", description = "Endpoint to get catalog.")
public interface CatalogDocumentation {

	@Operation(summary = "Get country code", description = "Get country code of all country")
	GetResponse<CountryCodeResponse> getAllCountryCode(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<CatalogResponse<DeliveryMethod>> getAllDeliveryMethod();

	GetResponse<CatalogResponse<PaymentMethod>> getAllPaymentMethod();

	GetResponse<CatalogResponse<OrderStatus>> getAllOrderStatus();

}
