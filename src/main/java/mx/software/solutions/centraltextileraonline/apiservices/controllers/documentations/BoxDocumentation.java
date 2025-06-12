package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Box", description = "Endpoints to manage the box show in Central Textilera Ecommerce.")
public interface BoxDocumentation {

	@Operation(summary = "Create box", description = "Create box of Central Textilera Ecommerce.")
	CrudResponse<BoxResponse> createBox(BoxCreateRequest boxCreateRequest) throws DataBaseException, ExistException;

	@Operation(summary = "Update box", description = "Update box of Central Textilera Ecommerce.")
	CrudResponse<BoxResponse> updateBox(BoxUpdateRequest boxUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate box", description = "Reactivate box of Central Textilera Ecommerce.")
	CrudResponse<BoxResponse> reactivateBox(UUID box) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete box", description = "Delete box of Central Textilera Ecommerce.")
	CrudResponse<BoxResponse> deleteBox(UUID box) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all boxes", description = "Get all boxes of Central Textilera Ecommerce.")
	GetResponse<BoxResponse> getAllBox(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get box history", description = "Get box history of Central Textilera Ecommerce.")
	GetResponse<BoxHistoryResponse> getBoxHistory(UUID box, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
