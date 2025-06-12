package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Component;

import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;

@Component
public class ImagesHelper {

	public void verifyImage(final Controller controller, final String imageBase64) throws ImageInvalidException {
		try {
			Base64.getDecoder().decode(imageBase64.split(",")[1]);
		} catch (final Exception exception) {
			throw new ImageInvalidException(controller);
		}
	}

	public void saveImage(final String imageBase64, final String pathSave, final UUID id) {
		try {
			final var path = Paths.get(pathSave + id + ".jpg");
			final var byteImage = Base64.getDecoder().decode(imageBase64.split(",")[1]);
			Files.write(path, byteImage);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public String getUrlImage(final String url, final UUID id) {
		return url + id + ".jpg?" + UUID.randomUUID().toString();
	}

}
