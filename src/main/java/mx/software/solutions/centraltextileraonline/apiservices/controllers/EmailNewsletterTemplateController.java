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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.EmailNewsletterTemplateDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateSendRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailNewsletterTemplateService;

@RestController
@RequestMapping("/email/newsletter/template")
public class EmailNewsletterTemplateController implements EmailNewsletterTemplateDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private EmailNewsletterTemplateService emailNewsletterTemplateService;

	@Override
	@PostMapping
	public CrudResponse<EmailNewsletterTemplateResponse> createEmailNewsletterTemplate(@Valid @RequestBody final EmailNewsletterTemplateCreateRequest emailNewsletterTemplateCreateRequest) throws DataBaseException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var emailNewsletterTemplateResponse = this.emailNewsletterTemplateService.createEmailNewsletterTemplate(emailNewsletterTemplateCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(emailNewsletterTemplateResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<EmailNewsletterTemplateResponse> updateEmailNewsletterTemplate(@Valid @RequestBody final EmailNewsletterTemplateUpdateRequest emailNewsletterTemplateUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var emailNewsletterTemplateResponse = this.emailNewsletterTemplateService.updateEmailNewsletterTemplate(emailNewsletterTemplateUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(emailNewsletterTemplateResponse, messageResponse);
	}

	@Override
	@PutMapping("/{emailNewsletterTemplate}")
	public CrudResponse<EmailNewsletterTemplateResponse> reactivateEmailNewsletterTemplate(@PathVariable final UUID emailNewsletterTemplate) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var emailNewsletterTemplateResponse = this.emailNewsletterTemplateService.reactivateEmailNewsletterTemplate(emailNewsletterTemplate, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(emailNewsletterTemplateResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{emailNewsletterTemplate}")
	public CrudResponse<EmailNewsletterTemplateResponse> deleteEmailNewsletterTemplate(@PathVariable final UUID emailNewsletterTemplate) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var emailNewsletterTemplateResponse = this.emailNewsletterTemplateService.deleteEmailNewsletterTemplate(emailNewsletterTemplate, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(emailNewsletterTemplateResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<EmailNewsletterTemplateResponse> getAllEmailNewsletterTemplate(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.emailNewsletterTemplateService.getAllEmailNewsletterTemplate(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{emailNewsletterTemplate}/history")
	public GetResponse<EmailNewsletterTemplateHistoryResponse> getEmailNewsletterTemplateHistory(@PathVariable final UUID emailNewsletterTemplate, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.emailNewsletterTemplateService.getEmailNewsletterTemplateHistory(emailNewsletterTemplate, paginationRequest);
	}

	@Override
	@PostMapping("/send")
	public void sendEmailNewsletterTemplate(@Valid @RequestBody final EmailNewsletterTemplateSendRequest emailNewsletterTemplateSendRequest) throws DataBaseException, NotFoundException {
		this.emailNewsletterTemplateService.sendEmailNewsletterTemplate(emailNewsletterTemplateSendRequest);
	}

}
