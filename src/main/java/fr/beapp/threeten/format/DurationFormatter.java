package fr.beapp.threeten.format;

import org.threeten.bp.Duration;

import java.util.List;
import java.util.Locale;

public class DurationFormatter {

	private final List<DurationFormatterBuilder.DurationPrinter> printers;
	private Locale locale;

	public DurationFormatter(List<DurationFormatterBuilder.DurationPrinter> printers, Locale locale) {
		this.printers = printers;
		this.locale = locale;
	}

	public DurationFormatter withLocale(Locale locale) {
		this.locale = locale;
		return this;
	}

	public String format(Duration duration) {
		StringBuilder builder = new StringBuilder();
		for (DurationFormatterBuilder.DurationPrinter printer : printers) {
			printer.print(printers, duration, locale, builder);
		}
		return builder.toString();
	}

}
