package model;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public final class Computer {
	
	private long id;
	private String name;
	private LocalDateTime introduced;
	private LocalDateTime discontinued;
	private Company company;
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public LocalDateTime getIntroduced() {
		return introduced;
	}
	public LocalDateTime getDiscontinued() {
		return discontinued;
	}
	public Company getCompany() {
		return company;
	}
	
	/**
	 * A computer should not have an ID of 0 unless it has yet to be created
	 * in the database; this function does not check whether the ID actually
	 * exists in the database
	 * @return true if id != 0; return false if id == 0
	 */
	public boolean hasValidID () {
		return id != 0;
	}
	
	private Computer(String name) {
		this.name = name;
	}

	public static class ComputerBuilder {
		private long id;
		private String name;
		private LocalDateTime introduced;
		private LocalDateTime discontinued;
		private Company company;
		
		public ComputerBuilder(String name) {
			this.name = name;
 		}
		
		public ComputerBuilder withId(long id) {
			this.id = id;
			return this;
		}
		
		public ComputerBuilder withIntroduced(LocalDateTime introduced) {
			this.introduced = introduced;
			return this;
		}
		
		public ComputerBuilder withDiscontinued(LocalDateTime discontinued) {
			this.discontinued = discontinued;
			return this;
		}
		
		public ComputerBuilder withCompany(Company company) {
			this.company = company;
			return this;
		}
		
		public Computer build() {
			Computer computer = new Computer(name);
			computer.company = this.company;
			computer.discontinued = this.discontinued;
			computer.introduced = this.introduced;
			computer.id = this.id;
			return computer;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder bob = new StringBuilder().append("Computer[");
		for(Field f : Computer.class.getDeclaredFields()) {
			try {
				//check access before modifying the builder 
				Object value = f.get(this);
				bob.append("(");
				bob.append(f.getName()).append(" : ").append(value);
				bob.append(")");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("absurd?");
			} catch (IllegalAccessException e) {}
		}
		bob.append("]");
		return bob.toString();
	}
	
}
