package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.FilterDate;

@Data
public class DateFilterRequest {

	@NotNull(message = "date.filter.not.null")
	private FilterDate filterDate;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateStart;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateEnd;

}
