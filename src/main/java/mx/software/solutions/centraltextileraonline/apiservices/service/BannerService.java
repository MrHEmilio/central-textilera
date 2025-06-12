package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface BannerService {

	BannerResponse createBanner(BannerCreateRequest bannerCreateRequest, UUID admin) throws DataBaseException, ImageInvalidException;

	BannerResponse updateBanner(BannerUpdateRequest bannerUpdateRequest, UUID admin) throws DataBaseException, NotFoundException;

	BannerResponse reactivateBanner(UUID banner, UUID admin) throws DataBaseException, NotFoundException;

	BannerResponse deleteBanner(UUID banner, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<BannerResponse> getAllBanner(PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<BannerHistoryResponse> getBannerHistory(UUID banner, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
