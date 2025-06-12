package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.mail.MessagingException;

import mx.software.solutions.centraltextileraonline.apiservices.repositories.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailContactUsRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderCreatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderUpdatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailRequest;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.EmailTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClientHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.AdminRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.EmailTemplateRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;
import mx.software.solutions.centraltextileraonline.apiservices.service.TokenService;

@Slf4j
@Service
public class EmailServiceImplementation implements EmailService {

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${central-textilera.email-contact}")
	private String emailContact;

	@Value("${url.images.emails}")
	private String urlImagesEmails;

	@Value("${url.frontend}")
	private String urlSite;

	@Value("${spring.mail.username}")
	private String emailFrom;

	@Value("${central-textilera.email-pedidos}")
	private String emailPedidos;

	@Value("${central-textilera.email-admin}")
	private String emailAdmin;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private ClientHelper clientHelper;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public void sendEmail(final String email, final String subject, final String content) {
		final var sendEmailRequest = new SendEmailRequest();
		sendEmailRequest.setSubject(subject);
		sendEmailRequest.setContent(content);
		final var emails = new String[1];
		emails[0] = email;
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailWelcomeClient(final SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.WELCOME_CLIENT);
		final var token = this.tokenService.createToken(sendEmailDataRequest.getEmail(), EmailTemplate.WELCOME_CLIENT);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailDataRequest.getName());
		content = content.replace("{{email}}", sendEmailDataRequest.getEmail());
		content = content.replace("{{urlVerifyAccount}}", this.urlSite + "/client/verify");
		content = content.replace("{{token}}", token);
		sendEmailRequest.setContent(content);
		this.tokenService.saveEmailToken(token, content);

		final var emails = new String[1];
		emails[0] = sendEmailDataRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailWelcomeAdmin(final SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.WELCOME_ADMIN);
		final var token = this.tokenService.createToken(sendEmailDataRequest.getEmail(), EmailTemplate.WELCOME_ADMIN);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailDataRequest.getName());
		content = content.replace("{{email}}", sendEmailDataRequest.getEmail());
		content = content.replace("{{urlChangePassword}}", this.urlSite + "/password/change");
		content = content.replace("{{token}}", token);
		sendEmailRequest.setContent(content);
		this.tokenService.saveEmailToken(token, content);

		final var emails = new String[1];
		emails[0] = sendEmailDataRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailChangePasswordRequest(final String email) throws DataBaseException, NotFoundException {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.CHANGE_PASSWORD_REQUEST);
		final var credentialEntity = this.securityHelper.getCredentialEntityActive(email);
		final var optionalClientEntity = this.clientRepository.findByCredentialEntity(credentialEntity);
		final var optionalAdminEntity = this.adminRepository.findByCredentialEntity(credentialEntity);
		final var token = this.tokenService.createToken(email, EmailTemplate.CHANGE_PASSWORD_REQUEST);
		var name = "";
		if (optionalClientEntity.isPresent()) {
			final var clientEntity = optionalClientEntity.get();
			name = clientEntity.getName() + " " + clientEntity.getFirstLastname();
		}
		if (optionalAdminEntity.isPresent()) {
			final var adminEntity = optionalAdminEntity.get();
			name = adminEntity.getName() + " " + adminEntity.getFirstLastname();
		}
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", name);
		content = content.replace("{{urlChangePassword}}", this.urlSite + "/password/change");
		content = content.replace("{{token}}", token);
		sendEmailRequest.setContent(content);
		this.tokenService.saveEmailToken(token, content);

		final var emails = new String[1];
		emails[0] = email;
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailChangePasswordInfo(final SendEmailDataRequest sendEmailDataRequest) {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.CHANGE_PASSWORD_INFO);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailDataRequest.getName());
		sendEmailRequest.setContent(content);

		final var emails = new String[1];
		emails[0] = sendEmailDataRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailVerifyEmail(final UUID client) throws DataBaseException, NotFoundException {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.VERIFY_EMAIL);
		final var clientEntity = this.clientHelper.getClientEntity(client);
		final var token = this.tokenService.createToken(clientEntity.getCredentialEntity().getEmail(), EmailTemplate.VERIFY_EMAIL);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", clientEntity.getName() + " " + clientEntity.getFirstLastname());
		content = content.replace("{{email}}", clientEntity.getCredentialEntity().getEmail());
		content = content.replace("{{urlVerifyEmail}}", this.urlSite + "/client/verify");
		content = content.replace("{{token}}", token);
		sendEmailRequest.setContent(content);
		this.tokenService.saveEmailToken(token, content);

		final var emails = new String[1];
		emails[0] = clientEntity.getCredentialEntity().getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailDeleteAdmin(final SendEmailDataRequest sendEmailDataRequest) {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.DELETE_ADMIN);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailDataRequest.getName());
		sendEmailRequest.setContent(content);

		final var emails = new String[1];
		emails[0] = sendEmailDataRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailDeleteClient(final SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.DELETE_CLIENT);
		final var token = this.tokenService.createToken(sendEmailDataRequest.getEmail(), EmailTemplate.DELETE_CLIENT);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailDataRequest.getName());
		content = content.replace("{{urlReactiveAccount}}", this.urlSite + "/client/reactive");
		content = content.replace("{{token}}", token);
		sendEmailRequest.setContent(content);
		this.tokenService.saveEmailToken(token, content);

		final var emails = new String[1];
		emails[0] = sendEmailDataRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailReactiveAccount(final UUID client) throws DataBaseException, NotFoundException {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.REACTIVE_CLIENT);
		final var clientEntity = this.clientHelper.getClientEntity(client);
		final var token = this.tokenService.createToken(clientEntity.getCredentialEntity().getEmail(), EmailTemplate.REACTIVE_CLIENT);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", clientEntity.getName() + " " + clientEntity.getFirstLastname());
		content = content.replace("{{urlReactiveAccount}}", this.urlSite + "/client/reactive");
		content = content.replace("{{token}}", token);
		sendEmailRequest.setContent(content);
		this.tokenService.saveEmailToken(token, content);

		final var emails = new String[1];
		emails[0] = clientEntity.getCredentialEntity().getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailContactUsClient(final SendEmailContactUsRequest sendEmailContactUsRequest) {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.CONTACT_US_CLIENT);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailContactUsRequest.getName());
		sendEmailRequest.setContent(content);

		final var emails = new String[1];
		emails[0] = sendEmailContactUsRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailContactUsCtx(final SendEmailContactUsRequest sendEmailContactUsRequest) {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.CONTACT_US_CTX);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{name}}", sendEmailContactUsRequest.getName());
		content = content.replace("{{email}}", sendEmailContactUsRequest.getEmail());
		if (sendEmailContactUsRequest.getPhone() != null && !sendEmailContactUsRequest.getPhone().isBlank())
			content = content.replace("{{phone}}", sendEmailContactUsRequest.getPhone());
		else
			content = content.replace("{{phone}}", "----------");
		content = content.replace("{{message}}", sendEmailContactUsRequest.getMessage());
		sendEmailRequest.setContent(content);

		final var emails = new String[1];
		emails[0] = this.emailContact;
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailOrderCreated(final SendEmailOrderCreatedRequest sendEmailOrderCreatedRequest) {
		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.ORDER_CREATED);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{clientName}}", sendEmailOrderCreatedRequest.getClientName());
		content = content.replace("{{numberOrder}}", sendEmailOrderCreatedRequest.getNumberOrder());
		content = content.replace("{{statusOrder}}", sendEmailOrderCreatedRequest.getStatusOrder());
		final var logoDelivery = new StringBuilder().append(this.urlImagesEmails);
		if (PaymentMethod.PAYPAL.equals(sendEmailOrderCreatedRequest.getPaymentMethod()))
			logoDelivery.append("logo_paypal.png");
		if (PaymentMethod.MERCADO_PAGO.equals(sendEmailOrderCreatedRequest.getPaymentMethod()))
			logoDelivery.append("logo_mercado_pago.png");
		content = content.replace("{{logoDelivery}}", logoDelivery.toString());

		BigDecimal subTotal = sendEmailOrderCreatedRequest.getTotal().subtract(sendEmailOrderCreatedRequest.getIva()).setScale(2, RoundingMode.HALF_UP);

		content = content.replace("{{productsName}}", sendEmailOrderCreatedRequest.getProductsName());
		content = content.replace("{{productsDetail}}", sendEmailOrderCreatedRequest.getProductsDetail());
		content = content.replace("{{productsAmount}}", sendEmailOrderCreatedRequest.getProductsAmount());
		content = content.replace("{{productsPrice}}", sendEmailOrderCreatedRequest.getPriceUnit()); //Nuevo calculo para precio por unidad si la cantidad funciona!
		content = content.replace("{{subtotal}}",subTotal.toString());
		content = content.replace("{{shippingTotal}}", sendEmailOrderCreatedRequest.getShippingTotal().toString());
		content = content.replace("{{iva}}", sendEmailOrderCreatedRequest.getIva().toString());
		content = content.replace("{{total}}", sendEmailOrderCreatedRequest.getTotal().toString());

		content = content.replace("{{shippingName}}", sendEmailOrderCreatedRequest.getShippingName());
		content = content.replace("{{urlTrackingPedido}}", this.urlSite + "/tracking/" + sendEmailOrderCreatedRequest.getId());
		var textRowOne = "Gracias por tu compra. Queremos informarte que tu solicitud est&aacute; siendo verificada seg&uacute;n la disponibilidad de inventario.";
		var textRowTwo = "";
		var trackingCode = "";
		if (DeliveryMethod.PICK_UP.equals(sendEmailOrderCreatedRequest.getDeliveryMethod())) {
			textRowTwo = "Despu&eacute;s de 24 horas, podr&aacute;s recoger tu pedido en nuestra sucursal. Agradecemos tu paciencia y confianza";
			trackingCode = "- Recoger en sucursal -";
		}
		if (DeliveryMethod.SHIPPING.equals(sendEmailOrderCreatedRequest.getDeliveryMethod())) {
			textRowTwo = "Pronto recibir&aacute;s un correo de seguimiento que incluir&aacute; tu n&uacute;mero de gu&iacute;a. Estamos trabajando en generar tu n&uacute;mero de seguimiento y te informaremos tan pronto est&eacute; disponible. Agradecemos tu paciencia y confianza en nuestro servicio.";
			if (sendEmailOrderCreatedRequest.getTrackingCode() != null) {
				textRowOne = "Gracias por tu pedido. En la actualizaci&oacute;n de seguimiento de tu compra, te proporcionamos tu n&uacute;mero de gu&iacute;a. Ahora podr&aacute;s rastrear el estado de tu pedido con este n&uacute;mero.";
				textRowTwo = "Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos. &iexcl;Apreciamos tu confianza en nosotros!";
				trackingCode = sendEmailOrderCreatedRequest.getTrackingCode();
			} else
				trackingCode = "- En proceso -";
		}
		content = content.replace("{{TextRowOne}}", textRowOne);
		content = content.replace("{{TextRowTwo}}", textRowTwo);
		content = content.replace("{{TrackingCode}}", trackingCode);
		sendEmailRequest.setContent(content);

		final var emails = new String[1];
		emails[0] = sendEmailOrderCreatedRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendCppEmail(sendEmailRequest);
	}

	@Override
	public void sendEmailOrderUpdated(final SendEmailOrderUpdatedRequest sendEmailOrderUpdatedRequest) {

		final var sendEmailRequest = this.getSendEmailRequest(EmailTemplate.STATUS_UPDATE);
		var content = sendEmailRequest.getContent();
		content = this.setDataEmail(content);
		content = content.replace("{{ClientName}}", sendEmailOrderUpdatedRequest.getClientName());
		content = content.replace("{{OrderNumber}}", sendEmailOrderUpdatedRequest.getNumberOrder());
		content = content.replace("{{StatusName}}", sendEmailOrderUpdatedRequest.getStatusOrder());

		content = content.replace("{{TrackingOrderUrl}}", this.urlSite + "/tracking/" + sendEmailOrderUpdatedRequest.getId());
		if (sendEmailOrderUpdatedRequest.getTrackingCode() != null)
			content = content.replace("{{TrackingCode}}", sendEmailOrderUpdatedRequest.getTrackingCode());
		else
			content = content.replace("{{TrackingCode}}", "- En proceso -");
		sendEmailRequest.setContent(content);

		final var emails = new String[1];
		emails[0] = sendEmailOrderUpdatedRequest.getEmail();
		sendEmailRequest.setSendTo(emails);
		this.sendEmail(sendEmailRequest);

	}

	private SendEmailRequest getSendEmailRequest(final EmailTemplate emailTemplate) {
		final var emailTemplateEntity = this.emailTemplateRepository.findByName(emailTemplate);
		final var sendEmailRequest = new SendEmailRequest();
		sendEmailRequest.setSubject(emailTemplateEntity.getSubject());
		sendEmailRequest.setContent(emailTemplateEntity.getContent());
		return sendEmailRequest;
	}

	private void sendEmail(final SendEmailRequest sendEmailRequest) {
		EmailServiceImplementation.log.info("Starting send email {} to {}.", sendEmailRequest.getSubject(), sendEmailRequest.getSendTo());
		final var mimeMessage = this.javaMailSender.createMimeMessage();
		final var helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(sendEmailRequest.getSendTo());
			helper.setFrom(this.emailFrom);
			helper.setSubject(sendEmailRequest.getSubject());
			helper.setText(sendEmailRequest.getContent(), true);
			this.javaMailSender.send(mimeMessage);
			EmailServiceImplementation.log.info("Finished send email {} to {}.", sendEmailRequest.getSubject(), sendEmailRequest.getSendTo());
		} catch (final MessagingException e) {
			EmailServiceImplementation.log.info("The email {} to {} could not send. ERROR {}", sendEmailRequest.getSubject(), sendEmailRequest.getSendTo(), e.getMessage());
		}
	}

	private void sendCppEmail(final SendEmailRequest sendEmailRequest){
		EmailServiceImplementation.log.info("Starting send email {} to {}.", sendEmailRequest.getSubject(), sendEmailRequest.getSendTo());
		final var mimeMessage = this.javaMailSender.createMimeMessage();
		final var helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(sendEmailRequest.getSendTo());
			helper.setFrom(this.emailFrom);
			String[] Bccs = {this.emailAdmin, this.emailPedidos};
			//String[] Bccs = {"at@gmail.com", "r93@hotmail.com"};
			helper.setBcc(Bccs);
			helper.setSubject(sendEmailRequest.getSubject());
			
			helper.setText(sendEmailRequest.getContent(), true);
			this.javaMailSender.send(mimeMessage);
			EmailServiceImplementation.log.info("Copias Ocultas: Admin {} Pedidos {}", this.emailAdmin, this.emailPedidos );
			EmailServiceImplementation.log.info("Finished send email {} to {}.", sendEmailRequest.getSubject(), sendEmailRequest.getSendTo());
		} catch (final MessagingException e) {
			EmailServiceImplementation.log.info("The email {} to {} could not send. ERROR {}", sendEmailRequest.getSubject(), sendEmailRequest.getSendTo(), e.getMessage());
		}
	}

	private String setDataEmail(String content) {
		final var logoCentralTextilera = this.urlImagesEmails + "logo_central_textilera.png";
		final var logoFacebook = this.urlImagesEmails + "logo_facebook.png";
		final var logoInstagram = this.urlImagesEmails + "logo_instagram.png";
		final var currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		final var urlCentralTextilera = this.urlSite;
		content = content.replace("{{logoCentralTextilera}}", logoCentralTextilera);
		content = content.replace("{{logoFacebook}}", logoFacebook);
		content = content.replace("{{logoInstagram}}", logoInstagram);
		content = content.replace("{{currentYear}}", currentYear);
		return content.replace("{{urlCentralTextilera}}", urlCentralTextilera);
	}

}
