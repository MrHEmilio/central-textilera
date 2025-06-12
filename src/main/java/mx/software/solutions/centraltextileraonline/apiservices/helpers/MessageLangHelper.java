package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ApiRestTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;

@Component
public class MessageLangHelper {

	private final ResourceBundleMessageSource resourceBundleMessageSource;

	public MessageLangHelper() {
		this.resourceBundleMessageSource = new ResourceBundleMessageSource();
		this.resourceBundleMessageSource.addBasenames("lang/responses", "lang/exceptions", "lang/validations");
		this.resourceBundleMessageSource.setDefaultEncoding("UTF-8");
	}

	private String createUrlLang(final Controller controller, final MessageLangType messageLangType, final String keyMessage) {
		final var name = new StringBuilder();
		name.append("lang");
		name.append(".");
		name.append(controller.getApi().name().toLowerCase());
		name.append(".");
		name.append(messageLangType.name().toLowerCase());
		name.append(".");
		name.append(controller.getKeyLang());
		name.append(".");
		name.append(keyMessage);

		return name.toString();
	}

	private String createUrlLang(final ApiRestTemplate apiRestTemplate, final MessageLangType messageLangType, final String keyMessage) {
		final var name = new StringBuilder();
		name.append("lang");
		name.append(".");
		name.append(messageLangType.name().toLowerCase());
		name.append(".api.");
		name.append(apiRestTemplate.name().toLowerCase());
		name.append(".");
		name.append(keyMessage);

		return name.toString();
	}

	public String getMessageLang(final Controller controller, final MessageLangType messageLangType, final DataBaseActionType dataBaseActionType, final Object... params) {
		final var urlLang = this.createUrlLang(controller, messageLangType, dataBaseActionType.name().toLowerCase());
		return this.resourceBundleMessageSource.getMessage(urlLang, params, new Locale("es"));
	}

	public String getMessageLang(final Controller controller, final MessageLangType messageLangType, final String key, final Object... params) {
		final var urlLang = this.createUrlLang(controller, messageLangType, key);
		return this.resourceBundleMessageSource.getMessage(urlLang, params, new Locale("es"));
	}

	public String getMessageLang(final ApiRestTemplate apiRestTemplate, final MessageLangType messageLangType, final String key, final Object... params) {
		final var urlLang = this.createUrlLang(apiRestTemplate, messageLangType, key);
		return this.resourceBundleMessageSource.getMessage(urlLang, params, new Locale("es"));
	}

	public String getMessageLang(final String key) {
		final var urlLang = "lang.validation." + key;
		return this.resourceBundleMessageSource.getMessage(urlLang, null, new Locale("es"));
	}

}
