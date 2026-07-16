package com.betacom.jpa.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.betacom.jpa.exceptions.ApiException;


// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Helper di conversione date, usato ovunque nel backend serva formattare o interpretare una data
public class Utilities {
	private final static String PATTERN_DATE = "d/M/yyyy HH:mm:ss:SSSS";
	private final static String PATTERN_DATE_1 = "dd/MM/yyyy";
	/*
	 * transform date to format string
	 */

	public static String dateToString(LocalDateTime myDate) {
		return dateToString(PATTERN_DATE,myDate);
	}
	public static String dateToString(String pattern, LocalDateTime myDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ITALIAN);
		return myDate.format(formatter);
	}

	public static String dateToString(String pattern, LocalDate myDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ITALIAN);
		return myDate.format(formatter);
	}

	// Interpreta una stringa "dd/MM/yyyy": se il formato non corrisponde, lancia ApiException
	// invece di lasciar propagare la DateTimeParseException grezza
	public static LocalDate stringToDate(String myDate) throws ApiException{
		LocalDate r = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE_1, Locale.ITALIAN);
			r=  LocalDate.parse(myDate, formatter);

		} catch (DateTimeParseException e) {
			throw new ApiException("Formato della data invalido:" + myDate + " formato previsto:" + PATTERN_DATE_1);
		}
		return r;
	}

	// Converte un java.sql.Date grezzo (tipico di risultati JDBC nativi) in LocalDate
	public static LocalDate dateToLocalDate(Object value) {
		if ((value) == null) return null;
		return ((Date)value).toLocalDate();
	}


}
