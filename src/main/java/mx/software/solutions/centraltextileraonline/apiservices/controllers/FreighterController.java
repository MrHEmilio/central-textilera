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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.FreighterDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.FreighterService;

@RestController
@RequestMapping("/freighter")
public class FreighterController implements FreighterDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private FreighterService freighterService;

	@Override
	@PostMapping
	public CrudResponse<FreighterResponse> createFreighter(@Valid @RequestBody final FreighterCreateRequest freighterCreateRequest) throws DataBaseException, ExistException, ImageInvalidException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var freighterResponse = this.freighterService.createFreighter(freighterCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FREIGHTER, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(freighterResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<FreighterResponse> updateFreighter(@Valid @RequestBody final FreighterUpdateRequest freighterUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var freighterResponse = this.freighterService.updateFreighter(freighterUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FREIGHTER, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(freighterResponse, messageResponse);
	}

	@Override
	@PutMapping("/{freighter}")
	public CrudResponse<FreighterResponse> reactivateFreighter(@PathVariable final UUID freighter) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var freighterResponse = this.freighterService.reactivateFreighter(freighter, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FREIGHTER, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(freighterResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{freighter}")
	public CrudResponse<FreighterResponse> deleteFreighter(@PathVariable final UUID freighter) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var freighterResponse = this.freighterService.deleteFreighter(freighter, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FREIGHTER, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(freighterResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<FreighterResponse> getAllFreighter(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.freighterService.getAllFreighter(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{freighter}/history")
	public GetResponse<FreighterHistoryResponse> getFreighterHistory(@PathVariable final UUID freighter, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.freighterService.getFreighterHistory(freighter, paginationRequest);
	}

}
