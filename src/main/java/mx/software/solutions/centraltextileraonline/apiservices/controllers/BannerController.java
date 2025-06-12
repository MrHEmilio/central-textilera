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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.BannerDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.BannerService;

@RestController
@RequestMapping("/banner")
public class BannerController implements BannerDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private BannerService bannerService;

	@Override
	@PostMapping
	public CrudResponse<BannerResponse> createBanner(@Valid @RequestBody final BannerCreateRequest bannerCreateRequest) throws DataBaseException, ImageInvalidException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var bannerResponse = this.bannerService.createBanner(bannerCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BANNER, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(bannerResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<BannerResponse> updateBanner(@Valid @RequestBody final BannerUpdateRequest bannerUpdateRequest) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var bannerResponse = this.bannerService.updateBanner(bannerUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BANNER, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(bannerResponse, messageResponse);
	}

	@Override
	@PutMapping("/{banner}")
	public CrudResponse<BannerResponse> reactivateBanner(@PathVariable final UUID banner) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var bannerResponse = this.bannerService.reactivateBanner(banner, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BANNER, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(bannerResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{banner}")
	public CrudResponse<BannerResponse> deleteBanner(@PathVariable final UUID banner) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var bannerResponse = this.bannerService.deleteBanner(banner, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_BANNER, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(bannerResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<BannerResponse> getAllBanner(@Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.bannerService.getAllBanner(paginationRequest);
	}

	@Override
	@GetMapping("/{banner}/history")
	public GetResponse<BannerHistoryResponse> getBannerHistory(@PathVariable final UUID banner, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.bannerService.getBannerHistory(banner, paginationRequest);
	}

}
