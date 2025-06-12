package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.NewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.VerifyNewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Newsletter", description = "Endpoints to manage the newsletter email in Central Textilera Ecommerce.")
public interface NewsletterDocumentation {

	@Operation(summary = "Subscribe newsletter", description = "Subscribe email to newsletters of Central Textilera Ecommerce.")
	NewsletterResponse subscribeNewsletter(String email) throws DataBaseException, ExistException;

	@Operation(summary = "Unsubscribe newsletter", description = "Unsubscribe email to newsletters of Central Textilera Ecommerce.")
	NewsletterResponse unsubscribeNewsletter(String email) throws DataBaseException, NotFoundException;

	@Operation(summary = "Verify if email is subscribe to newsletter", description = "Verify if email is subscribe to newsletter of Central Textilera Ecommerce.")
	VerifyNewsletterResponse isSubscribeNewsletter(String email) throws DataBaseException, NotFoundException;
}
