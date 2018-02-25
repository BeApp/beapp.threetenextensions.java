package fr.beapp.threeten;

import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

public final class DurationUtils {

	private DurationUtils() {
	}

	public static long getField(Duration duration, ChronoUnit chronoUnit) {
		switch (chronoUnit) {
			case NANOS:
				return duration.toNanos() % 1000;
			case MICROS:
				return (duration.toNanos() / 1000) % 1000;
			case MILLIS:
				return duration.toMillis() % 1000;
			case SECONDS:
				return duration.getSeconds() % 60;
			case MINUTES:
				return duration.toMinutes() % 60;
			case HOURS:
				return duration.toHours() % 24;
			case DAYS:
				return duration.toDays() % 365;
			case YEARS:
				return (duration.toDays() / 365);
			case HALF_DAYS:
			case WEEKS:
			case MONTHS:
			case DECADES:
			case CENTURIES:
			case MILLENNIA:
			case ERAS:
		}
		return 0L;
	}

}
