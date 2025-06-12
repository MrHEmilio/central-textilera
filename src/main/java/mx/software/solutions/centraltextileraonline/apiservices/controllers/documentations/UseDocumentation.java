package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Use", description = "Endpoints to manage the use show in Central Textilera Ecommerce.")
public interface UseDocumentation {

	@Operation(summary = "Create use", description = "Create use of Central Textilera Ecommerce.")
	CrudResponse<UseResponse> createUse(UseCreateRequest useCreateRequest) throws DataBaseException, ExistException;

	@Operation(summary = "Update use", description = "Update use of Central Textilera Ecommerce.")
	CrudResponse<UseResponse> updateUse(UseUpdateRequest useUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate use", description = "Reactivate use of Central Textilera Ecommerce.")
	CrudResponse<UseResponse> reactivateUse(UUID use) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Delete use", description = "Delete use of Central Textilera Ecommerce.")
	CrudResponse<UseResponse> deleteUse(UUID use) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all uses", description = "Get all uses of Central Textilera Ecommerce.")
	GetResponse<UseResponse> getAllUse(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get use history", description = "Get use history of Central Textilera Ecommerce.")
	GetResponse<UseHistoryResponse> getUseHistory(UUID use, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
