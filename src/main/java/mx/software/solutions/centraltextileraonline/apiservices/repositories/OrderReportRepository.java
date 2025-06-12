package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.math.BigDecimal;
import java.util.Date;

public interface OrderReportRepository {

	BigDecimal sumTotal(Date dateStart, Date dateEnd);

}
