package com.excilys.cdb.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerManager {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger("bob");
        logger.info("salut c'est bob");
    }
}
