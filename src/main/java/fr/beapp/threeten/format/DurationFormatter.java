package fr.beapp.threeten.format;

import org.threeten.bp.Duration;

import java.util.List;
import java.util.Locale;

/**
 * Controls the formatting for {@link Duration} classes.
 * This was inspired from JodaTime's {@code PeriodFormatter} class.
 */
public class DurationFormatter {

    private final List<DurationFormatterBuilder.DurationPrinter> printers;
    private final Locale locale;

    /**
     * Creates a new formatter, however you will normally use the factory or the builder.
     *
     * @param printers the internal printers, not null
     * @param locale   the locale to user, not null
     */
    public DurationFormatter(List<DurationFormatterBuilder.DurationPrinter> printers, Locale locale) {
        this.printers = printers;
        this.locale = locale;
    }

    /**
     * Returns a new formatter with a different locale that will be used for printing.
     * <p>
     * A PeriodFormatter is immutable, so a new instance is returned and the original is unaltered and still usable.
     * <p>
     * A null locale indicates that no specific locale override is in use.
     *
     * @param locale the locale to use, not null
     * @return the new formatter
     */
    public DurationFormatter withLocale(Locale locale) {
        if (locale.equals(getLocale())) {
            return this;
        }
        return new DurationFormatter(printers, locale);
    }

    /**
     * Gets the locale that will be used for printing.
     *
     * @return the locale to use
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Format a {@link Duration} to a new String.
     *
     * @param duration the duration to format, not null
     * @return the formatted result
     */
    public String format(Duration duration) {
        StringBuilder builder = new StringBuilder();
        for (DurationFormatterBuilder.DurationPrinter printer : printers) {
            printer.print(printers, duration, locale, builder);
        }
        return builder.toString();
    }

}
