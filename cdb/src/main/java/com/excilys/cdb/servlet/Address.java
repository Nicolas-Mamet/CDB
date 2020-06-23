package com.excilys.cdb.servlet;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Address {
    BEFOREDASHBOARD("dashboard", "beforedashboard"),
    DASHBOARD("WEB-INF/dashboard.jsp", "dashboard"),
    BEFOREADD("addcomputer", "beforeadd"),
    VIEWADD("WEB-INF/addComputer.jsp", "viewadd"), ADD("addcomputer2", "add"),
    BEFOREEDIT("editcomputer", "beforeedit"),
    VIEWEDIT("WEB-INF/editComputer.jsp", "viewedit"),
    EDIT("editcomputer2", "edit"), PROBLEM("problemlist", "problem"),
    INVALIDPAGE("invalidpage", "invalidpage"), DB("dbissue", "db"),
    ABSURD("absurd", "absurd"), DELETE("deletecomputers", "delete");

    private final String address;
    private final String value;

    Address(String add, String val) {
        address = add;
        value = val;
    }

    public String getAddress() {
        return address;
    }

    public static Map<String, String> getMap() {
        return Arrays.asList(Address.values()).stream().collect(
                Collectors.toMap(Address::getValue, Address::getAddress));
    }

    public String getValue() {
        return value;
    }

}
