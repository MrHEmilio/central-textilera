package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetResponse<T> {

	private final List<T> content;
	private final PaginationResponse pagination;

}
