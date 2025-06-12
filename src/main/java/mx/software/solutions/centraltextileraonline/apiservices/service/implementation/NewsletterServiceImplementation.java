package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.NewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.VerifyNewsletterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.NewsletterEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.NewsletterRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.NewsletterService;

@Slf4j
@Service
public class NewsletterServiceImplementation implements NewsletterService {

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Override
	public NewsletterResponse subscribeNewsletter(final String email) throws DataBaseException, ExistException {
		NewsletterServiceImplementation.log.info("Starting subscribed newsletter with the email {}.", email);
		final var newsletterInactive = this.getNewsletterInactive(email);
		var newsletterEntity = new NewsletterEntity();
		if (newsletterInactive != null)
			newsletterEntity = newsletterInactive;

		try {
			newsletterEntity.setEmail(email);
			newsletterEntity.setActive(true);
			final var newNewsletterEntity = this.newsletterRepository.save(newsletterEntity);
			NewsletterServiceImplementation.log.info("Finished subscribed newsletter with the email {}.", email);
			return new NewsletterResponse(newNewsletterEntity.getEmail());
		} catch (final Exception exception) {
			NewsletterServiceImplementation.log.error("The subscribed newsletter with email {} could not been create.", email, exception);
			throw new DataBaseException(Controller.EMAIL_NEWSLETTER, DataBaseActionType.CREATE, email);
		}
	}

	@Override
	public NewsletterResponse unsubscribeNewsletter(final String email) throws DataBaseException, NotFoundException {
		NewsletterServiceImplementation.log.info("Starting unsubscribed newsletter with the email {}.", email);
		final var newsletterEntity = this.getNewsletterEntity(email);
		newsletterEntity.setActive(false);
		try {
			final var newNewsletterEntity = this.newsletterRepository.save(newsletterEntity);
			final var newsletterResponse = new NewsletterResponse(newNewsletterEntity.getEmail());
			NewsletterServiceImplementation.log.info("Finished unsubscribed newsletter with the email {}.", email);
			return newsletterResponse;
		} catch (final Exception exception) {
			NewsletterServiceImplementation.log.error("The subscribed newsletter with email {} could not been delete.", email, exception);
			throw new DataBaseException(Controller.EMAIL_NEWSLETTER, DataBaseActionType.DELETE, email);
		}
	}

	@Override
	public VerifyNewsletterResponse isSubscribeNewsletter(final String email) throws DataBaseException, NotFoundException {
		NewsletterServiceImplementation.log.info("Starting verify if email {} has newsletter.", email);
		Optional<NewsletterEntity> optionalNewsletterEntity = null;
		try {
			optionalNewsletterEntity = this.newsletterRepository.findByEmailIgnoreCase(email);
		} catch (final Exception exception) {
			NewsletterServiceImplementation.log.error("The subscribed newsletter with email {} could not been read.", email, exception);
			throw new DataBaseException(Controller.EMAIL_NEWSLETTER, DataBaseActionType.READ, email);
		}
		if (optionalNewsletterEntity.isEmpty()) {
			NewsletterServiceImplementation.log.error("The newsletter not found.");
			throw new NotFoundException(Controller.EMAIL_NEWSLETTER, "email", email);
		}
		final var newsletterEntity = optionalNewsletterEntity.get();
		NewsletterServiceImplementation.log.info("Finished verify if email {} has newsletter.", email);
		return new VerifyNewsletterResponse(newsletterEntity.isActive(), newsletterEntity.getEmail());
	}

	private NewsletterEntity getNewsletterEntity(final String email) throws DataBaseException, NotFoundException {
		Optional<NewsletterEntity> optionalNewslatterEntity = Optional.empty();
		try {
			NewsletterServiceImplementation.log.info("Starting searched the newsletter with the email {}.", email);
			optionalNewslatterEntity = this.newsletterRepository.findByEmailIgnoreCase(email);
			if (optionalNewslatterEntity.isEmpty()) {
				NewsletterServiceImplementation.log.error("The newsletter not found with the email {}.", email);
				throw new NotFoundException(Controller.EMAIL_NEWSLETTER, "id", email);
			}
			NewsletterServiceImplementation.log.info("Finished search the newsletter with the email {}.", email);
			return optionalNewslatterEntity.get();
		} catch (final Exception exception) {
			NewsletterServiceImplementation.log.error("The newsletter with the id {} could not read.", email, exception);
			throw new DataBaseException(Controller.EMAIL_NEWSLETTER, DataBaseActionType.READ, email.toString());
		}
	}

	private NewsletterEntity getNewsletterInactive(final String email) throws ExistException {
		final var optionalNewslatterEntity = this.newsletterRepository.findByEmailIgnoreCase(email);
		if (optionalNewslatterEntity.isPresent()) {
			if (optionalNewslatterEntity.get().isActive())
				throw new ExistException(Controller.EMAIL_NEWSLETTER, "email", email);
			return optionalNewslatterEntity.get();
		}
		return null;
	}

}
