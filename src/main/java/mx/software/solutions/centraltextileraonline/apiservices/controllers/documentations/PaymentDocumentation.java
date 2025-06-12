package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculateInventoryProductRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceProductRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CheckoutMercadoPagoCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CalculatePriceResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CheckoutResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.CheckoutException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.InventoryException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;


@Tag(name = "Payment", description = "Endpoints to manage the payments realized in Central Textilera Ecommerce.")
public interface PaymentDocumentation {

	@Operation(summary = "Calculate Price Product", description = "Calculate price of total cart of Central Textilera Ecommerce.")
	List<CalculatePriceResponse> calculatePriceProduct(CalculatePriceProductRequest calculatePriceProductRequest);

	@Operation(summary = "Calculate Inventory Porduct", description = "Calculate product in invenory of Central Textilera Ecommerce.")
	boolean calculateInventoryProduct(CalculateInventoryProductRequest calculateInventoryProductRequest) throws InventoryException;

	@Operation(summary = "Create the checkout to pay", description = "Create the checkout to pay of Central Textilera Ecommerce.")
	CheckoutResponse createCheckoutMercadoPago(CheckoutMercadoPagoCreateRequest checkoutMercadoPagoCreate) throws CheckoutException, DataBaseException, NotFoundException;
}
