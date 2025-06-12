package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.AdminResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.AdminEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.PermissionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.EmailExistWithoutPasswordException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.EmailHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MenuHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.AdminRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CredentialRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.RoleRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.AdminService;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;

@Slf4j
@Service
public class AdminServiceImplementation implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private EmailHelper emailHelper;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private MenuHelper menuHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private EmailService emailService;

	@Override
	public AdminResponse createAdmin(final AdminCreateRequest adminCreateRequest) throws DataBaseException, ExistException, EmailExistWithoutPasswordException {
		AdminServiceImplementation.log.info("Starting created the admin {}.", adminCreateRequest.getEmail());
		this.emailHelper.validateEmailNotExist(adminCreateRequest.getEmail());
		try {
			final var credentialEntity = new CredentialEntity();
			credentialEntity.setEmail(adminCreateRequest.getEmail());
			credentialEntity.setPassword("password");
			credentialEntity.setRoleEntity(this.roleRepository.findByKey("ADMIN"));
			credentialEntity.setDate(new Date());
			credentialEntity.setActive(true);

			final var adminEntity = new AdminEntity();
			adminEntity.setCredentialEntity(credentialEntity);
			adminEntity.setName(adminCreateRequest.getName());
			adminEntity.setFirstLastname(adminCreateRequest.getFirstLastname());

			if (adminCreateRequest.getSecondLastname() != null && !adminCreateRequest.getSecondLastname().isBlank())
				adminEntity.setSecondLastname(adminCreateRequest.getSecondLastname());

			final var newAdminEntity = this.adminRepository.save(adminEntity);
			AdminServiceImplementation.log.info("Finished create the admin {}.", adminCreateRequest.getEmail());

			AdminServiceImplementation.log.info("Starting send email to admin {}.", adminCreateRequest.getEmail());
			final var sendEmailDataRequest = new SendEmailDataRequest();
			sendEmailDataRequest.setEmail(adminCreateRequest.getEmail());
			sendEmailDataRequest.setName(adminCreateRequest.getName() + " " + adminCreateRequest.getFirstLastname());
			this.emailService.sendEmailWelcomeAdmin(sendEmailDataRequest);
			AdminServiceImplementation.log.info("Finished send email to admin {}.", adminCreateRequest.getEmail());

			return this.adminHelper.convertAdmin(newAdminEntity);
		} catch (final Exception exception) {
			AdminServiceImplementation.log.error("The client {} could not been create.", adminCreateRequest.getEmail(), exception);
			throw new DataBaseException(Controller.PROFILE_ADMIN, DataBaseActionType.CREATE, adminCreateRequest.getEmail());
		}
	}

	@Override
	public AdminResponse updateAdmin(final AdminUpdateRequest adminUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException {
		AdminServiceImplementation.log.info("Starting updated the admin with the id {}", admin);
		final var adminEntity = this.adminHelper.getAdminEntity(admin);
		adminEntity.setName(adminUpdateRequest.getName());
		adminEntity.setFirstLastname(adminUpdateRequest.getFirstLastname());
		adminEntity.setSecondLastname(adminUpdateRequest.getSecondLastname());

		final var credentialEntity = adminEntity.getCredentialEntity();
		credentialEntity.setActive(true);
		try {
			final var newAdminEntity = this.adminRepository.save(adminEntity);
			AdminServiceImplementation.log.info("Finished update the admin with the id {}.", admin);
			return this.adminHelper.convertAdmin(newAdminEntity);
		} catch (final Exception exception) {
			AdminServiceImplementation.log.error("The admin could not been update with the id {}.", admin, exception);
			throw new DataBaseException(Controller.PROFILE_ADMIN, DataBaseActionType.UPDATE, adminUpdateRequest.getName());
		}
	}

	@Override
	public AdminResponse deleteAdmin(final UUID admin) throws DataBaseException, NotFoundException {
		AdminServiceImplementation.log.info("Starting deleted the admin with the id {}.", admin);
		final var adminEntity = this.adminHelper.getAdminEntity(admin);
		final var credentialEntity = adminEntity.getCredentialEntity();
		credentialEntity.setActive(false);
		try {
			this.credentialRepository.save(credentialEntity);
			AdminServiceImplementation.log.info("Finished delete the admin with the id {}.", admin);

			AdminServiceImplementation.log.info("Starting send email to admin {}.", credentialEntity.getEmail());
			final var sendEmailDataRequest = new SendEmailDataRequest();
			sendEmailDataRequest.setEmail(credentialEntity.getEmail());
			sendEmailDataRequest.setName(adminEntity.getName() + " " + adminEntity.getFirstLastname());
			this.emailService.sendEmailDeleteAdmin(sendEmailDataRequest);
			AdminServiceImplementation.log.info("Finished send email to admin {}.", credentialEntity.getEmail());

			return this.adminHelper.convertAdmin(adminEntity);
		} catch (final Exception exception) {
			AdminServiceImplementation.log.error("The admin with the id {} could not been delete.", admin, exception);
			throw new DataBaseException(Controller.PROFILE_ADMIN, DataBaseActionType.DELETE, admin.toString());
		}
	}

	@Override
	public GetResponse<AdminResponse> getAllAdmin(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		AdminServiceImplementation.log.info("Starting searched of all admins.");
		Page<AdminEntity> pageAdminEntity = null;
		try {
			final var search = filterRequest.getSearch();
			String direction = null;
			if (paginationRequest.getTypeSort() != null)
				direction = paginationRequest.getTypeSort().name();
			paginationRequest.setColumnSort(null);
			paginationRequest.setTypeSort(null);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageAdminEntity = this.adminRepository.findAll(search, direction, pageable);
		} catch (final Exception exception) {
			AdminServiceImplementation.log.error("The admins could not been read.", exception);
			throw new DataBaseException(Controller.PROFILE_ADMIN, DataBaseActionType.READ);
		}
		final var listAdminResponse = pageAdminEntity.get().map(this.adminHelper::convertAdmin).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageAdminEntity);
		if (listAdminResponse.isEmpty()) {
			AdminServiceImplementation.log.error("The admins not found.");
			throw new NotFoundException(Controller.PROFILE_ADMIN, "all");
		}
		AdminServiceImplementation.log.info("Finished search the admins.");
		return new GetResponse<>(listAdminResponse, paginationResponse);
	}

	@Override
	public CredentialInfoResponse<AdminResponse> getInfoAdmin(final UUID admin) throws DataBaseException, NotFoundException {
		AdminServiceImplementation.log.info("Starting searched the admin with the id {}.", admin);
		final var adminEntity = this.adminHelper.getAdminEntity(admin);
		AdminServiceImplementation.log.info("Finished search the admin with the id {}.", admin);
		final var role = adminEntity.getCredentialEntity().getRoleEntity().getKey();
		final var adminResponse = this.adminHelper.convertAdmin(adminEntity);
		final var listPermissionEntities = this.securityHelper.getPermissions(adminEntity.getCredentialEntity());
		final var listPermissionKeyString = listPermissionEntities.stream().map(PermissionEntity::getKey).collect(Collectors.toList());
		final var menuResponse = this.menuHelper.getMenuResponse(adminEntity.getCredentialEntity(), listPermissionEntities);
		return new CredentialInfoResponse<>(role, adminResponse, listPermissionKeyString, menuResponse);
	}

}
