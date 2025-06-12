package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailNewsletterTemplateEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailNewsletterTemplateHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.EmailNewsletterTemplateRepository;

@Slf4j
@Component
public class EmailNewsletterTemplateHelper {

	@Autowired
	private EmailNewsletterTemplateRepository emailNewsletterTemplateRepository;

	@Autowired
	private AdminHelper adminHelper;

	public EmailNewsletterTemplateEntity getEmailNewsletterTemplateEntity(final UUID emailNewsletterTemplate) throws DataBaseException, NotFoundException {
		Optional<EmailNewsletterTemplateEntity> optionalEmailNewsletterTemplateEntity = Optional.empty();
		try {
			EmailNewsletterTemplateHelper.log.info("Starting searched the email newsletter template with the id {}.", emailNewsletterTemplate);
			optionalEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.findById(emailNewsletterTemplate);
		} catch (final Exception exception) {
			EmailNewsletterTemplateHelper.log.error("The Email newsletter template with the id {} could not read.", emailNewsletterTemplate, exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.READ, emailNewsletterTemplate.toString());
		}

		if (optionalEmailNewsletterTemplateEntity.isEmpty()) {
			EmailNewsletterTemplateHelper.log.error("The email newsletter template not found with the id {}.", emailNewsletterTemplate);
			throw new NotFoundException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, "id", emailNewsletterTemplate.toString());
		}
		EmailNewsletterTemplateHelper.log.info("Finished search the email newsletter template with the id {}.", emailNewsletterTemplate);
		return optionalEmailNewsletterTemplateEntity.get();
	}

	public EmailNewsletterTemplateResponse convertEmailNewsletterTemplate(final EmailNewsletterTemplateEntity emailNewsletterTemplateEntity) {
		final var id = emailNewsletterTemplateEntity.getId();
		final var name = emailNewsletterTemplateEntity.getName();
		final var subject = emailNewsletterTemplateEntity.getSubject();
		final var contentHtml = emailNewsletterTemplateEntity.getContentHtml();
		final var contentJson = emailNewsletterTemplateEntity.getContentJson();
		final var isActive = emailNewsletterTemplateEntity.isActive();
		return new EmailNewsletterTemplateResponse(id, name, subject, contentHtml, contentJson, isActive);
	}

	public EmailNewsletterTemplateHistoryResponse convertEmailNewsletterTemplateHistory(final EmailNewsletterTemplateHistoryEntity emailNewsletterTemplateHistoryEntity) {
		final var id = emailNewsletterTemplateHistoryEntity.getId();
		final var emailNewsletterTemplateResponse = this.convertEmailNewsletterTemplate(emailNewsletterTemplateHistoryEntity.getEmailNewsletterTemplateEntity());
		final var adminResponse = this.adminHelper.convertAdmin(emailNewsletterTemplateHistoryEntity.getAdminEntity());
		final var actionType = emailNewsletterTemplateHistoryEntity.getActionType();
		final var date = emailNewsletterTemplateHistoryEntity.getDate();
		final var object = emailNewsletterTemplateHistoryEntity.getObject();
		return new EmailNewsletterTemplateHistoryResponse(id, emailNewsletterTemplateResponse, adminResponse, actionType, date, object);
	}

}
