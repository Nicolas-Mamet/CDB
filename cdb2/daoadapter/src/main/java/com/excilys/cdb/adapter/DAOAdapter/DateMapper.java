package com.excilys.cdb.adapter.DAOAdapter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class DateMapper {
    public static Optional<LocalDateTime> toLocalDateTime(Timestamp t) {
        if (t == null) {
            return Optional.empty();
        } else {
            return Optional.of(t.toLocalDateTime());
        }
    }

    public static Optional<Timestamp> valueOf(LocalDateTime ldt) {
        if (ldt == null) {
            return Optional.empty();
        } else {
            return Optional.of(Timestamp.valueOf(ldt));
        }
    }
}
