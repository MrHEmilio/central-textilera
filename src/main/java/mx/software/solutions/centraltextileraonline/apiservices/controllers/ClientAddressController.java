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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.ClientAddressDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientAddressResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientAddressService;

@RestController
@RequestMapping("/client/address")
public class ClientAddressController implements ClientAddressDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private ClientAddressService clientAddressService;

	@Override
	@PostMapping
	public CrudResponse<ClientAddressResponse> createClientAddress(@Valid @RequestBody final ClientAddressCreateRequest clientAddresCreateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clientAddressResponse = this.clientAddressService.createClientAddress(clientAddresCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT_ADDRESS, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(clientAddressResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<ClientAddressResponse> updateClientAddress(@Valid @RequestBody final ClientAddressUpdateRequest clientAddressUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clientAddressResponse = this.clientAddressService.updateClientAddress(clientAddressUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT_ADDRESS, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(clientAddressResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{clientAddress}")
	public CrudResponse<ClientAddressResponse> deleteClientAddress(@PathVariable final UUID clientAddress) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clientAddressResponse = this.clientAddressService.deleteClientAddress(clientAddress, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_CLIENT_ADDRESS, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(clientAddressResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<ClientAddressResponse> getClientAddress(@Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		return this.clientAddressService.getClientAddress(sessionDto.getIdUser(), paginationRequest);
	}

	@Override
	@GetMapping("/{client}")
	public GetResponse<ClientAddressResponse> getClientAddress(@PathVariable final UUID client, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.clientAddressService.getClientAddress(client, paginationRequest);
	}
}
