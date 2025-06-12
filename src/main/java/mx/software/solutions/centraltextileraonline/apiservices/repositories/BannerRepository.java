package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.BannerEntity;

public interface BannerRepository extends PagingAndSortingRepository<BannerEntity, UUID> {

	Optional<BannerEntity> findFirstByOrderByOrderDesc();

	Page<BannerEntity> findAllByOrderByOrder(Pageable pageable);

	Page<BannerEntity> findAllByActiveTrueOrderByOrder(Pageable pageable);

}
