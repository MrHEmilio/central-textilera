package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientAddressResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Client Address", description = "Endpoints to manage the address registred for client to send products.")
public interface ClientAddressDocumentation {

	@Operation(summary = "Create client address", description = "Create client address for send porduct in Central Textilera Ecommerce.")
	CrudResponse<ClientAddressResponse> createClientAddress(ClientAddressCreateRequest clientAddresCreateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Update client address", description = "Update client address registered in Central Textilera Ecommerce.")
	CrudResponse<ClientAddressResponse> updateClientAddress(ClientAddressUpdateRequest clientAddressUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Delete client address", description = "Delete client address registered in Central Textilera Ecommerce.")
	CrudResponse<ClientAddressResponse> deleteClientAddress(UUID clientAddress) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all client address", description = "Get all client address registered in Central Textilera Ecommerce.")
	GetResponse<ClientAddressResponse> getClientAddress(PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get client address by client", description = "Get all client address registered by client specify.")
	GetResponse<ClientAddressResponse> getClientAddress(UUID client, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
