package com.excilys.cdb.exceptions;

import java.util.List;

public class CorruptComputersException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final List<List<Problem>> corruptComputers;

    public CorruptComputersException(List<List<Problem>> corruptComputers) {
        this.corruptComputers = corruptComputers;
    }

    public List<List<Problem>> getCorruptComputers() {
        return corruptComputers;
    }

}
