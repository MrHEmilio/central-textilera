package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientVerifyDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientVerifyDataResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface ClientService {

	ClientResponse createClient(ClientCreateRequest clientCreateRequest) throws DataBaseException, NotFoundException, ExistException;

	ClientResponse updateClient(ClientUpdateRequest clientUpdateRequest, UUID client) throws DataBaseException, NotFoundException, ExistException;

	ClientResponse deleteClient(UUID client) throws DataBaseException, NotFoundException;

	GetResponse<ClientResponse> getAllClient(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	ClientResponse getClientByEmail(String email) throws DataBaseException, NotFoundException;

	CredentialInfoResponse<ClientResponse> getInfoClient(UUID client) throws DataBaseException, NotFoundException;

	void verifyClient(String email) throws DataBaseException, NotFoundException;

	void reactiveClient(String email) throws DataBaseException, NotFoundException;

	ClientVerifyDataResponse verifyData(String email, ClientVerifyDataRequest clientVerifyDataRequest) throws DataBaseException, NotFoundException;

}
