package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.NewsletterDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.NewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.VerifyNewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.service.NewsletterService;

@RestController
@RequestMapping("/newsletter/{email}")
public class NewsletterController implements NewsletterDocumentation {

	@Autowired
	private NewsletterService newsletterService;

	@Override
	@PostMapping
	public NewsletterResponse subscribeNewsletter(@PathVariable final String email) throws DataBaseException, ExistException {
		return this.newsletterService.subscribeNewsletter(email);
	}

	@Override
	@DeleteMapping
	public NewsletterResponse unsubscribeNewsletter(@PathVariable final String email) throws DataBaseException, NotFoundException {
		return this.newsletterService.unsubscribeNewsletter(email);
	}

	@Override
	@GetMapping
	public VerifyNewsletterResponse isSubscribeNewsletter(@PathVariable final String email) throws DataBaseException, NotFoundException {
		return this.newsletterService.isSubscribeNewsletter(email);
	}

}
