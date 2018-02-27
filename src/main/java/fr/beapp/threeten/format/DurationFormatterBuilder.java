package fr.beapp.threeten.format;

import fr.beapp.threeten.DurationUtils;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Factory that creates complex instances of {@link DurationFormatter} via method calls.
 * <p>
 * This was inspired from JodaTime's {@code PeriodFormatterBuilder} class.
 */
public class DurationFormatterBuilder {

    private List<DurationPrinter> printers = new LinkedList<>();

    /**
     * Instruct the formatter to emit an integer years field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendYears() {
        printers.add(new FieldValuePrinter(ChronoUnit.YEARS));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer months field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendMonths() {
        printers.add(new FieldValuePrinter(ChronoUnit.MONTHS));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer weeks field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendWeeks() {
        printers.add(new FieldValuePrinter(ChronoUnit.WEEKS));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer days field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendDays() {
        printers.add(new FieldValuePrinter(ChronoUnit.DAYS));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer hours field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendHours() {
        printers.add(new FieldValuePrinter(ChronoUnit.HOURS));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer minutes field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendMinutes() {
        printers.add(new FieldValuePrinter(ChronoUnit.MINUTES));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer seconds field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendSeconds() {
        printers.add(new FieldValuePrinter(ChronoUnit.SECONDS));
        return this;
    }

    /**
     * Instruct the formatter to emit an integer millis field.
     * <p>
     * The number of printed digits can be controlled {@link #valueFormat(boolean, String)}.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendMillis() {
        printers.add(new FieldValuePrinter(ChronoUnit.MILLIS));
        return this;
    }

    /**
     * Define if the formatter should display 0-value for the previous digits printed.
     * Also define the {@link String#format(String, Object...)} to use for printing the previous digits.
     *
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder valueFormat(boolean allowZero, String numberFormat) {
        if (!printers.isEmpty()) {
            int index = printers.size() - 1;
            DurationPrinter lastPrinter = printers.get(index);

            if (lastPrinter instanceof ValuePrinter) {
                printers.set(index, new ValueFormatPrinter((ValuePrinter) lastPrinter, allowZero, numberFormat));
            } else {
                throw new IllegalStateException("Suffix can only be applied after a FieldValuePrinter");
            }
        }
        return this;
    }

    /**
     * Append a field suffix which applies only to the last appended field. If the field is not printed, neither is the suffix.
     *
     * @param suffix custom suffix
     * @return this DurationFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendSuffix(String, String)
     */
    public DurationFormatterBuilder appendSuffix(String suffix) {
        return appendSuffix(suffix, suffix);
    }

    /**
     * Append a field suffix which applies only to the last appended field. If the field is not printed, neither is the suffix.
     * <p>
     * During parsing, the singular and plural versions are accepted whether or not the actual value matches plurality.
     *
     * @param singular text to print if field value is one
     * @param plural   text to print if field value is not one
     * @return this DurationFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     */
    public DurationFormatterBuilder appendSuffix(String singular, String plural) {
        if (!printers.isEmpty()) {
            int index = printers.size() - 1;
            DurationPrinter lastPrinter = printers.get(index);

            if (lastPrinter instanceof ValuePrinter) {
                printers.set(index, new SuffixPrinter((ValuePrinter) lastPrinter, singular, plural));
            } else {
                throw new IllegalStateException("Suffix can only be applied after a FieldValuePrinter");
            }
        }
        return this;
    }

    /**
     * Append a separator, which is output if fields are printed both before and after the separator.
     * <p>
     * This method changes the separator depending on whether it is the last separator to be output.
     * <p>
     * For example, <code>builder.appendDays().appendSeparator(",", "&").appendHours().appendSeparator(",", "&").appendMinutes()</code>
     * will output '1,2&3' if all three fields are output, '1&2' if two fields are output
     * and '1' if just one field is output.
     * <p>
     * The text will be parsed case-insensitively.
     * <p>
     * Note: appending a separator discontinues any further work on the latest appended field.
     *
     * @param text      the text to use as a separator
     * @param finalText the text used used if this is the final separator to be printed
     * @param variants  set of text values which are also acceptable when parsed
     * @return this DurationFormatterBuilder
     */
    public DurationFormatterBuilder appendSeparator(String text, String finalText, String[] variants) {
        printers.add(new SeparatorPrinter(text, finalText, variants));
        return this;
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect it.
     * The parser is case-insensitive.
     *
     * @return this DurationFormatterBuilder
     * @throws IllegalArgumentException if text is null
     */
    public DurationFormatterBuilder appendLiteral(String literal) {
        if (literal == null) {
            throw new IllegalArgumentException("Literal must not be null");
        }
        printers.add(new StringLiteralPrinter(literal));
        return this;
    }

    /**
     * Constructs a {@link DurationFormatter} using all the appended elements and the default Locale.
     * <p>
     * This is the main method used by applications at the end of the build process to create a usable formatter.
     *
     * @return the newly created formatter
     * @see #toFormatter(Locale)
     */
    public DurationFormatter toFormatter() {
        return toFormatter(Locale.getDefault());
    }

    /**
     * Constructs a {@link DurationFormatter} using all the appended elements and the given locale.
     * <p>
     * This is the main method used by applications at the end of the build process to create a usable formatter.
     *
     * @param locale the locale to use
     * @return the newly created formatter
     */
    public DurationFormatter toFormatter(Locale locale) {
        return new DurationFormatter(printers, locale);
    }

    // ==== PRINTERS ====

    interface DurationPrinter {
        boolean print(List<DurationPrinter> printers, Duration duration, Locale locale, StringBuilder builder);
    }

    interface ValuePrinter extends DurationPrinter {
        long getValue(Duration duration, Locale locale);
    }

    class StringLiteralPrinter implements DurationPrinter {
        private final String literal;

        StringLiteralPrinter(String literal) {
            this.literal = literal;
        }

        @Override
        public boolean print(List<DurationPrinter> printers, Duration duration, Locale locale, StringBuilder builder) {
            builder.append(literal);
            return true;
        }
    }

    class FieldValuePrinter implements DurationPrinter, ValuePrinter {
        private final ChronoUnit unit;

        FieldValuePrinter(ChronoUnit unit) {
            this.unit = unit;
        }

        @Override
        public long getValue(Duration duration, Locale locale) {
            return DurationUtils.getField(duration, unit);
        }

        @Override
        public boolean print(List<DurationPrinter> printers, Duration duration, Locale locale, StringBuilder builder) {
            long value = getValue(duration, locale);
            if (value > 0) {
                builder.append(value);
                return true;
            }
            return false;
        }
    }

    class ValueFormatPrinter implements DurationPrinter, ValuePrinter {
        private final ValuePrinter lastPrinter;
        private final boolean allowZero;
        private final String numberFormat;

        public ValueFormatPrinter(ValuePrinter lastPrinter, boolean allowZero, String numberFormat) {
            this.lastPrinter = lastPrinter;
            this.allowZero = allowZero;
            this.numberFormat = numberFormat;
        }

        @Override
        public long getValue(Duration duration, Locale locale) {
            return lastPrinter.getValue(duration, locale);
        }

        @Override
        public boolean print(List<DurationPrinter> printers, Duration duration, Locale locale, StringBuilder builder) {
            long value = getValue(duration, locale);
            if (allowZero || value > 0) {
                builder.append(String.format(numberFormat, value));
                return true;
            }
            return false;
        }
    }

    class SuffixPrinter implements DurationPrinter, ValuePrinter {
        private final ValuePrinter lastPrinter;
        private final String singular;
        private final String plural;

        SuffixPrinter(ValuePrinter lastPrinter, String singular, String plural) {
            this.lastPrinter = lastPrinter;
            this.singular = singular;
            this.plural = plural;
        }

        @Override
        public long getValue(Duration duration, Locale locale) {
            return lastPrinter.getValue(duration, locale);
        }

        @Override
        public boolean print(List<DurationPrinter> printers, Duration duration, Locale locale, StringBuilder builder) {
            if (lastPrinter.print(printers, duration, locale, builder)) {
                if (lastPrinter.getValue(duration, locale) > 1) {
                    builder.append(plural);
                } else {
                    builder.append(singular);
                }
                return true;
            }
            return false;
        }
    }

    class SeparatorPrinter implements DurationPrinter {
        private final String text;
        private final String finalText;
        private final String[] variants;

        SeparatorPrinter(String text, String finalText, String[] variants) {
            this.text = text;
            this.finalText = finalText;
            this.variants = variants;
        }

        @Override
        public boolean print(List<DurationPrinter> printers, Duration duration, Locale locale, StringBuilder builder) {
            int printerIndex = resolvePrinterIndex(printers);
            int previousValues = 0;
            int nextValues = 0;
            for (int i = 0; i < printers.size(); i++) {
                DurationPrinter durationPrinter = printers.get(i);
                if (durationPrinter instanceof ValuePrinter) {
                    if (((ValuePrinter) durationPrinter).getValue(duration, locale) > 0) {
                        if (i < printerIndex) {
                            previousValues++;
                        } else if (i > printerIndex) {
                            nextValues++;
                        }
                    }
                }
            }

            if (previousValues > 0) {
                if (nextValues > 1) {
                    builder.append(text);
                } else if (nextValues == 1) {
                    builder.append(finalText);
                }
            }
            return true;
        }

        private int resolvePrinterIndex(List<DurationPrinter> printers) {
            for (int i = 0; i < printers.size(); i++) {
                if (printers.get(i).equals(this)) {
                    return i;
                }
            }
            throw new IllegalStateException("Couldn't resolve printer index");
        }

    }

}
