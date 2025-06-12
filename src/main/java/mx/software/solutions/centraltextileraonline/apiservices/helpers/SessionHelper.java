package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import mx.software.solutions.centraltextileraonline.apiservices.configurations.security.SessionDto;

@Component
public class SessionHelper {

	public boolean isAdmin() {
		final var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken)
			return false;
		final var sessionDto = (SessionDto) authentication.getPrincipal();
		return sessionDto.getRole().contains("ADMIN");
	}

	public boolean isAdminRoot() {
		final var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken)
			return false;
		final var sessionDto = (SessionDto) authentication.getPrincipal();
		return sessionDto.getRole().contains("ADMIN_ROOT");
	}

	public SessionDto getSessionDto() {
		final var authentication = SecurityContextHolder.getContext().getAuthentication();
		return (SessionDto) authentication.getPrincipal();
	}

}
