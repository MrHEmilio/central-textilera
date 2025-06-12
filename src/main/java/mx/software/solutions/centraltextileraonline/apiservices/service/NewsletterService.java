package mx.software.solutions.centraltextileraonline.apiservices.service;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.NewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.VerifyNewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface NewsletterService {

	NewsletterResponse subscribeNewsletter(String email) throws DataBaseException, ExistException;

	NewsletterResponse unsubscribeNewsletter(String email) throws DataBaseException, NotFoundException;

	VerifyNewsletterResponse isSubscribeNewsletter(String email) throws DataBaseException, NotFoundException;

}
