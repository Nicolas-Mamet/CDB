package com.excilys.cdb.crossProject.exceptions;

import java.util.Optional;

public class Problem {

    @Override
    public String toString() {
        return "Problem [origin=" + origin + ", nature=" + nature + "]";
    }

    String origin;
    Nature nature;

    public enum Nature {
        NOTADATE, NOTALONG, WRONGORDER, NONAME, NOCOMPUTER, NOCOMPANY,
        BEFORE1970, WRONGOFFSET, WRONGLIMIT
    }

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

    public static Problem createBefore1970(String date) {
        return new Problem(Nature.BEFORE1970, date);
    }

    public static Problem createWrongOffset(long offset) {
        return new Problem(Nature.WRONGOFFSET, offset + "");
    }

    public static Problem createWrongLimit(long limit) {
        return new Problem(Nature.WRONGLIMIT, limit + "");
    }
}
