package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateSendRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "EmailNewsletterTemplate", description = "Endpoints to manage the email newsletter template show in Central Textilera Ecommerce.")
public interface EmailNewsletterTemplateDocumentation {

	@Operation(summary = "Create email newsletter template", description = "Create email newsletter template of Central Textilera Ecommerce.")
	CrudResponse<EmailNewsletterTemplateResponse> createEmailNewsletterTemplate(EmailNewsletterTemplateCreateRequest emailNewsletterTemplateCreateRequest) throws DataBaseException, ExistException;

	@Operation(summary = "Update email newsletter template", description = "Update email newsletter template of Central Textilera Ecommerce.")
	CrudResponse<EmailNewsletterTemplateResponse> updateEmailNewsletterTemplate(EmailNewsletterTemplateUpdateRequest emailNewsletterTemplateUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate email newsletter template", description = "Reactivate email newsletter template of Central Textilera Ecommerce.")
	CrudResponse<EmailNewsletterTemplateResponse> reactivateEmailNewsletterTemplate(UUID emailNewsletterTemplate) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete email newsletter template", description = "Delete email newsletter template of Central Textilera Ecommerce.")
	CrudResponse<EmailNewsletterTemplateResponse> deleteEmailNewsletterTemplate(UUID emailNewsletterTemplate) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all emails newsletter templates", description = "Get all emails newsletter templates of Central Textilera Ecommerce.")
	GetResponse<EmailNewsletterTemplateResponse> getAllEmailNewsletterTemplate(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get email newsletter template history", description = "Get email newsletter template history of Central Textilera Ecommerce.")
	GetResponse<EmailNewsletterTemplateHistoryResponse> getEmailNewsletterTemplateHistory(UUID emailNewsletterTemplate, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Send email newsletter template", description = "Send email newsletter template of Central Textilera Ecommerce.")
	void sendEmailNewsletterTemplate(EmailNewsletterTemplateSendRequest emailNewsletterTemplateSendRequest) throws DataBaseException, NotFoundException;

}
