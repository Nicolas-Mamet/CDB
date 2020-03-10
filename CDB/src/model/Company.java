package model;

public final class Company {

	private long id;
	private String name;
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	private Company(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Company [(id : "+id+")(name : "+name+")]";
	}
	
	/**
	 * 
	 * @param id company ID
	 * @param name company name
	 * @return return null if id == 0 (no company); else return a new Company
	 */
	public static Company createCompany(long id, String name) {
		if(id < 0) {
			throw new IllegalArgumentException(
					"All company IDs must be positive");
		}
		if(id != 0) {
			return new Company(id, name);
		} else {
			return null;
		}
	}
}
