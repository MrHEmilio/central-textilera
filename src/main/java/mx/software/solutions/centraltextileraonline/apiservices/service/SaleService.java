package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface SaleService {

	SaleResponse createSale(SaleCreateRequest saleCreateRequest, UUID admin) throws DataBaseException, ExistException;

	SaleResponse updateSale(SaleUpdateRequest saleUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	SaleResponse reactivateSale(UUID sale, UUID admin) throws DataBaseException, NotFoundException;

	SaleResponse deleteSale(UUID sale, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<SaleResponse> getAllSale(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<SaleHistoryResponse> getSaleHistory(UUID sale, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
