package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.BannerHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ImagesHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BannerHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BannerRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.BannerService;

@Slf4j
@Service
public class BannerServiceImplementation implements BannerService {

	@Autowired
	private BannerRepository bannerRepository;

	@Autowired
	private BannerHistoryRepository bannerHistoryRepository;

	@Autowired
	private ImagesHelper imagesHelper;

	@Autowired
	private BannerHelper bannerHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Value("${path.images.banners}")
	private String pathImagesBanners;

	@Value("${url.images.banners}")
	private String urlImagesBanners;

	@Override
	public BannerResponse createBanner(final BannerCreateRequest bannerCreateRequest, final UUID admin) throws DataBaseException, ImageInvalidException {
		BannerServiceImplementation.log.info("Starting created the banner.");
		this.imagesHelper.verifyImage(Controller.CATALOG_BANNER, bannerCreateRequest.getImage());
		try {
			final var optionalLastBannerEntity = this.bannerRepository.findFirstByOrderByOrderDesc();
			var order = 1;
			if (optionalLastBannerEntity.isPresent())
				order = optionalLastBannerEntity.get().getOrder() + 1;

			final var bannerEntity = new BannerEntity();
			bannerEntity.setDescription(bannerCreateRequest.getDescription());
			bannerEntity.setWaitTime(bannerCreateRequest.getWaitTime());
			bannerEntity.setOrder(order);
			bannerEntity.setActive(true);
			final var newBannerEntity = this.bannerRepository.save(bannerEntity);
			BannerServiceImplementation.log.info("Finished create the banner.");

			BannerServiceImplementation.log.info("Starting saved the banner image.");
			this.imagesHelper.saveImage(bannerCreateRequest.getImage(), this.pathImagesBanners, newBannerEntity.getId());
			BannerServiceImplementation.log.info("Finished save the banner image.");

			BannerServiceImplementation.log.info("Starting created the create history {}.");
			this.createBannerHistory(newBannerEntity, admin, DataBaseActionType.CREATE);
			BannerServiceImplementation.log.info("Finished create the create history {}.");

			return this.bannerHelper.convertBanner(bannerEntity);
		} catch (final Exception exception) {
			BannerServiceImplementation.log.error("The banner could not been save.", exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.CREATE);
		}
	}

	@Override
	public BannerResponse updateBanner(final BannerUpdateRequest bannerUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException {
		BannerServiceImplementation.log.info("Starting updated the banner with id {}.", bannerUpdateRequest.getId());
		final var bannerEntity = this.bannerHelper.getBannerEntity(bannerUpdateRequest.getId());
		bannerEntity.setDescription(bannerUpdateRequest.getDescription());
		bannerEntity.setWaitTime(bannerUpdateRequest.getWaitTime());
		bannerEntity.setActive(true);
		try {
			final var newBannerEntity = this.bannerRepository.save(bannerEntity);

			BannerServiceImplementation.log.info("Finished update the banner with the id {}.", bannerUpdateRequest.getId());

			BannerServiceImplementation.log.info("Starting created the update history of id {}.", bannerUpdateRequest.getId());
			this.createBannerHistory(newBannerEntity, admin, DataBaseActionType.UPDATE);
			BannerServiceImplementation.log.info("Finished create the update history of id {}.", bannerUpdateRequest.getId());

			return this.bannerHelper.convertBanner(bannerEntity);
		} catch (final Exception exception) {
			BannerServiceImplementation.log.error("The banner could not been update with the id {}.", bannerUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.UPDATE, bannerUpdateRequest.getId().toString());
		}
	}

	@Override
	public BannerResponse reactivateBanner(final UUID banner, final UUID admin) throws DataBaseException, NotFoundException {
		BannerServiceImplementation.log.info("Starting reactivated the banner with id {}.", banner);
		final var bannerEntity = this.bannerHelper.getBannerEntity(banner);
		bannerEntity.setActive(true);
		try {
			final var newBannerEntity = this.bannerRepository.save(bannerEntity);

			BannerServiceImplementation.log.info("Finished reactivate the banner with the id {}.", banner);

			BannerServiceImplementation.log.info("Starting created the reactivate history of id {}.", banner);
			this.createBannerHistory(newBannerEntity, admin, DataBaseActionType.REACTIVATE);
			BannerServiceImplementation.log.info("Finished create the reactivate history of id {}.", banner);

			return this.bannerHelper.convertBanner(bannerEntity);
		} catch (final Exception exception) {
			BannerServiceImplementation.log.error("The banner could not been reactivate with the id {}.", banner, exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.REACTIVATE, banner.toString());
		}
	}

	@Override
	public BannerResponse deleteBanner(final UUID banner, final UUID admin) throws DataBaseException, NotFoundException {
		BannerServiceImplementation.log.info("Starting deleted the banner with the id {}.", banner);
		final var bannerEntity = this.bannerHelper.getBannerEntity(banner);
		bannerEntity.setActive(false);
		try {
			final var newBannerEntity = this.bannerRepository.save(bannerEntity);
			BannerServiceImplementation.log.info("Finished delete the banner with the id {}.", banner);

			BannerServiceImplementation.log.info("Starting created the delete history of id {}.", banner);
			this.createBannerHistory(newBannerEntity, admin, DataBaseActionType.DELETE);
			BannerServiceImplementation.log.info("Finished create the delete history of id {}.", banner);

			return this.bannerHelper.convertBanner(newBannerEntity);
		} catch (final Exception exception) {
			BannerServiceImplementation.log.error("The banner could not been delete with the id {}.", banner, exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.DELETE, banner.toString());
		}
	}

	@Override
	public GetResponse<BannerResponse> getAllBanner(final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		BannerServiceImplementation.log.info("Starting searched of all banners.");
		Page<BannerEntity> pageBannerEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			if (this.sessionHelper.isAdmin())
				pageBannerEntity = this.bannerRepository.findAllByOrderByOrder(pageable);
			else
				pageBannerEntity = this.bannerRepository.findAllByActiveTrueOrderByOrder(pageable);
		} catch (final Exception exception) {
			BannerServiceImplementation.log.error("The banner could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.READ);
		}
		final var listBannerResponse = pageBannerEntity.get().map(this.bannerHelper::convertBanner).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageBannerEntity);
		if (listBannerResponse.isEmpty()) {
			BannerServiceImplementation.log.error("The banners not found.");
			throw new NotFoundException(Controller.CATALOG_BANNER, "all");
		}
		BannerServiceImplementation.log.info("Finished search of all banners.");
		return new GetResponse<>(listBannerResponse, paginationResponse);
	}

	@Override
	public GetResponse<BannerHistoryResponse> getBannerHistory(final UUID banner, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		BannerServiceImplementation.log.info("Starting searched history of banner with the id {}.", banner);
		final var bannerEntity = this.bannerHelper.getBannerEntity(banner);
		Page<BannerHistoryEntity> pageBannerHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageBannerHistoryEntity = this.bannerHistoryRepository.findAllByBannerEntity(bannerEntity, pageable);
		} catch (final Exception exception) {
			BannerServiceImplementation.log.error("The history banner could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.READ);
		}
		final var listBannerHistoryResponse = pageBannerHistoryEntity.get().map(this.bannerHelper::convertBannerHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageBannerHistoryEntity);
		BannerServiceImplementation.log.info("Finished search history of banner with the id {}.", banner);
		return new GetResponse<>(listBannerHistoryResponse, paginationResponse);
	}

	private void createBannerHistory(final BannerEntity bannerEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var bannerHistoryEntity = new BannerHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(bannerEntity);
			bannerHistoryEntity.setBannerEntity(bannerEntity);
			bannerHistoryEntity.setAdminEntity(adminEntity);
			bannerHistoryEntity.setActionType(dataBaseActionType);
			bannerHistoryEntity.setDate(new Date());
			bannerHistoryEntity.setObject(object);
			this.bannerHistoryRepository.save(bannerHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
