package mx.software.solutions.centraltextileraonline.apiservices.configurations.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.entities.AdminEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.AdminRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientRepository;

@Slf4j
@Component
public class AuthenticationProviderCustom implements AuthenticationProvider {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		final var email = authentication.getName();
		final var password = authentication.getCredentials().toString();
		AuthenticationProviderCustom.log.info("Starting login {}.", email);
		var credentialEntity = new CredentialEntity();
		try {
			credentialEntity = this.securityHelper.getCredentialEntityActive(email);
		} catch (DataBaseException | NotFoundException exception) {
			exception.printStackTrace();
			throw new BadCredentialsException("User not found");
		}
		AuthenticationProviderCustom.log.info("Starting verifying the password of email {}", email);
		if (!this.passwordEncoder.matches(password, credentialEntity.getPassword())) {
			AuthenticationProviderCustom.log.info("The password is invalid");
			throw new BadCredentialsException("Password invalid");
		}
		AuthenticationProviderCustom.log.info("Finished verify the password of email {}", email);

		final var listGrantedAuthority = new ArrayList<GrantedAuthority>();
		listGrantedAuthority.add(new SimpleGrantedAuthority("ROLE_" + credentialEntity.getRoleEntity().getKey()));
		listGrantedAuthority.addAll(this.getPermissions(credentialEntity));
		listGrantedAuthority.addAll(this.getAuthorities(credentialEntity));

		final var idCredential = credentialEntity.getId();
		final var role = credentialEntity.getRoleEntity().getKey();
		UUID idUser = null;
		var name = "";
		var firstLastname = "";
		var secondLastname = "";

		if ("ADMIN_ROOT".equals(role) || "ADMIN".equals(role)) {
			final var adminEntity = this.adminRepository.findByCredentialEntity(credentialEntity).orElseGet(AdminEntity::new);
			idUser = adminEntity.getId();
			name = adminEntity.getName();
			firstLastname = adminEntity.getFirstLastname();
			secondLastname = adminEntity.getSecondLastname();
		} else if ("CLIENT".equals(role)) {
			final var clientEntity = this.clientRepository.findByCredentialEntity(credentialEntity).orElseGet(ClientEntity::new);
			idUser = clientEntity.getId();
			name = clientEntity.getName();
			firstLastname = clientEntity.getFirstLastname();
			secondLastname = clientEntity.getSecondLastname();
		}
		final var sessionDto = new SessionDto(idCredential, idUser, role, email, name, firstLastname, secondLastname);

		return new UsernamePasswordAuthenticationToken(sessionDto, email, listGrantedAuthority);
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private List<GrantedAuthority> getPermissions(final CredentialEntity credentialEntity) {
		try {
			final var listPermissionEntities = this.securityHelper.getPermissions(credentialEntity);
			return listPermissionEntities.stream().distinct().map(permissionEntity -> new SimpleGrantedAuthority(permissionEntity.getKey())).collect(Collectors.toList());
		} catch (final Exception exception) {
			exception.printStackTrace();
			return new ArrayList<>();
		}
	}

	private List<GrantedAuthority> getAuthorities(final CredentialEntity credentialEntity) {
		try {
			final var listAuthorityEntities = this.securityHelper.getAuthorities(credentialEntity);
			return listAuthorityEntities.stream().distinct().map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getName())).collect(Collectors.toList());
		} catch (final Exception exception) {
			exception.printStackTrace();
			return new ArrayList<>();
		}
	}

}
