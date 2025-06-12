package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientAddressResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface ClientAddressService {

	ClientAddressResponse createClientAddress(ClientAddressCreateRequest clientAddresCreateRequest, UUID client) throws DataBaseException, NotFoundException, ExistException;

	ClientAddressResponse updateClientAddress(ClientAddressUpdateRequest clientAddressUpdateRequest, UUID client) throws DataBaseException, NotFoundException, ExistException;

	ClientAddressResponse deleteClientAddress(UUID clientAddress, UUID client) throws DataBaseException, NotFoundException;

	GetResponse<ClientAddressResponse> getClientAddress(UUID client, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;
}
