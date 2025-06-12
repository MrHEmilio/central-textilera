package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientVerifyDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientVerifyDataResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.PermissionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.EmailExistWithoutPasswordException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClientHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.CountryCodeHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.EmailHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MenuHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CredentialRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.RoleRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientService;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;
import mx.software.solutions.centraltextileraonline.apiservices.service.TokenService;

@Slf4j
@Service
public class ClientServiceImplementation implements ClientService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmailHelper emailHelper;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private MenuHelper menuHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private ClientHelper clientHelper;

	@Autowired
	private CountryCodeHelper countryCodeHelper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TokenService tokenService;

	@Value("${central-textilera.generic-password}")
	private String genericPassword;

	@Override
	public ClientResponse createClient(final ClientCreateRequest clientCreateRequest) throws DataBaseException, NotFoundException, ExistException {
		ClientServiceImplementation.log.info("Starting created the client {}.", clientCreateRequest.getEmail());
		var credentialEntity = new CredentialEntity();
		var clientEntity = new ClientEntity();
		var isClientNew = true;
		try {
			this.emailHelper.validateEmailNotExist(clientCreateRequest.getEmail());
		} catch (final EmailExistWithoutPasswordException emailExistWithoutPasswordException) {
			credentialEntity = this.securityHelper.getCredentialEntity(clientCreateRequest.getEmail());
			clientEntity = this.clientRepository.findByCredentialEntity(credentialEntity).orElseGet(ClientEntity::new);
			isClientNew = false;
		}
		final var countryCodeEntity = this.countryCodeHelper.getCountryCodeEntity(clientCreateRequest.getCountryCode());
		if (isClientNew) {
			this.validatePhoneNotExist(countryCodeEntity, clientCreateRequest.getPhone());
			this.validateRfcNotExist(clientCreateRequest.getRfc());
		}
		try {
			credentialEntity.setEmail(clientCreateRequest.getEmail());
			credentialEntity.setPassword(this.passwordEncoder.encode(clientCreateRequest.getPassword()));
			credentialEntity.setRoleEntity(this.roleRepository.findByKey("CLIENT"));
			credentialEntity.setDate(new Date());
			credentialEntity.setActive(true);

			clientEntity.setCredentialEntity(credentialEntity);
			clientEntity.setName(clientCreateRequest.getName());
			clientEntity.setFirstLastname(clientCreateRequest.getFirstLastname());

			if (clientCreateRequest.getSecondLastname() != null && !clientCreateRequest.getSecondLastname().isBlank())
				clientEntity.setSecondLastname(clientCreateRequest.getSecondLastname());

			clientEntity.setCountryCodeEntity(countryCodeEntity);
			clientEntity.setPhone(clientCreateRequest.getPhone());
			clientEntity.setEmailValidated(false);
			clientEntity.setRfc(clientCreateRequest.getRfc());
			clientEntity.setCompanyName(clientCreateRequest.getCompanyName());
			clientEntity.setFiscalRegimen(clientCreateRequest.getFiscalRegimen());

			final var newClientEntity = this.clientRepository.save(clientEntity);
			ClientServiceImplementation.log.info("Finished create the client {}.", clientCreateRequest.getEmail());

			if (!this.passwordEncoder.matches(this.genericPassword, credentialEntity.getPassword())) {
				ClientServiceImplementation.log.info("Starting send email to client {}.", clientCreateRequest.getEmail());
				final var sendEmailDataRequest = new SendEmailDataRequest();
				sendEmailDataRequest.setEmail(clientCreateRequest.getEmail());
				sendEmailDataRequest.setName(clientCreateRequest.getName() + " " + clientCreateRequest.getFirstLastname());
				this.emailService.sendEmailWelcomeClient(sendEmailDataRequest);
				ClientServiceImplementation.log.info("Finished send email to client {}.", clientCreateRequest.getEmail());
			}

			return this.clientHelper.convertClient(newClientEntity);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The client {} could not been create.", clientCreateRequest.getEmail(), exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.CREATE, clientCreateRequest.getEmail());
		}
	}

	@Override
	public ClientResponse updateClient(final ClientUpdateRequest clientUpdateRequest, final UUID client) throws DataBaseException, NotFoundException, ExistException {
		ClientServiceImplementation.log.info("Starting updated the client with the id {}", client);
		final var countryCodeEntity = this.countryCodeHelper.getCountryCodeEntity(clientUpdateRequest.getCountryCode());
		final var clientEntity = this.clientHelper.getClientEntity(client);
		clientEntity.setName(clientUpdateRequest.getName());
		clientEntity.setFirstLastname(clientUpdateRequest.getFirstLastname());

		if (clientUpdateRequest.getSecondLastname() != null && !clientUpdateRequest.getSecondLastname().isBlank())
			clientEntity.setSecondLastname(clientUpdateRequest.getSecondLastname());

		clientEntity.setCountryCodeEntity(countryCodeEntity);
		clientEntity.setPhone(clientUpdateRequest.getPhone());
		clientEntity.setRfc(clientUpdateRequest.getRfc());
		clientEntity.setCompanyName(clientUpdateRequest.getCompanyName());
		clientEntity.setFiscalRegimen(clientUpdateRequest.getFiscalRegimen());
		try {
			final var newClientEntity = this.clientRepository.save(clientEntity);
			ClientServiceImplementation.log.info("Finished update the client with the id {}.", client);
			return this.clientHelper.convertClient(newClientEntity);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The client could not been update with the id {}.", client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.UPDATE, clientUpdateRequest.getName());
		}
	}

	@Override
	public ClientResponse deleteClient(final UUID client) throws DataBaseException, NotFoundException {
		ClientServiceImplementation.log.info("Starting deleted the client with the id {}.", client);
		final var clientEntity = this.clientHelper.getClientEntity(client);
		final var credentialEntity = clientEntity.getCredentialEntity();
		credentialEntity.setActive(false);
		try {
			this.credentialRepository.save(credentialEntity);
			ClientServiceImplementation.log.info("Finished delete the client with the id {}.", client);
			return this.clientHelper.convertClient(clientEntity);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The client with the id {} could not been delete.", client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.DELETE, client.toString());
		}
	}

	@Override
	public GetResponse<ClientResponse> getAllClient(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		ClientServiceImplementation.log.info("Starting searched of all clients.");
		Page<ClientEntity> pageClientEntity = null;
		try {
			final var search = filterRequest.getSearch();
			String direction = null;
			if (paginationRequest.getTypeSort() != null)
				direction = paginationRequest.getTypeSort().name();
			paginationRequest.setColumnSort(null);
			paginationRequest.setTypeSort(null);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageClientEntity = this.clientRepository.findAll(search, direction, pageable);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The clients could not been read.", exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.DELETE);
		}
		final var listClientResponse = pageClientEntity.get().map(this.clientHelper::convertClient).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageClientEntity);
		if (listClientResponse.isEmpty()) {
			ClientServiceImplementation.log.error("The clients not found.");
			throw new NotFoundException(Controller.PROFILE_CLIENT, "all");
		}
		ClientServiceImplementation.log.info("Finished search the clients.");
		return new GetResponse<>(listClientResponse, paginationResponse);
	}

	@Override
	public ClientResponse getClientByEmail(final String email) throws DataBaseException, NotFoundException {
		ClientServiceImplementation.log.info("Starting searched the client with the email {}.", email);
		final var credentialEntity = this.securityHelper.getCredentialEntity(email);
		Optional<ClientEntity> optionalClientEntity = Optional.empty();
		try {
			optionalClientEntity = this.clientRepository.findByCredentialEntity(credentialEntity);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The clients could not been read.", exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.UPDATE);
		}
		if (optionalClientEntity.isEmpty()) {
			ClientServiceImplementation.log.error("The client not found.");
			throw new NotFoundException(Controller.PROFILE_CLIENT, "email");
		}
		ClientServiceImplementation.log.info("Finished search the client with the email {}.", email);
		return this.clientHelper.convertClient(optionalClientEntity.get());
	}

	@Override
	public CredentialInfoResponse<ClientResponse> getInfoClient(final UUID client) throws DataBaseException, NotFoundException {
		ClientServiceImplementation.log.info("Starting searched the client with the id {}.", client);
		final var clientEntity = this.clientHelper.getClientEntity(client);
		ClientServiceImplementation.log.info("Finished search the client with the id {}.", client);
		final var role = clientEntity.getCredentialEntity().getRoleEntity().getKey();
		final var clientResponse = this.clientHelper.convertClient(clientEntity);
		final var listPermissionEntities = this.securityHelper.getPermissions(clientEntity.getCredentialEntity());
		final var listPermissionKeyString = listPermissionEntities.stream().map(PermissionEntity::getKey).collect(Collectors.toList());
		final var menuResponse = this.menuHelper.getMenuResponse(clientEntity.getCredentialEntity(), listPermissionEntities);
		return new CredentialInfoResponse<>(role, clientResponse, listPermissionKeyString, menuResponse);
	}

	@Override
	public void verifyClient(final String email) throws DataBaseException, NotFoundException {
		final var credentialEntity = this.securityHelper.getCredentialEntityActive(email);
		try {
			final var clientEntity = this.clientRepository.findByCredentialEntity(credentialEntity).orElseGet(ClientEntity::new);
			clientEntity.setEmailValidated(true);
			this.clientRepository.save(clientEntity);
			this.tokenService.invalidLastToken(credentialEntity);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The client could not verify account.", exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.UPDATE);
		}
	}

	@Override
	public void reactiveClient(final String email) throws DataBaseException, NotFoundException {
		final var credentialEntity = this.securityHelper.getCredentialEntity(email);
		try {
			credentialEntity.setActive(true);
			this.credentialRepository.save(credentialEntity);
			this.tokenService.invalidLastToken(credentialEntity);
		} catch (final Exception exception) {
			ClientServiceImplementation.log.error("The client could not reactive account.", exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.UPDATE);
		}
	}

	@Override
	public ClientVerifyDataResponse verifyData(final String email, final ClientVerifyDataRequest clientVerifyDataRequest) throws DataBaseException, NotFoundException {
		ClientServiceImplementation.log.info("Starting validate the data if exist for client {}.", email);
		final var clientVerifyDataResponse = new ClientVerifyDataResponse();
		clientVerifyDataResponse.setOk(true);
		final List<String> listDataWrong = new ArrayList<>();

		final var validatePhone = clientVerifyDataRequest.getCountryCode() != null && clientVerifyDataRequest.getPhone() != null && !clientVerifyDataRequest.getPhone().isBlank();
		final var validateRfc = clientVerifyDataRequest.getRfc() != null && !clientVerifyDataRequest.getRfc().isBlank();

		if (validatePhone) {
			final var countryCodeEntity = this.countryCodeHelper.getCountryCodeEntity(clientVerifyDataRequest.getCountryCode());
			ClientServiceImplementation.log.info("Starting validate the phone if exist {} {}.", countryCodeEntity, clientVerifyDataRequest.getPhone());
			final var optionalClientEntity = this.clientRepository.findByCountryCodeEntityAndPhone(countryCodeEntity, clientVerifyDataRequest.getPhone());

			if (optionalClientEntity.isPresent()) {
				final var clientEntity = optionalClientEntity.get();
				final var isSameEmail = clientEntity.getCredentialEntity().getEmail().equals(email);
				clientVerifyDataResponse.setOk(isSameEmail);
				if (!isSameEmail)
					listDataWrong.add("phone");
			}
		}

		if (validateRfc) {
			ClientServiceImplementation.log.info("Starting validate the rfc if exist {}.", clientVerifyDataRequest.getRfc());
			final var optionalClientEntity = this.clientRepository.findByRfcIgnoreCase(clientVerifyDataRequest.getRfc());
			if (optionalClientEntity.isPresent()) {
				final var clientEntity = optionalClientEntity.get();
				final var isSameEmail = clientEntity.getCredentialEntity().getEmail().equals(email);
				clientVerifyDataResponse.setOk(isSameEmail);
				if (!isSameEmail)
					listDataWrong.add("rfc");
			}
		}

		clientVerifyDataResponse.setDataWrong(listDataWrong);
		ClientServiceImplementation.log.info("Finished validate the data if exist for client {}.", email);
		return clientVerifyDataResponse;
	}

	private void validatePhoneNotExist(final CountryCodeEntity countryCodeEntity, final String phone) throws ExistException {
		if (phone == null)
			return;
		ClientServiceImplementation.log.info("Starting validate the phone if exist {} {}.", countryCodeEntity.getCode(), phone);
		final var optionalClientEntity = this.clientRepository.findByCountryCodeEntityAndPhone(countryCodeEntity, phone);
		if (optionalClientEntity.isPresent()) {
			ClientServiceImplementation.log.error("The phone {} {} exist.", countryCodeEntity.getCode(), phone);
			throw new ExistException(Controller.PROFILE_CLIENT, "phone", countryCodeEntity.getCode() + phone);
		}
	}

	private void validateRfcNotExist(final String rfc) throws ExistException {
		if (rfc == null)
			return;
		ClientServiceImplementation.log.info("Starting validate the rfc if exist {}.", rfc);
		final var optionalClientEntity = this.clientRepository.findByRfcIgnoreCase(rfc);
		if (optionalClientEntity.isPresent()) {
			ClientServiceImplementation.log.error("The rfc {} exist.", rfc);
			throw new ExistException(Controller.PROFILE_CLIENT, "rfc", rfc);
		}
	}

}
