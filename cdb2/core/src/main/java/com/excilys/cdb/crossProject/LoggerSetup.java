package com.excilys.cdb.crossProject;

public class LoggerSetup {
    public static void setDefaultLevelToTrace() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    }
}