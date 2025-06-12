package mx.software.solutions.centraltextileraonline.apiservices.configurations.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import brave.Tracer;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;

@Configuration
public class AuthenticationFailureHandlerCustom implements AuthenticationFailureHandler {

	@Autowired
	private Tracer tracer;

	@Autowired
	private MessageLangHelper messageLangHelper;

	private String getCurrentSpanId() {
		return this.tracer.currentSpan().context().spanIdString();
	}

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authenticationException) throws IOException, ServletException {
		final var messageError = this.messageLangHelper.getMessageLang(Controller.PROFILE_SECURITY, MessageLangType.EXCEPTION, "login.failure");
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		final var writer = response.getWriter();
		writer.print("{\"id\": \" " + this.getCurrentSpanId() + " \", \"message\": \"" + messageError + "\"}");
		writer.flush();
	}

}
