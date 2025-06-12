package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BannerRepository;

@Slf4j
@Component
public class BannerHelper {

	@Autowired
	private BannerRepository bannerRepository;

	@Autowired
	private ImagesHelper imagesHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Value("${url.images.banners}")
	private String urlImagesBanners;

	public BannerEntity getBannerEntity(final UUID banner) throws DataBaseException, NotFoundException {
		Optional<BannerEntity> optionalBannerEntity = Optional.empty();
		try {
			BannerHelper.log.info("Starting searched the banner with the id {}.", banner);
			optionalBannerEntity = this.bannerRepository.findById(banner);
		} catch (final Exception exception) {
			BannerHelper.log.error("The banner could not read with the id {}.", banner, exception);
			throw new DataBaseException(Controller.CATALOG_BANNER, DataBaseActionType.READ, banner.toString());
		}

		if (optionalBannerEntity.isEmpty()) {
			BannerHelper.log.error("The banner not found with the id {}.", banner);
			throw new NotFoundException(Controller.CATALOG_BANNER, "id", banner.toString());
		}
		BannerHelper.log.info("Finished search the banner with the id {}.", banner);
		return optionalBannerEntity.get();
	}

	public BannerResponse convertBanner(final BannerEntity bannerEntity) {
		final var id = bannerEntity.getId();
		final var image = this.imagesHelper.getUrlImage(this.urlImagesBanners, bannerEntity.getId());
		final var description = bannerEntity.getDescription();
		final var waitTime = bannerEntity.getWaitTime();
		final var isActive = bannerEntity.isActive();
		return new BannerResponse(id, image, description, waitTime, isActive);
	}

	public BannerHistoryResponse convertBannerHistory(final BannerHistoryEntity bannerHistoryEntity) {
		final var id = bannerHistoryEntity.getId();
		final var bannerResponse = this.convertBanner(bannerHistoryEntity.getBannerEntity());
		final var adminResponse = this.adminHelper.convertAdmin(bannerHistoryEntity.getAdminEntity());
		final var actionType = bannerHistoryEntity.getActionType();
		final var date = bannerHistoryEntity.getDate();
		final var object = bannerHistoryEntity.getObject();
		return new BannerHistoryResponse(id, bannerResponse, adminResponse, actionType, date, object);
	}

}
