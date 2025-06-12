package mx.software.solutions.centraltextileraonline.apiservices.enumerations;

public enum Controller {

	CATALOG_BANNER(Api.CATALOG, "banner"),
	CATALOG_BOX(Api.CATALOG, "box"),
	CATALOG_CLOTH(Api.CATALOG, "cloth"),
	CATALOG_COLLECTION(Api.CATALOG, "collection"),
	CATALOG_FREIGHTER(Api.CATALOG, "freighter"),
	CATALOG_COLOR(Api.CATALOG, "color"),
	CATALOG_FIBER(Api.CATALOG, "fiber"),
	CATALOG_USE(Api.CATALOG, "use"),
	CATALOG_SALE(Api.CATALOG, "sale"),
	CATALOG_SUBURB(Api.CATALOG, "suburb"),
	CATALOG_COUNTRY_CODE(Api.CATALOG, "country.code"),
	CATALOG_EMAIL_NEWSLETTER_TEMPLATE(Api.CATALOG, "email.newsletter.template"),

	EMAIL_SEND_EMAIL(Api.EMAIL, "send.email"),
	EMAIL_NEWSLETTER(Api.EMAIL, "newsletter"),

	PAYMENT_CALCULATE(Api.PAYMENT, "calculate"),
	PAYMENT_ORDER(Api.PAYMENT, "order"),
	PAYMENT_BILLING(Api.PAYMENT, "billing"),

	SHIPPING_TRACKING(Api.SHIPPING, "tracking"),

	PROFILE_CLIENT(Api.PROFILE, "client"),
	PROFILE_SECURITY(Api.PROFILE, "security"),
	PROFILE_CLIENT_ADDRESS(Api.PROFILE, "client.address"),
	PROFILE_ADMIN(Api.PROFILE, "admin"),

	REPORT_CLIENT(Api.REPORT, "client"),
	REPORT_CLOTH(Api.REPORT, "cloth"),
	REPORT_ORDER(Api.REPORT, "order");

	private Api api;
	private String keyLang;

	Controller(final Api api, final String keyLang) {
		this.api = api;
		this.keyLang = keyLang;
	}

	public Api getApi() {
		return this.api;
	}

	public String getKeyLang() {
		return this.keyLang;
	}

}
