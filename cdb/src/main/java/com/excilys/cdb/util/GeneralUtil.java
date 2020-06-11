package com.excilys.cdb.util;

import java.util.Optional;

public class GeneralUtil {
    public static Optional<String> toString(Object object) {
        if (object == null) {
            return Optional.empty();
        } else {
            return Optional.of(object.toString());
        }
    }
}
