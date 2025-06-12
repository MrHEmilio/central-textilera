package mx.software.solutions.centraltextileraonline.apiservices.configurations.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;

@Configuration
public class AuthenticationSuccessHandlerCustom implements AuthenticationSuccessHandler {

	@Autowired
	private SecurityHelper securityHelper;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
		final var sessionDto = (SessionDto) authentication.getPrincipal();
		var credentialEntity = new CredentialEntity();
		try {
			credentialEntity = this.securityHelper.getCredentialEntityActive(sessionDto.getEmail());
		} catch (DataBaseException | NotFoundException exception) {
			exception.printStackTrace();
			throw new IOException("User not found");
		}
		final var role = credentialEntity.getRoleEntity();
		var pathRedirect = "";

		if ("DEVELOPER".equals(role.getKey())) {
			response.setStatus(HttpStatus.FOUND.value());
			response.sendRedirect("/swagger-ui/index.html");
		} else if ("ADMIN_ROOT".equals(role.getKey()) || "ADMIN".equals(role.getKey()))
			pathRedirect = "/admin/info";
		else if ("CLIENT".equals(role.getKey()))
			pathRedirect = "/client/info";

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		final var writer = response.getWriter();
		writer.print("{\"redirect\": \"" + pathRedirect + "\"}");
		writer.flush();
	}

}
