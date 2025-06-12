package mx.software.solutions.centraltextileraonline.apiservices.configurations.security;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionDto implements Serializable {

	private static final long serialVersionUID = 8844845276970998137L;

	private final UUID idCredential;
	private final UUID idUser;
	private final String role;
	private final String email;
	private final String name;
	private final String firstLastname;
	private final String secondLastname;

}
