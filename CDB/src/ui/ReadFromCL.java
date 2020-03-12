package ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class ReadFromCL extends AbstractCanRead{
	private static final ReadFromCL INSTANCE = new ReadFromCL();
	
	private ReadFromCL() {}
	
	public static ReadFromCL getInstance() { return INSTANCE; }
	
	public Optional<Long> readLong() throws IOException {
		Optional<Long> oID = Optional.empty();
		try {
			String line = getReader().readLine();
			oID = Optional.of(Long.parseLong(line));
		} catch (NumberFormatException e) {}
		return oID;
	}
	
	public Optional<LocalDateTime> readLocalDateTime() throws IOException{
		Optional<LocalDateTime> oDate = Optional.empty();
		try {
			String line = getReader().readLine();
			oDate = Optional.of(LocalDate.parse(line).atStartOfDay());
		} catch (DateTimeParseException e) {}
		return oDate;
	}
	
	public String readString() throws IOException {
		return getReader().readLine();
	}
}
