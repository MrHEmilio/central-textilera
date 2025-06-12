package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialInfoResponse<T> {

	private final String role;

	private final T info;

	private final List<String> permission;

	private final List<MenuResponse> menus;

}
