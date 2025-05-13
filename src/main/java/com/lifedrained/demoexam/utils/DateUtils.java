package com.lifedrained.demoexam.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

//Утилитарный класс для парсинга и форматирования дат(НЕ ИСПОЛЬЗУЕТСЯ)
public class DateUtils {
    public static final String DB_DT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd";

    private static final String LD_FORMAT = "dd.MM.yyyy";
    private static final String LDT_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String LT_FORMAT = "HH:mm";

    private DateUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String getStringFromDateTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(LDT_FORMAT));
    }

    public static String getStringFromTime(LocalTime time) {
        return new SimpleDateFormat(LT_FORMAT).format(asDate(time));
    }

    public static String getStringFromDate(Date date) {
        return new SimpleDateFormat(LD_FORMAT).format(date);
    }

    public static String getStringTimeFromDate(Date date) {
        return new SimpleDateFormat(LT_FORMAT).format(date);
    }

    public static String getStringDateTimeFromDate(Date date) {
        return new SimpleDateFormat(LDT_FORMAT).format(date);
    }

    public static String getStringFromDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(LD_FORMAT));
    }

    public static LocalDate getLDFromString(String stringDate) throws DateTimeParseException {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(LD_FORMAT));
    }

    public static Date getDateTimeFromString(String stringDate) throws ParseException {

        return new SimpleDateFormat(LDT_FORMAT).parse(stringDate);

    }

    public static LocalTime getTimeFromString(String stringTime) throws DateTimeParseException {
        return LocalTime.parse(stringTime, DateTimeFormatter.ofPattern(LT_FORMAT));
    }

    public static Date getDateFromString(String stringDate) throws ParseException {
        return new SimpleDateFormat(LD_FORMAT).parse(stringDate);
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime getLDTFromString(String stringDate) throws DateTimeParseException {
        System.out.println("parsing date: " + stringDate);
        System.err.println("PARSING DATE: " + stringDate);
        return LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern(LDT_FORMAT));
    }
    public static String parseDateToDBString(Date date) {
       return new SimpleDateFormat(DB_DT_FORMAT).format(date);
    }

    public static Date asDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalTime localTime) {
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String parseLocalTemporal(Object localTemporal) {
        if (localTemporal instanceof LocalDateTime) {
            return getStringFromDateTime((LocalDateTime) localTemporal);
        }
        if (localTemporal instanceof Date){
            return getStringDateTimeFromDate((Date) localTemporal);
        }
        if (localTemporal instanceof LocalDate) {
            return getStringFromDate((LocalDate) localTemporal);
        }
        if (localTemporal instanceof LocalTime) {
            return getStringFromTime((LocalTime) localTemporal);
        }
        return "";
    }
}
