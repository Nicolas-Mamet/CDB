package ui;

import java.io.BufferedReader;

public abstract class AbstractCanRead {
	private static BufferedReader reader;
	
	protected static final BufferedReader getReader() {
		return reader;
	}
	
	public static final void setReader(BufferedReader pReader) {
		reader = pReader;
	}
}
