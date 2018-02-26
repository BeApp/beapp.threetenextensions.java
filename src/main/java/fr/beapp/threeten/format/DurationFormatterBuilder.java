package fr.beapp.threeten.format;


import fr.beapp.threeten.DurationUtils;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DurationFormatterBuilder {

    private List<DurationPrinter> printers = new LinkedList<>();

    public DurationFormatterBuilder appendYears() {
        printers.add(new FieldValuePrinter(ChronoUnit.YEARS));
        return this;
    }

    public DurationFormatterBuilder appendMonths() {
        printers.add(new FieldValuePrinter(ChronoUnit.MONTHS));
        return this;
    }

    public DurationFormatterBuilder appendWeeks() {
        printers.add(new FieldValuePrinter(ChronoUnit.WEEKS));
        return this;
    }

    public DurationFormatterBuilder appendDays() {
        printers.add(new FieldValuePrinter(ChronoUnit.DAYS));
        return this;
    }

    public DurationFormatterBuilder appendHours() {
        printers.add(new FieldValuePrinter(ChronoUnit.HOURS));
        return this;
    }

    public DurationFormatterBuilder appendMinutes() {
        printers.add(new FieldValuePrinter(ChronoUnit.MINUTES));
        return this;
    }

    public DurationFormatterBuilder appendSeconds() {
        printers.add(new FieldValuePrinter(ChronoUnit.SECONDS));
        return this;
    }

    public DurationFormatterBuilder appendMillis() {
        printers.add(new FieldValuePrinter(ChronoUnit.MILLIS));
        return this;
    }

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

    public DurationFormatterBuilder appendSuffix(String suffix) {
        return appendSuffix(suffix, suffix);
    }

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

    public DurationFormatterBuilder appendSeparator(String text, String finalText, String[] variants) {
        printers.add(new SeparatorPrinter(text, finalText, variants));
        return this;
    }

    public DurationFormatterBuilder appendLiteral(String literal) {
        printers.add(new StringLiteralPrinter(literal));
        return this;
    }

    public DurationFormatter toFormatter() {
        return toFormatter(Locale.getDefault());
    }

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

    interface ValueAggregator {
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
