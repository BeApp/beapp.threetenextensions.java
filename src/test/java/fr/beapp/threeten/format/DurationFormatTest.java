package fr.beapp.threeten.format;

import org.junit.Assert;
import org.junit.Test;
import org.threeten.bp.Duration;

import java.util.Locale;

public class DurationFormatTest {

    private DurationFormatter formatterEN = DurationFormat.wordBased(Locale.ENGLISH);
    private DurationFormatter formatterFR = DurationFormat.wordBased(Locale.FRENCH);

    @Test
    public void shortBased() {
        DurationFormatter formatter = DurationFormat.shortBased();

        Assert.assertEquals("00:00:00", formatter.format(Duration.ofSeconds(0)));
        Assert.assertEquals("00:00:10", formatter.format(Duration.ofSeconds(10)));
        Assert.assertEquals("00:03:10", formatter.format(Duration.ofMinutes(3).plusSeconds(10)));
        Assert.assertEquals("01:03:10", formatter.format(Duration.ofHours(1).plusMinutes(3).plusSeconds(10)));
        Assert.assertEquals("01:00:00", formatter.format(Duration.ofHours(1)));

        Assert.assertEquals("00:00:00", formatter.format(Duration.ofDays(1)));
    }

    @Test
    public void wordBased_simple() {
        assetWithLocales("1 second", "1 seconde", Duration.ofSeconds(1));
        assetWithLocales("15 seconds", "15 secondes", Duration.ofSeconds(15));

        assetWithLocales("1 minute", "1 minute", Duration.ofMinutes(1));
        assetWithLocales("15 minutes", "15 minutes", Duration.ofMinutes(15));

        assetWithLocales("1 hour", "1 heure", Duration.ofHours(1));
        assetWithLocales("15 hours", "15 heures", Duration.ofHours(15));

        assetWithLocales("1 day", "1 jour", Duration.ofDays(1));
        assetWithLocales("15 days", "15 jours", Duration.ofDays(15));

        assetWithLocales("1 year", "1 année", Duration.ofDays(365));
        assetWithLocales("2 years", "2 années", Duration.ofDays(365 * 2));
    }

    private void assetWithLocales(String expectedEN, String expectedFR, Duration duration) {
        Assert.assertEquals(expectedEN, formatterEN.format(duration));
        Assert.assertEquals(expectedFR, formatterFR.format(duration));
    }

    @Test
    public void wordBased_composite() {
        Duration duration = Duration.ofDays(1).plusHours(4).plusMinutes(3);

        Assert.assertEquals("1 day, 4 hours and 3 minutes", formatterEN.format(duration));
        Assert.assertEquals("1 jour, 4 heures et 3 minutes", formatterFR.format(duration));
    }

}