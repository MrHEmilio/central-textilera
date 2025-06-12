package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.PaginationResponse;

@Component
public class PaginationHelper {

	public Pageable getPageable(final PaginationRequest paginationRequest) {
		final var page = paginationRequest.getPage() - 1;
		final var size = paginationRequest.getSize();
		final var columnSort = paginationRequest.getColumnSort();
		final var typeSort = paginationRequest.getTypeSort();
		return columnSort == null || columnSort.isEmpty() ? PageRequest.of(page, size) : PageRequest.of(page, size, Sort.by(typeSort, columnSort));
	}

	public PaginationResponse getPaginationResponse(final Page<?> page) {
		final var pageActual = page.getPageable().getPageNumber() + 1;
		final var size = page.getSize();
		final var totalPage = page.getTotalPages();
		final var totalRecords = (int) page.getTotalElements();
		return new PaginationResponse(pageActual, size, totalPage, totalRecords);
	}

}
