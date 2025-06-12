package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Fiber", description = "Endpoints to manage the fiber show in Central Textilera Ecommerce.")
public interface FiberDocumentation {

	@Operation(summary = "Create fiber", description = "Create fiber of Central Textilera Ecommerce.")
	CrudResponse<FiberResponse> createFiber(FiberCreateRequest fiberCreateRequest) throws DataBaseException, ExistException;

	@Operation(summary = "Update fiber", description = "Update fiber of Central Textilera Ecommerce.")
	CrudResponse<FiberResponse> updateFiber(FiberUpdateRequest fiberUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate fiber", description = "Reactivate fiber of Central Textilera Ecommerce.")
	CrudResponse<FiberResponse> reactivateFiber(UUID fiber) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete fiber", description = "Delete fiber of Central Textilera Ecommerce.")
	CrudResponse<FiberResponse> deleteFiber(UUID fiber) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all fibers", description = "Get all fibers of Central Textilera Ecommerce.")
	GetResponse<FiberResponse> getAllFiber(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get fiber history", description = "Get fiber history of Central Textilera Ecommerce.")
	GetResponse<FiberHistoryResponse> getFiberHistory(@PathVariable final UUID fiber, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
