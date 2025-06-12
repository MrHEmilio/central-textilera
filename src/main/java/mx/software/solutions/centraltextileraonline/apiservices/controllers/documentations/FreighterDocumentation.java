package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Freighter", description = "Endpoints to manage the freighter show in Central Textilera Ecommerce.")
public interface FreighterDocumentation {

	@Operation(summary = "Create freighter", description = "Create freighter of Central Textilera Ecommerce.")
	CrudResponse<FreighterResponse> createFreighter(FreighterCreateRequest freighterCreateRequest) throws DataBaseException, ExistException, ImageInvalidException;

	@Operation(summary = "Update freighter", description = "Update freighter of Central Textilera Ecommerce.")
	CrudResponse<FreighterResponse> updateFreighter(FreighterUpdateRequest freighterUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate freighter", description = "Reactivate freighter of Central Textilera Ecommerce.")
	CrudResponse<FreighterResponse> reactivateFreighter(UUID freighter) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete freighter", description = "Delete freighter of Central Textilera Ecommerce.")
	CrudResponse<FreighterResponse> deleteFreighter(UUID freighter) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all freighter", description = "Get all freighter of Central Textilera Ecommerce.")
	GetResponse<FreighterResponse> getAllFreighter(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get freighter history", description = "Get freighter history of Central Textilera Ecommerce.")
	GetResponse<FreighterHistoryResponse> getFreighterHistory(UUID freighter, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
