package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;

@Component
public class DateHelper {

	public void setDateFilter(final DateFilterRequest dateFilterRequest) {
		final var today = LocalDate.now();
		switch (dateFilterRequest.getFilterDate()) {
		case ALWAYS:
			dateFilterRequest.setDateStart(null);
			dateFilterRequest.setDateEnd(null);
			break;
		case TODAY:
			final var tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_YEAR, 1);
			dateFilterRequest.setDateStart(this.convertDate(today));
			dateFilterRequest.setDateEnd(tomorrow.getTime());
			break;
		case THIS_WEEK:
			final var firstDayWeek = today.with(DayOfWeek.SUNDAY).minusDays(7L);
			final var lastDayWeek = today.with(DayOfWeek.SATURDAY).plusDays(1L);
			dateFilterRequest.setDateStart(this.convertDate(firstDayWeek));
			dateFilterRequest.setDateEnd(this.convertDate(lastDayWeek));
			break;
		case THIS_MONTH:
			final var firstDayMonth = today.with(TemporalAdjusters.firstDayOfMonth());
			final var lastDayMonth = today.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1L);
			dateFilterRequest.setDateStart(this.convertDate(firstDayMonth));
			dateFilterRequest.setDateEnd(this.convertDate(lastDayMonth));
			break;
		case THIS_YEAR:
			final var firstDayYear = today.with(TemporalAdjusters.firstDayOfYear());
			final var lastDayYear = today.with(TemporalAdjusters.lastDayOfYear()).plusDays(1L);
			dateFilterRequest.setDateStart(this.convertDate(firstDayYear));
			dateFilterRequest.setDateEnd(this.convertDate(lastDayYear));
			break;
		case RANGE:
			break;
		}
	}

	private Date convertDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
