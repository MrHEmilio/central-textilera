package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerHistoryEntity;

public interface BannerHistoryRepository extends PagingAndSortingRepository<BannerHistoryEntity, UUID> {

	Page<BannerHistoryEntity> findAllByBannerEntity(BannerEntity bannerEntity, Pageable pageable);

}
