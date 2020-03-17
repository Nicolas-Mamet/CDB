package mapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import exceptions.NotDateException;
import exceptions.NotLongException;
import ui.AbstractCanRead;

public class Mapper {

	private Mapper() {}
	
	/**
	 * Parse the parameter into a long; raise an exception if the parameter
	 * is null or does not represent a long
	 * @param string
	 * @return
	 * @throws NotLongException
	 */
	public static Long mapLong(String string)
			throws NotLongException {
		long iD;
		try {
			iD = Long.parseLong(string);
		} catch (NumberFormatException e) {
			throw new NotLongException();
		}
		return iD;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 * @throws NotDateException
	 */
	public static Optional<LocalDateTime> mapLocalDateTime(String string)
			throws NotDateException {
		if(string == null || string.equals("")) {
			return Optional.empty();
		}
		LocalDateTime date;
		try {
			date = LocalDate.parse(string).atStartOfDay();
		} catch (DateTimeParseException e) {
			throw new NotDateException(string);
		}
		return Optional.of(date);
	}
	
	public static Optional<LocalDateTime> toLocalDateTime(Timestamp t) {
		 if(t == null) {
			 return Optional.empty();
		 } else {
			 return Optional.of(t.toLocalDateTime());
		 }
	}
	
	public static Optional<Timestamp> valueOf(LocalDateTime ldt) {
		if(ldt == null) {
			 return Optional.empty();
		 } else {
			 return Optional.of(Timestamp.valueOf(ldt));
		 }
	}

}
