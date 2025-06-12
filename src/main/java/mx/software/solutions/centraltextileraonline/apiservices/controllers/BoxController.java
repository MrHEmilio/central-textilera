package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.List;
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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.BoxDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculateBoxRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxCalculateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxResponse;
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
import mx.software.solutions.centraltextileraonline.apiservices.service.BoxService;

@RestController
@RequestMapping("/box")
public class BoxController implements BoxDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private BoxService boxService;

	@Override
	@PostMapping
	public CrudResponse<BoxResponse> createBox(@Valid @RequestBody final BoxCreateRequest boxCreateRequest) throws DataBaseException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var boxResponse = this.boxService.createBox(boxCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BOX, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(boxResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<BoxResponse> updateBox(@Valid @RequestBody final BoxUpdateRequest boxUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var boxResponse = this.boxService.updateBox(boxUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BOX, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(boxResponse, messageResponse);
	}

	@Override
	@PutMapping("/{box}")
	public CrudResponse<BoxResponse> reactivateBox(@PathVariable final UUID box) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var boxResponse = this.boxService.reactivateBox(box, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BOX, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(boxResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{box}")
	public CrudResponse<BoxResponse> deleteBox(@PathVariable final UUID box) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var boxResponse = this.boxService.deleteBox(box, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BOX, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(boxResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<BoxResponse> getAllBox(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.boxService.getAllBox(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{box}/history")
	public GetResponse<BoxHistoryResponse> getBoxHistory(@PathVariable final UUID box, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.boxService.getBoxHistory(box, paginationRequest);
	}

	@PostMapping("/calculate")
	public List<BoxCalculateResponse> calculateBox(@Valid @RequestBody final CalculateBoxRequest calculateBoxRequest) throws DataBaseException, NotFoundException {
		return this.boxService.calculateBox(calculateBoxRequest);
	}
}
