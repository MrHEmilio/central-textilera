package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

	@NotNull(message = "pagination.page.not.null")
	@Positive(message = "pagination.page.positive")
	private int page;

	@NotNull(message = "pagination.size.not.null")
	@Positive(message = "pagination.size.positive")
	private int size;

	private String columnSort;

	private Direction typeSort;

}
