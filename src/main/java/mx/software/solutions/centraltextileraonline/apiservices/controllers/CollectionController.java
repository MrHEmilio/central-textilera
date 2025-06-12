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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.CollectionDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
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
import mx.software.solutions.centraltextileraonline.apiservices.service.CollectionService;

@RestController
@RequestMapping("/collection")
public class CollectionController implements CollectionDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private CollectionService collectionService;

	@Override
	@PostMapping
	public CrudResponse<CollectionResponse> createCollection(@Valid @RequestBody final CollectionCreateRequest collectionCreateRequest) throws DataBaseException, ExistException, ImageInvalidException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.collectionService.createCollection(collectionCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLLECTION, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<CollectionResponse> updateCollection(@Valid @RequestBody final CollectionUpdateRequest collectionUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.collectionService.updateCollection(collectionUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLLECTION, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@PutMapping("/{collection}")
	public CrudResponse<CollectionResponse> reactivateCollection(@PathVariable final UUID collection) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.collectionService.reactivateCollection(collection, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLLECTION, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{collection}")
	public CrudResponse<CollectionResponse> deleteCollection(@PathVariable final UUID collection) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.collectionService.deleteCollection(collection, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLLECTION, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<CollectionResponse> getAllCollection(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.collectionService.getAllCollection(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{collection}/history")
	public GetResponse<CollectionHistoryResponse> getCollectionHistory(@PathVariable final UUID collection, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.collectionService.getCollectionHistory(collection, paginationRequest);
	}

}
