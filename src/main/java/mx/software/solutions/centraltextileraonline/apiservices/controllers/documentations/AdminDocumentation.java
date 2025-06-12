package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.AdminResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.EmailExistWithoutPasswordException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Admin", description = "Endpoints to manage the administrators of the Central Textilera Ecommerce.")
public interface AdminDocumentation {

	@Operation(summary = "Create Administrator", description = "Create Administrator of Central Textilera Ecommerce.")
	CrudResponse<AdminResponse> createAdmin(AdminCreateRequest adminCreateRequest) throws DataBaseException, ExistException, EmailExistWithoutPasswordException;

	@Operation(summary = "Update Administrator", description = "Update Administrator from Admin of Central Textilera Ecommerce")
	CrudResponse<AdminResponse> updateAdmin(AdminUpdateRequest adminUpdateRequest, UUID admin) throws DataBaseException, NotFoundException;

	@Operation(summary = "Update Administrator", description = "Update Administrator from Profile of Central Textilera Ecommerce")
	CrudResponse<AdminResponse> updateAdmin(AdminUpdateRequest adminUpdateRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete Administrator", description = "Delete Administrator of Central Textilera Ecommerce.")
	CrudResponse<AdminResponse> deleteAdmin(UUID admin) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all Administrators", description = "Get all admins registry in the Central Textilera Ecommerce")
	GetResponse<AdminResponse> getAllAdmin(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get info about administrator to login", description = "Get info about admin when user logged in the app")
	CredentialInfoResponse<AdminResponse> getInfoAdmin() throws DataBaseException, NotFoundException;

	@Operation(summary = "Get info about administrator", description = "Get info about admin by id")
	CredentialInfoResponse<AdminResponse> getInfoAdmin(UUID admin) throws DataBaseException, NotFoundException;

}
