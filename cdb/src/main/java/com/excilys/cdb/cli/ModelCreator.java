/*
 * package com.excilys.cdb.cli;
 *
 * import java.io.IOException; import java.time.LocalDateTime; import
 * java.util.Optional;
 *
 * import com.excilys.cdb.exceptions.UserInterruptException; import
 * com.excilys.cdb.model.Company; import com.excilys.cdb.model.Computer; import
 * com.excilys.cdb.model.Computer.ComputerBuilder;
 *
 * @Deprecated class ModelCreator { private static final ModelCreator INSTANCE =
 * new ModelCreator();
 *
 * public ModelCreator getInstance() { return INSTANCE; }
 *
 * private String askString(String msg) throws IOException {
 * System.out.println(msg); return ReadFromCL.getInstance().readString(); }
 *
 * private Long askLong(String msg) throws IOException, UserInterruptException {
 * System.out.println(msg); Optional<Long> oLong =
 * ReadFromCL.getInstance().readLong(); if(oLong.isEmpty()) { askReset(); return
 * askLong(msg); } else { return oLong.get(); }
 *
 * }
 *
 * private static final String resetNullMsg =
 * "wrong input. Go back to menu (M)?" + " Use a null value (N)?" +
 * " Try again (anything else)?";
 *
 * private static final String resetMsg = "wrong input. Go back to menu (M)? " +
 * "Try again (anything else)?";
 *
 * private void askReset() throws IOException, UserInterruptException {
 * System.out.println(resetMsg); String answer =
 * ReadFromCL.getInstance().readString(); if(answer.equalsIgnoreCase("m")) {
 * throw new UserInterruptException(); } }
 *
 * private boolean askResetNull() throws IOException, UserInterruptException {
 * System.out.println(resetNullMsg); String answer =
 * ReadFromCL.getInstance().readString(); if(answer.equalsIgnoreCase("m")) {
 * throw new UserInterruptException(); } else if(answer.equalsIgnoreCase("N")) {
 * return false; } else { return true; } }
 *
 * private LocalDateTime askDate(String msg) throws IOException,
 * UserInterruptException { System.out.println(msg); Optional<LocalDateTime>
 * oDate = ReadFromCL.getInstance().readLocalDateTime(); if(oDate.isEmpty()) {
 * if(askResetNull()) { return askDate(msg); } else { return null; } } else {
 * return oDate.get(); } }
 *
 * public Computer computerFromInput(Company company) throws IOException,
 * UserInterruptException { return new
 * ComputerBuilder(askString("Computer name : "))
 * .withIntroduced(askDate("Introduced : "))
 * .withDiscontinued(askDate("Discontinued : ")) .withCompany(company).build();
 * }
 *
 * public Company companyFromInput() throws IOException, UserInterruptException
 * { return Company.createCompany(askLong("Company ID : "),
 * askString("Company name : ")); } }
 */
