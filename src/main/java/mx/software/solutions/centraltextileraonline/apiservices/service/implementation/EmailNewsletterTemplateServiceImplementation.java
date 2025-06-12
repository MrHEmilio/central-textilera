package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateSendRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.EmailNewsletterTemplateUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.EmailNewsletterTemplateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailNewsletterTemplateEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailNewsletterTemplateHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.SendEmailNewsletterTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.EmailNewsletterTemplateHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.EmailNewsletterTemplateHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.EmailNewsletterTemplateRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.NewsletterRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailNewsletterTemplateService;

@Slf4j
@Service
public class EmailNewsletterTemplateServiceImplementation implements EmailNewsletterTemplateService {

	@Autowired
	private EmailNewsletterTemplateRepository emailNewsletterTemplateRepository;

	@Autowired
	private EmailNewsletterTemplateHistoryRepository emailNewsletterTemplateHistoryRepository;

	@Autowired
	private EmailNewsletterTemplateHelper emailNewsletterTemplateHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String emailFrom;

	@Override
	public EmailNewsletterTemplateResponse createEmailNewsletterTemplate(final EmailNewsletterTemplateCreateRequest emailNewsletterTemplateCreateRequest, final UUID admin) throws DataBaseException, ExistException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting created the email newsletter template {}.", emailNewsletterTemplateCreateRequest.getName());
		this.validateEmailNewsletterTemplateNotExist(emailNewsletterTemplateCreateRequest.getName());
		try {
			final var emailNewsletterTemplateEntity = new EmailNewsletterTemplateEntity();
			emailNewsletterTemplateEntity.setName(emailNewsletterTemplateCreateRequest.getName());
			emailNewsletterTemplateEntity.setSubject(emailNewsletterTemplateCreateRequest.getSubject());
			emailNewsletterTemplateEntity.setContentHtml(emailNewsletterTemplateCreateRequest.getContentHtml());
			emailNewsletterTemplateEntity.setContentJson(emailNewsletterTemplateCreateRequest.getContentJson());
			emailNewsletterTemplateEntity.setActive(true);
			final var newEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.save(emailNewsletterTemplateEntity);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished create the email newsletter template {}.", emailNewsletterTemplateCreateRequest.getName());

			EmailNewsletterTemplateServiceImplementation.log.info("Starting created the create history {}.", emailNewsletterTemplateCreateRequest.getName());
			this.createEmailNewsletterTemplateHistory(newEmailNewsletterTemplateEntity, admin, DataBaseActionType.CREATE);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished create the create history {}.", emailNewsletterTemplateCreateRequest.getName());

			return this.emailNewsletterTemplateHelper.convertEmailNewsletterTemplate(newEmailNewsletterTemplateEntity);
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("The email newsletter template {} could not been create.", emailNewsletterTemplateCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.CREATE, emailNewsletterTemplateCreateRequest.getName());
		}
	}

	@Override
	public EmailNewsletterTemplateResponse updateEmailNewsletterTemplate(final EmailNewsletterTemplateUpdateRequest emailNewsletterTemplateUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting updated the email newsletter template with the id {}.", emailNewsletterTemplateUpdateRequest.getId());
		final var emailNewsletterTemplateEntity = this.emailNewsletterTemplateHelper.getEmailNewsletterTemplateEntity(emailNewsletterTemplateUpdateRequest.getId());
		emailNewsletterTemplateEntity.setName(emailNewsletterTemplateUpdateRequest.getName());
		emailNewsletterTemplateEntity.setSubject(emailNewsletterTemplateUpdateRequest.getSubject());
		emailNewsletterTemplateEntity.setContentHtml(emailNewsletterTemplateUpdateRequest.getContentHtml());
		emailNewsletterTemplateEntity.setContentJson(emailNewsletterTemplateUpdateRequest.getContentJson());
		emailNewsletterTemplateEntity.setActive(true);
		try {
			final var newEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.save(emailNewsletterTemplateEntity);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished update the email newsletter template with the id {}.", emailNewsletterTemplateUpdateRequest.getId());

			EmailNewsletterTemplateServiceImplementation.log.info("Starting created the update history of id {}.", emailNewsletterTemplateUpdateRequest.getId());
			this.createEmailNewsletterTemplateHistory(newEmailNewsletterTemplateEntity, admin, DataBaseActionType.UPDATE);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished create the update history of id {}.", emailNewsletterTemplateUpdateRequest.getId());

			return this.emailNewsletterTemplateHelper.convertEmailNewsletterTemplate(newEmailNewsletterTemplateEntity);
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("The email newsletter template with the id {} could not been update.", emailNewsletterTemplateUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.UPDATE, emailNewsletterTemplateUpdateRequest.getName());
		}
	}

	@Override
	public EmailNewsletterTemplateResponse reactivateEmailNewsletterTemplate(final UUID emailNewsletterTemplate, final UUID admin) throws DataBaseException, NotFoundException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting reactivated the email newsletter template with the id {}.", emailNewsletterTemplate);
		final var emailNewsletterTemplateEntity = this.emailNewsletterTemplateHelper.getEmailNewsletterTemplateEntity(emailNewsletterTemplate);
		emailNewsletterTemplateEntity.setActive(true);
		try {
			final var newEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.save(emailNewsletterTemplateEntity);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished reactivate the email newsletter template with the id {}.", emailNewsletterTemplate);

			EmailNewsletterTemplateServiceImplementation.log.info("Starting created the reactivate history of id {}.", emailNewsletterTemplate);
			this.createEmailNewsletterTemplateHistory(newEmailNewsletterTemplateEntity, admin, DataBaseActionType.REACTIVATE);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished create the reactivate history of id {}.", emailNewsletterTemplate);

			return this.emailNewsletterTemplateHelper.convertEmailNewsletterTemplate(newEmailNewsletterTemplateEntity);
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("The email newsletter template with the id {} could not been reactivate.", emailNewsletterTemplate, exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.REACTIVATE, emailNewsletterTemplate.toString());
		}
	}

	@Override
	public EmailNewsletterTemplateResponse deleteEmailNewsletterTemplate(final UUID emailNewsletterTemplate, final UUID admin) throws DataBaseException, NotFoundException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting deleted the email newsletter template with the id {}.", emailNewsletterTemplate);
		final var emailNewsletterTemplateEntity = this.emailNewsletterTemplateHelper.getEmailNewsletterTemplateEntity(emailNewsletterTemplate);
		emailNewsletterTemplateEntity.setActive(false);
		try {
			final var newEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.save(emailNewsletterTemplateEntity);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished delete the email newsletter template with the id {}.", emailNewsletterTemplate);

			EmailNewsletterTemplateServiceImplementation.log.info("Starting created the delete history of id {}.", emailNewsletterTemplate);
			this.createEmailNewsletterTemplateHistory(newEmailNewsletterTemplateEntity, admin, DataBaseActionType.DELETE);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished create the delete history of id {}.", emailNewsletterTemplate);

			return this.emailNewsletterTemplateHelper.convertEmailNewsletterTemplate(newEmailNewsletterTemplateEntity);
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("The email newsletter template with the id {} could not been delete.", emailNewsletterTemplate, exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.DELETE, emailNewsletterTemplate.toString());
		}
	}

	@Override
	public GetResponse<EmailNewsletterTemplateResponse> getAllEmailNewsletterTemplate(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting searched of all emails newsletter templates.");
		Page<EmailNewsletterTemplateEntity> pageEmailNewsletterTemplateEntity = null;
		try {
			final var search = filterRequest.getSearch();
			final var active = filterRequest.getActive();
			String direction = null;
			if (paginationRequest.getTypeSort() != null)
				direction = paginationRequest.getTypeSort().name();
			paginationRequest.setColumnSort(null);
			paginationRequest.setTypeSort(null);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("The emails newsletter templates could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.READ);
		}
		final var listEmailNewsletterTemplateResponse = pageEmailNewsletterTemplateEntity.get().map(this.emailNewsletterTemplateHelper::convertEmailNewsletterTemplate).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageEmailNewsletterTemplateEntity);
		if (listEmailNewsletterTemplateResponse.isEmpty()) {
			EmailNewsletterTemplateServiceImplementation.log.error("The emails newsletter templates not found.");
			throw new NotFoundException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, "all");
		}
		EmailNewsletterTemplateServiceImplementation.log.info("Finished search the emails newsletter templates.");
		return new GetResponse<>(listEmailNewsletterTemplateResponse, paginationResponse);
	}

	@Override
	public GetResponse<EmailNewsletterTemplateHistoryResponse> getEmailNewsletterTemplateHistory(final UUID emailNewsletterTemplate, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting searched history of email newsletter template with the id {}.", emailNewsletterTemplate);
		final var emailNewsletterTemplateEntity = this.emailNewsletterTemplateHelper.getEmailNewsletterTemplateEntity(emailNewsletterTemplate);
		Page<EmailNewsletterTemplateHistoryEntity> pageEmailNewsletterTemplateHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageEmailNewsletterTemplateHistoryEntity = this.emailNewsletterTemplateHistoryRepository.findAllByEmailNewsletterTemplateEntity(emailNewsletterTemplateEntity, pageable);
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("The history email newsletter template could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.READ);
		}
		final var listEmailNewsletterTemplateHistoryResponse = pageEmailNewsletterTemplateHistoryEntity.get().map(this.emailNewsletterTemplateHelper::convertEmailNewsletterTemplateHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageEmailNewsletterTemplateHistoryEntity);
		EmailNewsletterTemplateServiceImplementation.log.info("Finished search history of fiber with the id {}.", emailNewsletterTemplate);
		return new GetResponse<>(listEmailNewsletterTemplateHistoryResponse, paginationResponse);
	}

	@Override
	public void sendEmailNewsletterTemplate(final EmailNewsletterTemplateSendRequest emailNewsletterTemplateSendRequest) throws DataBaseException, NotFoundException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting send email newsletter template with id {} to {}.", emailNewsletterTemplateSendRequest.getEmailNewsletterTemplate(), emailNewsletterTemplateSendRequest.getSendTo());
		final var emailNewsletterTemplateEntity = this.emailNewsletterTemplateHelper.getEmailNewsletterTemplateEntity(emailNewsletterTemplateSendRequest.getEmailNewsletterTemplate());
		final var mimeMessage = this.javaMailSender.createMimeMessage();
		final var helper = new MimeMessageHelper(mimeMessage);

		final List<String> emailsTo = new ArrayList<>();
		try {
			if (SendEmailNewsletterTemplate.CLIENTS.equals(emailNewsletterTemplateSendRequest.getSendTo()) || SendEmailNewsletterTemplate.EVERYBODY.equals(emailNewsletterTemplateSendRequest.getSendTo()))
				this.clientRepository.findAll().forEach(clientEntity -> emailsTo.add(clientEntity.getCredentialEntity().getEmail()));
			if (SendEmailNewsletterTemplate.NEWSLETTERS.equals(emailNewsletterTemplateSendRequest.getSendTo())
			    || SendEmailNewsletterTemplate.EVERYBODY.equals(emailNewsletterTemplateSendRequest.getSendTo()))
				this.newsletterRepository.findAll().forEach(newsletterEntity -> emailsTo.add(newsletterEntity.getEmail()));
		} catch (final Exception exception) {
			EmailNewsletterTemplateServiceImplementation.log.error("Could not get newsletter of subcription from data base.", exception);
			throw new DataBaseException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, DataBaseActionType.READ);
		}

		if (emailsTo.isEmpty()) {
			EmailNewsletterTemplateServiceImplementation.log.error("Not found newsletter of subcription");
			throw new NotFoundException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, "email");
		}

		try {
			helper.setTo(emailsTo.toArray(new String[0]));
			helper.setFrom(this.emailFrom);
			helper.setSubject(emailNewsletterTemplateEntity.getSubject());
			helper.setText(emailNewsletterTemplateEntity.getContentHtml(), true);
			this.javaMailSender.send(mimeMessage);
			EmailNewsletterTemplateServiceImplementation.log.info("Finished send email newsletter template {} to {}.", emailNewsletterTemplateSendRequest.getEmailNewsletterTemplate(), emailNewsletterTemplateSendRequest.getSendTo());
		} catch (final MessagingException e) {
			EmailNewsletterTemplateServiceImplementation.log.info("The email newsletter template {} to {} could not send.", emailNewsletterTemplateSendRequest.getEmailNewsletterTemplate(), emailNewsletterTemplateSendRequest.getSendTo());
			e.printStackTrace();
		}
	}

	private void validateEmailNewsletterTemplateNotExist(final String name) throws ExistException {
		EmailNewsletterTemplateServiceImplementation.log.info("Starting validate the email newsletter template if exist {}.", name);
		final var optionalEmailNewsletterTemplateEntity = this.emailNewsletterTemplateRepository.findByNameIgnoreCase(name);
		if (optionalEmailNewsletterTemplateEntity.isPresent()) {
			EmailNewsletterTemplateServiceImplementation.log.error("The email newsletter template {} exist.", name);
			throw new ExistException(Controller.CATALOG_EMAIL_NEWSLETTER_TEMPLATE, "name", name);
		}
	}

	private void createEmailNewsletterTemplateHistory(final EmailNewsletterTemplateEntity emailNewsletterTemplateEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var emailNewsletterTemplateHistoryEntity = new EmailNewsletterTemplateHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(emailNewsletterTemplateEntity);
			emailNewsletterTemplateHistoryEntity.setEmailNewsletterTemplateEntity(emailNewsletterTemplateEntity);
			emailNewsletterTemplateHistoryEntity.setAdminEntity(adminEntity);
			emailNewsletterTemplateHistoryEntity.setActionType(dataBaseActionType);
			emailNewsletterTemplateHistoryEntity.setDate(new Date());
			emailNewsletterTemplateHistoryEntity.setObject(object);
			this.emailNewsletterTemplateHistoryRepository.save(emailNewsletterTemplateHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
