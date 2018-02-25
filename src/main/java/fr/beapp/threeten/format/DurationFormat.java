package fr.beapp.threeten.format;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DurationFormat {

    private static final String BUNDLE_NAME = "fr.beapp.threeten.format.messages";
    private static final ConcurrentMap<Locale, DurationFormatter> FORMATTERS = new ConcurrentHashMap<>();

    private DurationFormat() {
    }

    public static DurationFormatter wordBased(Locale locale) {
        DurationFormatter pf = FORMATTERS.get(locale);
        if (pf == null) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            pf = wordBased(resourceBundle, locale);
            DurationFormatter existing = FORMATTERS.putIfAbsent(locale, pf);
            if (existing != null) {
                pf = existing;
            }
        }
        return pf;
    }

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
