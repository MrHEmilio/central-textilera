package mx.software.solutions.centraltextileraonline.apiservices.controllers.exceptionhandlers;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import brave.Tracer;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ExceptionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.RestTemplateExceptionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.InventoryException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.OrderStatusException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.PasswordWrongException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@Autowired
	private Tracer tracer;

	@Autowired
	private MessageLangHelper messageLangHelper;

	private String getCurrentSpanId() {
		return this.tracer.currentSpan().context().spanIdString();
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse notFoundException(final NotFoundException notFoundException) {
		final var keyMessage = new StringBuilder();
		keyMessage.append("not.found.");
		keyMessage.append(notFoundException.getKey());

		final var messageError = this.messageLangHelper.getMessageLang(notFoundException.getController(), MessageLangType.EXCEPTION, keyMessage.toString(), notFoundException.getName());
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(ExistException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse existException(final ExistException existException) {
		final var keyMessage = new StringBuilder();
		keyMessage.append("exist.");
		keyMessage.append(existException.getKey());

		final var messageError = this.messageLangHelper.getMessageLang(existException.getController(), MessageLangType.EXCEPTION, keyMessage.toString(), existException.getName());
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(DataBaseException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse dataBaseException(final DataBaseException dataBaseException) {
		final var messageError = this.messageLangHelper.getMessageLang(dataBaseException.getController(), MessageLangType.EXCEPTION, dataBaseException.getDataBaseActionType(), dataBaseException.getName());
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(LoginException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse loginException(final LoginException loginException) {
		final var messageError = this.messageLangHelper.getMessageLang(Controller.PROFILE_SECURITY, MessageLangType.EXCEPTION, "login.failure");
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(PasswordWrongException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse passwordWrongException(final PasswordWrongException loginException) {
		final var messageError = this.messageLangHelper.getMessageLang(Controller.PROFILE_SECURITY, MessageLangType.EXCEPTION, "password.wrong");
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse mehodArgumentNoValidException(final MethodArgumentNotValidException methodArgumentNotValidException) {
		final var key = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
		final var messageError = this.messageLangHelper.getMessageLang(key);
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(TokenInvalidException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse tokenInvalidException(final TokenInvalidException tokenInvalidException) {
		final var messageError = this.messageLangHelper.getMessageLang(Controller.EMAIL_SEND_EMAIL, MessageLangType.EXCEPTION, "token.invalid");
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(OrderStatusException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse orderStatusException(final OrderStatusException orderStatusException) {
		final var messageError = this.messageLangHelper.getMessageLang(Controller.PAYMENT_ORDER, MessageLangType.EXCEPTION, "status.not.more");
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(RestTemplateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public RestTemplateExceptionResponse restTemplateException(final RestTemplateException restTemplateException) {
		final var messageError = this.messageLangHelper.getMessageLang(restTemplateException.getApiRestTemplate(), MessageLangType.EXCEPTION, restTemplateException.getKey());
		return new RestTemplateExceptionResponse(this.getCurrentSpanId(), messageError, restTemplateException.getMessage());
	}

	@ExceptionHandler(ImageInvalidException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse imageInvalidException(final ImageInvalidException imageInvalidException) {
		final var messageError = this.messageLangHelper.getMessageLang(imageInvalidException.getController(), MessageLangType.EXCEPTION, "image.invalid");
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

	@ExceptionHandler(InventoryException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ExceptionResponse inventoryException(final InventoryException inventoryException) {
		final var messageError = this.messageLangHelper.getMessageLang(Controller.PAYMENT_CALCULATE, MessageLangType.EXCEPTION, "inventory");
		return new ExceptionResponse(this.getCurrentSpanId(), messageError);
	}

}
