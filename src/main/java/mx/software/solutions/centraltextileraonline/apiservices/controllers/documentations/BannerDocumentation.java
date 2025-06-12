package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BannerUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BannerResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Banner", description = "Endpoints to manage the banners show in Central Textilera Ecommerce.")
public interface BannerDocumentation {

	@Operation(summary = "Create banner", description = "Create banner of Central Textilera Ecommerce.")
	CrudResponse<BannerResponse> createBanner(BannerCreateRequest bannerCreateRequest) throws DataBaseException, ImageInvalidException;

	@Operation(summary = "Update banner", description = "Update banner of Central Textilera Ecommerce.")
	CrudResponse<BannerResponse> updateBanner(BannerUpdateRequest bannerUpdateRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Reactivate banner", description = "Reactivate banner of Central Textilera Ecommerce.")
	CrudResponse<BannerResponse> reactivateBanner(@PathVariable final UUID banner) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete banner", description = "Delete banner of Central Textilera Ecommerce.")
	CrudResponse<BannerResponse> deleteBanner(UUID banner) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all banner", description = "Get all banner of Central Textilera Ecommerce.")
	GetResponse<BannerResponse> getAllBanner(PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get banner history", description = "Get banner history of Central Textilera Ecommerce.")
	GetResponse<BannerHistoryResponse> getBannerHistory(UUID banner, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
