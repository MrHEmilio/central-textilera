package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;

public interface CountryCodeSearchRepository {

	Page<CountryCodeEntity> findAll(String search, Pageable pageable);

}
