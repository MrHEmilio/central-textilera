package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientVerifyDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientVerifyDataResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TokenActionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;

@Tag(name = "Client", description = "Endpoints to manage the client for Central Textilera Ecommerce.")
public interface ClientDocumentation {

	@Operation(summary = "Create client", description = "Create client of Central Textilera Ecommerce.")
	CrudResponse<ClientResponse> createClient(ClientCreateRequest clientCreateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Update client", description = "Update client of Central Textilera Ecommerce.")
	CrudResponse<ClientResponse> updateClient(ClientUpdateRequest clientUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Delete client", description = "Delete client of Central Textilera Ecommerce.")
	CrudResponse<ClientResponse> deleteClient() throws DataBaseException, NotFoundException;

	@Operation(summary = "Create client by ID", description = "Create client by ID of Central Textilera Ecommerce.")
	CrudResponse<ClientResponse> deleteClientById(UUID client) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all client", description = "Get all client registered in Central Textilera Ecommerce.")
	GetResponse<ClientResponse> getAllClient(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get info about client to login", description = "Get info about client when user logged in the app.")
	CredentialInfoResponse<ClientResponse> getInfoClient() throws DataBaseException, NotFoundException;

	@Operation(summary = "Get info about client", description = "Get info about client by id")
	CredentialInfoResponse<ClientResponse> getInfoClient(UUID client) throws DataBaseException, NotFoundException;

	@Operation(summary = "Verify client email", description = "Verify email for token")
	TokenActionResponse verifyClient(String token) throws DataBaseException, NotFoundException, TokenInvalidException;

	@Operation(summary = "Reactive client account", description = "Reactive client account")
	TokenActionResponse reactiveClient(String token) throws DataBaseException, NotFoundException, TokenInvalidException;

	@Operation(summary = "Verify client data", description = "Verify client data")
	ClientVerifyDataResponse verifyData(String email, ClientVerifyDataRequest clientVerifyDataRequest) throws DataBaseException, NotFoundException;

}
