package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.AdminResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.AdminEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.AdminRepository;

@Slf4j
@Component
public class AdminHelper {

	@Autowired
	private AdminRepository adminRepository;

	public AdminEntity getAdminEntity(final UUID admin) throws DataBaseException, NotFoundException {
		Optional<AdminEntity> optionalAdminEntity = Optional.empty();
		try {
			AdminHelper.log.info("Starting searched the admin with the id {}.", admin);
			optionalAdminEntity = this.adminRepository.findById(admin);
		} catch (final Exception exception) {
			AdminHelper.log.error("The admin with the id {} could not read.", admin, exception);
			throw new DataBaseException(Controller.PROFILE_ADMIN, DataBaseActionType.READ, admin.toString());
		}

		if (optionalAdminEntity.isEmpty()) {
			AdminHelper.log.error("The admin not found with the id {}.", admin);
			throw new NotFoundException(Controller.PROFILE_ADMIN, "id", admin.toString());
		}
		AdminHelper.log.info("Finished search the admin with the id {}.", admin);
		return optionalAdminEntity.get();
	}

	public AdminResponse convertAdmin(final AdminEntity adminEntity) {
		final var id = adminEntity.getId();
		final var name = adminEntity.getName();
		final var firstLastname = adminEntity.getFirstLastname();
		final var secondLastname = adminEntity.getSecondLastname();
		final var email = adminEntity.getCredentialEntity().getEmail();
		final var isRoot = "ADMIN_ROOT".equals(adminEntity.getCredentialEntity().getRoleEntity().getKey());
		final var date = adminEntity.getCredentialEntity().getDate();
		final var isActive = adminEntity.getCredentialEntity().isActive();
		return new AdminResponse(id, name, firstLastname, secondLastname, email, isRoot, date, isActive);
	}

}
