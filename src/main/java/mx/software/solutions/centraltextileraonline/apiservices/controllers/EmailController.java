package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.EmailDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailContactUsRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderCreatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SendEmailResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;
import mx.software.solutions.centraltextileraonline.apiservices.service.OrderService;

@RestController
@RequestMapping("/send/email")
public class EmailController implements EmailDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private OrderService orderService;

	@Override
	@PostMapping("/contact/us")
	public SendEmailResponse sendEmailContactUs(@Valid @RequestBody final SendEmailContactUsRequest sendEmailContactUsRequest) {
		this.emailService.sendEmailContactUsCtx(sendEmailContactUsRequest);
		this.emailService.sendEmailContactUsClient(sendEmailContactUsRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.RESPONSE, "contact.us");
		return new SendEmailResponse(messageResponse);
	}

	@Override
	@PostMapping("/change/password/{email}")
	public SendEmailResponse sendEmailChangePassword(@PathVariable final String email) throws DataBaseException, NotFoundException {
		this.emailService.sendEmailChangePasswordRequest(email);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.RESPONSE, "change.password");
		return new SendEmailResponse(messageResponse);
	}

	@Override
	@PostMapping("/reactive/client/{client}")
	public SendEmailResponse sendEmailReactiveClient(@PathVariable final UUID client) throws DataBaseException, NotFoundException {
		this.emailService.sendEmailReactiveAccount(client);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.RESPONSE, "reactive.client");
		return new SendEmailResponse(messageResponse);
	}

	@Override
	@PostMapping("/verify/email")
	public SendEmailResponse sendEmailVerifyEmail() throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		this.emailService.sendEmailVerifyEmail(sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.RESPONSE, "verify.email");
		return new SendEmailResponse(messageResponse);
	}

	@Override
	@PostMapping("/verify/email/{client}")
	public SendEmailResponse sendEmailVerifyEmail(@PathVariable final UUID client) throws DataBaseException, NotFoundException {
		this.emailService.sendEmailVerifyEmail(client);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.RESPONSE, "verify.email");
		return new SendEmailResponse(messageResponse);
	}

	@Override
	@PostMapping("/ticket/{order}")
	public SendEmailResponse sendEmailTicket(@PathVariable final UUID order) throws DataBaseException, NotFoundException {
		final var orderResponse = this.orderService.getOrderById(order);
		final var clientResponse = orderResponse.getClient();

		final var shippingName = new StringBuilder();
		if (DeliveryMethod.SHIPPING.getLabel().equals(orderResponse.getDeliveryMethod())) {
			final var orderShipping = orderResponse.getOrderShipping();
			shippingName.append(orderShipping.getStreetName());
			shippingName.append(" ");
			shippingName.append(orderShipping.getNumExt());
			shippingName.append(", ");
			shippingName.append(orderShipping.getZipCode());
			shippingName.append(" ");
			shippingName.append(orderShipping.getSuburb());
			shippingName.append(", ");
			shippingName.append(orderShipping.getMunicipality());
		}

		final var productsName = new StringBuilder();
		final var productsDetails = new StringBuilder();
		final var productsAmount = new StringBuilder();

		orderResponse.getProducts().forEach(orderProductResponse -> {
			productsName.append("<br />");
			productsName.append(orderProductResponse.getName());
			productsDetails.append("<br />");
			productsDetails.append(orderProductResponse.getColor());
			productsAmount.append("<br />");
			productsAmount.append(orderProductResponse.getAmount());
			productsAmount.append(" ");
			productsAmount.append(orderProductResponse.getSale());
		});

		final var sendEmailOrderCreatedRequest = new SendEmailOrderCreatedRequest();
		sendEmailOrderCreatedRequest.setId(orderResponse.getId());
		sendEmailOrderCreatedRequest.setEmail(clientResponse.getEmail());
		sendEmailOrderCreatedRequest.setClientName(clientResponse.getName() + " " + clientResponse.getFirstLastname());
		sendEmailOrderCreatedRequest.setNumberOrder(String.valueOf(orderResponse.getNumber()));
		sendEmailOrderCreatedRequest.setStatusOrder(OrderStatus.REVISION.getLabel());
		sendEmailOrderCreatedRequest.setPaymentMethod(orderResponse.getPaymentMethod());
		sendEmailOrderCreatedRequest.setSubtotal(orderResponse.getTotal());
		sendEmailOrderCreatedRequest.setShippingTotal(orderResponse.getOrderShipping().getPrice());
		sendEmailOrderCreatedRequest.setTotal(orderResponse.getTotal());
		sendEmailOrderCreatedRequest.setShippingName(shippingName.toString());
		sendEmailOrderCreatedRequest.setProductsName(productsName.toString());
		sendEmailOrderCreatedRequest.setProductsDetail(productsDetails.toString());
		sendEmailOrderCreatedRequest.setProductsAmount(productsAmount.toString());

		this.emailService.sendEmailOrderCreated(sendEmailOrderCreatedRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.RESPONSE, "ticket");
		return new SendEmailResponse(messageResponse);
	}

}
