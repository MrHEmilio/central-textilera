package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ReportClothMostSoldEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ReportClothSoldOutEntity;

public interface ClothReportRepository {

	long countByActive();

	Page<ReportClothMostSoldEntity> findByMostSold(Date dateStart, Date dateEnd, Pageable pageable);

	Page<ReportClothSoldOutEntity> findBySoldOut(Pageable pageable);

}
