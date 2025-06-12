package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.entities.AuthorityEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.PermissionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CredentialRepository;

@Slf4j
@Component
public class SecurityHelper {

	@Autowired
	private CredentialRepository credentialRepository;

	public List<PermissionEntity> getPermissions(final CredentialEntity credentialEntity) throws DataBaseException, NotFoundException {
		SecurityHelper.log.info("Starting search the permission with the credential id {}.", credentialEntity.getId());
		final List<PermissionEntity> listPermissionEntities = new ArrayList<>();
		credentialEntity.getRoleEntity().getPermissionEntities().forEach(listPermissionEntities::add);
		credentialEntity.getPermissionEntities().forEach(listPermissionEntities::add);
		SecurityHelper.log.info("Finished search the permission with the credential id {} with permission size {}.", credentialEntity.getId(), listPermissionEntities.size());
		return listPermissionEntities;
	}

	public List<AuthorityEntity> getAuthorities(final CredentialEntity credentialEntity) throws DataBaseException, NotFoundException {
		SecurityHelper.log.info("Starting search the authority with the credential id {}.", credentialEntity.getId());
		final List<AuthorityEntity> listAuthority = credentialEntity.getRoleEntity().getAuthorityEntities().stream().collect(Collectors.toList());
		SecurityHelper.log.info("Finished search the authority with the credential id {} with authority size {}.", credentialEntity.getId(), listAuthority.size());
		return listAuthority;
	}

	public CredentialEntity getCredentialEntity(final UUID credential) throws DataBaseException, NotFoundException {
		SecurityHelper.log.info("Starting search the credential with the id {}.", credential);
		Optional<CredentialEntity> optionalCredentialEntity = Optional.empty();
		try {
			optionalCredentialEntity = this.credentialRepository.findById(credential);
		} catch (final Exception exception) {
			SecurityHelper.log.error("The credential with the id {} could not read.", credential, exception);
			throw new DataBaseException(Controller.PROFILE_SECURITY, DataBaseActionType.READ, credential.toString());
		}
		if (optionalCredentialEntity.isEmpty()) {
			SecurityHelper.log.warn("The credential {} not found.", credential);
			throw new NotFoundException(Controller.PROFILE_SECURITY, "id", credential.toString());
		}
		SecurityHelper.log.info("Finished search the credentia with the id {}.", credential);
		return optionalCredentialEntity.get();
	}

	public CredentialEntity getCredentialEntityActive(final String email) throws DataBaseException, NotFoundException {
		SecurityHelper.log.info("Starting search the credential with the email {}.", email);
		Optional<CredentialEntity> optionalCredentialEntity = Optional.empty();
		try {
			optionalCredentialEntity = this.credentialRepository.findByEmailIgnoreCase(email);
		} catch (final Exception exception) {
			SecurityHelper.log.error("The credential with the id {} could not read.", email, exception);
			throw new DataBaseException(Controller.PROFILE_SECURITY, DataBaseActionType.READ, email);
		}
		if (optionalCredentialEntity.isEmpty()) {
			SecurityHelper.log.warn("The credential {} not found.", email);
			throw new NotFoundException(Controller.PROFILE_SECURITY, "email", email);
		}
		if (!optionalCredentialEntity.get().isActive()) {
			SecurityHelper.log.warn("The email {} found but not active.", email);
			throw new NotFoundException(Controller.PROFILE_SECURITY, "email", email);
		}
		SecurityHelper.log.info("Finished search the credentia with the id {}.", email);
		return optionalCredentialEntity.get();
	}

	public CredentialEntity getCredentialEntity(final String email) throws DataBaseException, NotFoundException {
		SecurityHelper.log.info("Starting search the credential with the email {}.", email);
		Optional<CredentialEntity> optionalCredentialEntity = Optional.empty();
		try {
			optionalCredentialEntity = this.credentialRepository.findByEmailIgnoreCase(email);
		} catch (final Exception exception) {
			SecurityHelper.log.error("The credential with the id {} could not read.", email, exception);
			throw new DataBaseException(Controller.PROFILE_SECURITY, DataBaseActionType.READ, email);
		}
		if (optionalCredentialEntity.isEmpty()) {
			SecurityHelper.log.warn("The credential {} not found.", email);
			throw new NotFoundException(Controller.PROFILE_SECURITY, "email", email);
		}
		SecurityHelper.log.info("Finished search the credentia with the id {}.", email);
		return optionalCredentialEntity.get();
	}

}
