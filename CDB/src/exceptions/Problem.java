package exceptions;

import java.util.Optional;

public class Problem {
	
	String origin;
	Nature nature;
	
	public enum Nature{ NOTADATE, NOTALONG, WRONGORDER,
		NONAME, NOCOMPUTER, NOCOMPANY}
	
	private Problem(Nature nature, String origin) {
		this.nature = nature;
		this.origin = origin;
	}

	private Problem(Nature nature) {
		super();
		this.nature = nature;
	}



	public Optional<String> getOrigin() {
		return Optional.ofNullable(origin);
	}

	public Nature getNature() {
		return nature;
	}
	
	public static Problem createNotADate(String origin) {
		return new Problem(Nature.NOTADATE, origin);
	}
	
	public static Problem createNotALong(String origin) {
		return new Problem(Nature.NOTALONG, origin);
	}
	
	public static Problem createWrongOrder() {
		return new Problem(Nature.WRONGORDER);
	}
	
	public static Problem createNoName() {
		return new Problem(Nature.NONAME);
	}

	public static Problem createNoComputer(String iD) {
		return new Problem(Nature.NOCOMPUTER, iD);
	}

	public static Problem createNoCompany(String origin) {
		return new Problem(Nature.NOCOMPANY, origin);
	}
}
