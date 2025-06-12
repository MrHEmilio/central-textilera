package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.ClientDocumentation;
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
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientService;
import mx.software.solutions.centraltextileraonline.apiservices.service.TokenService;

@RestController
@RequestMapping("/client")
public class ClientController implements ClientDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TokenService tokenService;

	@Override
	@PostMapping
	public CrudResponse<ClientResponse> createClient(@Valid @RequestBody final ClientCreateRequest clientCreateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var clientResponse = this.clientService.createClient(clientCreateRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(clientResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<ClientResponse> updateClient(@Valid @RequestBody final ClientUpdateRequest clientUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clientResponse = this.clientService.updateClient(clientUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(clientResponse, messageResponse);
	}

	@Override
	@DeleteMapping
	public CrudResponse<ClientResponse> deleteClient() throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clientResponse = this.clientService.deleteClient(sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(clientResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{client}")
	public CrudResponse<ClientResponse> deleteClientById(@PathVariable final UUID client) throws DataBaseException, NotFoundException {
		final var clientResponse = this.clientService.deleteClient(client);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(clientResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<ClientResponse> getAllClient(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.clientService.getAllClient(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/info")
	public CredentialInfoResponse<ClientResponse> getInfoClient() throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		return this.clientService.getInfoClient(sessionDto.getIdUser());
	}

	@Override
	@GetMapping("/info/{client}")
	public CredentialInfoResponse<ClientResponse> getInfoClient(@PathVariable final UUID client) throws DataBaseException, NotFoundException {
		return this.clientService.getInfoClient(client);
	}

	@Override
	@PutMapping("/verify/{token}")
	public TokenActionResponse verifyClient(@PathVariable final String token) throws DataBaseException, NotFoundException, TokenInvalidException {
		final var tokenInfoResponse = this.tokenService.validateToken(token);
		this.clientService.verifyClient(tokenInfoResponse.getEmail());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT, MessageLangType.RESPONSE, "verify.client");
		return new TokenActionResponse(messageResponse);
	}

	@Override
	@PutMapping("/reactive/{token}")
	public TokenActionResponse reactiveClient(@PathVariable final String token) throws DataBaseException, NotFoundException, TokenInvalidException {
		final var tokenInfoResponse = this.tokenService.validateToken(token);
		this.clientService.reactiveClient(tokenInfoResponse.getEmail());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT, MessageLangType.RESPONSE, "reactive.token");
		return new TokenActionResponse(messageResponse);
	}

	@Override
	@GetMapping("/verify/{email}")
	public ClientVerifyDataResponse verifyData(@PathVariable final String email, final ClientVerifyDataRequest clientVerifyDataRequest) throws DataBaseException, NotFoundException {
		return this.clientService.verifyData(email, clientVerifyDataRequest);
	}

}
