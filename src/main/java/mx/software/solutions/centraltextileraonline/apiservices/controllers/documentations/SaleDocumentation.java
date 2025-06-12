package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Sale", description = "Endpoints to manage the sale show in Central Textilera Ecommerce.")
public interface SaleDocumentation {

	@Operation(summary = "Create sale", description = "Create sale of Central Textilera Ecommerce.")
	CrudResponse<SaleResponse> createSale(SaleCreateRequest saleCreateRequest) throws DataBaseException, ExistException;

	@Operation(summary = "Update sale", description = "Update sale of Central Textilera Ecommerce.")
	CrudResponse<SaleResponse> updateSale(SaleUpdateRequest saleUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate sale", description = "Reactivate sale of Central Textilera Ecommerce.")
	CrudResponse<SaleResponse> reactivateSale(UUID sale) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete sale", description = "Delete sale of Central Textilera Ecommerce.")
	CrudResponse<SaleResponse> deleteSale(UUID sale) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all sales", description = "Get all sales of Central Textilera Ecommerce.")
	GetResponse<SaleResponse> getAllSale(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get sale history", description = "Get sale history of Central Textilera Ecommerce.")
	GetResponse<SaleHistoryResponse> getSaleHistory(UUID sale, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
