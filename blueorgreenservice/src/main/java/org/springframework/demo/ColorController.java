package org.springframework.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ryan Baxter
 */
@RestController
public class ColorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorController.class);
	private ColorProperties colorProperties;
	private Tracer tracer;

	public ColorController(ColorProperties colorProperties, Tracer tracer) {
		this.colorProperties = colorProperties;
		this.tracer = tracer;
	}

	@GetMapping(path = {"/blueorgreen", "/"})
	public ColorResponse color() throws InterruptedException {
		if (colorProperties.isSlow()) {
			Thread.sleep(5000);
		}

		Color color = Color.valueOf(colorProperties.getColor());
		tracer.currentSpan().tag("color", String.valueOf(color));
		LOGGER.info("Picked color: {}", color);

		return new ColorResponse(color);
	}

	static class ColorResponse {
		private Color id;

		ColorResponse() {}

		public ColorResponse(Color id) { this.id = id; }

		public Color getId() {
			return this.id;
		}
	}

	enum Color {
		green,
		blue,
		yellow;
	}
}
