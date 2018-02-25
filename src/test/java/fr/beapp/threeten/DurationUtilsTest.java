package fr.beapp.threeten;

import org.junit.Assert;
import org.junit.Test;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

public class DurationUtilsTest {

    @Test
    public void getField_nanos() {
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofNanos(15), ChronoUnit.NANOS));
        Assert.assertEquals(5L, DurationUtils.getField(Duration.ofNanos(10005), ChronoUnit.NANOS));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofNanos(1000), ChronoUnit.NANOS));
    }

    @Test
    public void getField_micros() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofNanos(1000), ChronoUnit.MICROS));
        Assert.assertEquals(5L, DurationUtils.getField(Duration.ofNanos(5000), ChronoUnit.MICROS));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofNanos(15000), ChronoUnit.MICROS));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofNanos(1000000), ChronoUnit.MICROS));
    }

    @Test
    public void getField_millis() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofNanos(1000000), ChronoUnit.MILLIS));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofMillis(15), ChronoUnit.MILLIS));
        Assert.assertEquals(5L, DurationUtils.getField(Duration.ofMillis(10005), ChronoUnit.MILLIS));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofMillis(1000), ChronoUnit.MILLIS));
    }

    @Test
    public void getField_seconds() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofMillis(1000), ChronoUnit.SECONDS));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofSeconds(15), ChronoUnit.SECONDS));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofSeconds(75), ChronoUnit.SECONDS));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofSeconds(60), ChronoUnit.SECONDS));
    }

    @Test
    public void getField_minutes() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofSeconds(60), ChronoUnit.MINUTES));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofMinutes(15), ChronoUnit.MINUTES));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofMinutes(75), ChronoUnit.MINUTES));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofMinutes(60), ChronoUnit.MINUTES));
    }

    @Test
    public void getField_hours() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofMinutes(60), ChronoUnit.HOURS));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofHours(15), ChronoUnit.HOURS));
        Assert.assertEquals(11L, DurationUtils.getField(Duration.ofHours(35), ChronoUnit.HOURS));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofHours(24), ChronoUnit.HOURS));
    }

    @Test
    public void getField_days() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofHours(24), ChronoUnit.DAYS));
        Assert.assertEquals(15L, DurationUtils.getField(Duration.ofDays(15), ChronoUnit.DAYS));
        Assert.assertEquals(0L, DurationUtils.getField(Duration.ofDays(365), ChronoUnit.DAYS));
    }

    @Test
    public void getField_years() {
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofDays(365), ChronoUnit.YEARS));
        Assert.assertEquals(1L, DurationUtils.getField(Duration.ofDays(400), ChronoUnit.YEARS));
    }

}