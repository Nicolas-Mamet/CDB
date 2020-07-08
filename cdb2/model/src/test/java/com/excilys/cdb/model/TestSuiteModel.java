package com.excilys.cdb.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.excilys.cdb.model.validator.CompanyValidatorTest;
import com.excilys.cdb.model.validator.ComputerValidatorTest;
import com.excilys.cdb.model.validator.PageValidatorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ PageValidatorTest.class, CompanyValidatorTest.class,
        ComputerValidatorTest.class })
public class TestSuiteModel {

}
