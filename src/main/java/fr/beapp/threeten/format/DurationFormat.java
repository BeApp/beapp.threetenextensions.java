package fr.beapp.threeten.format;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides formatter for {@link org.threeten.bp.Duration} classes.
 * This was inspired from JodaTime's {@code PeriodFormat} class.
 */
public class DurationFormat {

    private static final String BUNDLE_NAME = "fr.beapp.threeten.format.messages";
    private static final ConcurrentMap<Locale, DurationFormatter> WORD_FORMATTERS = new ConcurrentHashMap<>();

    private DurationFormat() {
    }

    /**
     * Returns a short formatter for the specified locale.
     * <p>
     * Format examples:
     * <ul>
     * <li>All locales: 01:02:03</li>
     * </ul>
     *
     * @return the formatter, not null
     */
    public static DurationFormatter shortBased() {
        return new DurationFormatterBuilder()
                .appendHours()
                .valueFormat(true, "%02d")
                .appendLiteral(":")
                .appendMinutes()
                .valueFormat(true, "%02d")
                .appendLiteral(":")
                .appendSeconds()
                .valueFormat(true, "%02d")
                .toFormatter();
    }

    /**
     * Returns a word based formatter for the specified locale.
     * <p>
     * The words are configured in a resource bundle text file -
     * {@code fr.beapp.threeten.format.messages}.
     * This can be added to via the normal classpath resource bundle mechanisms.
     * <p>
     * You can add your own translation by creating messages_<locale>.properties file
     * and adding it to the {@code fr.beapp.threeten.format.messages} path.
     * <p>
     * Format examples:
     * <ul>
     * <li>English: 1 year, 2 months, 3 days, 4 hours and 5 minutes</li>
     * <li>French: 1 année, 2 mois, 3 jours, 4 heurs et 5 minutes</li>
     * </ul>
     *
     * @return the formatter, not null
     */
    public static DurationFormatter wordBased(Locale locale) {
        DurationFormatter pf = WORD_FORMATTERS.get(locale);
        if (pf == null) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            pf = wordBased(resourceBundle, locale);
            DurationFormatter existing = WORD_FORMATTERS.putIfAbsent(locale, pf);
            if (existing != null) {
                pf = existing;
            }
        }
        return pf;
    }

    /**
     * Returns a word based formatter for the specified locale.
     * <p>
     * The words are configured the given resource bundle.
     *
     * @return the formatter, not null
     */
    public static DurationFormatter wordBased(ResourceBundle b, Locale locale) {
        String[] variants = retrieveVariants(b);
        String commaspace = b.getString("PeriodFormat.commaspace");
        String spaceandspace = b.getString("PeriodFormat.spaceandspace");

        return new DurationFormatterBuilder()
                .appendYears()
                .appendSuffix(b.getString("PeriodFormat.year"), b.getString("PeriodFormat.years"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendMonths()
                .appendSuffix(b.getString("PeriodFormat.month"), b.getString("PeriodFormat.months"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendWeeks()
                .appendSuffix(b.getString("PeriodFormat.week"), b.getString("PeriodFormat.weeks"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendDays()
                .appendSuffix(b.getString("PeriodFormat.day"), b.getString("PeriodFormat.days"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendHours()
                .appendSuffix(b.getString("PeriodFormat.hour"), b.getString("PeriodFormat.hours"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendMinutes()
                .appendSuffix(b.getString("PeriodFormat.minute"), b.getString("PeriodFormat.minutes"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendSeconds()
                .appendSuffix(b.getString("PeriodFormat.second"), b.getString("PeriodFormat.seconds"))
                .appendSeparator(commaspace, spaceandspace, variants)
                .appendMillis()
                .appendSuffix(b.getString("PeriodFormat.millisecond"), b.getString("PeriodFormat.milliseconds"))
                .toFormatter()
                .withLocale(locale);
    }

    private static String[] retrieveVariants(ResourceBundle b) {
        return new String[]{
                b.getString("PeriodFormat.space"), b.getString("PeriodFormat.comma"),
                b.getString("PeriodFormat.commandand"), b.getString("PeriodFormat.commaspaceand")
        };
    }

}
