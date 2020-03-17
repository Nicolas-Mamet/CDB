package model;

public final class Company {

	private long iD;
	private String name;
	
	public long getId() {
		return iD;
	}
	public String getName() {
		return name;
	}
	
	private Company(long iD, String name) {
		super();
		this.iD = iD;
		this.name = name;
	}
	
	public static CompanyBuilder builder() {
		return new CompanyBuilder();
	}
	
	public static class CompanyBuilder {
		private long iD;
		private String name;
		
		private CompanyBuilder() {}
		
		public CompanyBuilder withID(long iD) {
			this.iD = iD;
			return this;
		}
		
		public CompanyBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Company build() {
			return new Company(this.iD, this.name);
		}
	}
	
	@Override
	public String toString() {
		return "Company [(id : "+iD+")(name : "+name+")]";
	}
}
