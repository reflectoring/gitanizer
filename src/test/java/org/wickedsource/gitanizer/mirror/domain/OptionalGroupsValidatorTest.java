package org.wickedsource.gitanizer.mirror.domain;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalGroupsValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validationSucceedsWhenAllFieldsAreNull() {
        OptionalGroupTestObject object = new OptionalGroupTestObject();
        assertThat(validator.validate(object)).isEmpty();
    }

    @Test
    public void validationSucceedsWhenGroupFieldsAreNotNull() {
        OptionalGroupTestObject object = new OptionalGroupTestObject();
        object.setField1("field1");
        object.setField2("field2");
        assertThat(validator.validate(object)).isEmpty();
    }

    @Test
    public void validationSucceedsWhenMoreThanGroupFieldsAreNotNull() {
        OptionalGroupTestObject object = new OptionalGroupTestObject();
        object.setField3("field3");
        assertThat(validator.validate(object)).isEmpty();
    }

    @Test
    public void validationFailsWhenOnlyOneGroupFieldIsNotNull() {
        OptionalGroupTestObject object = new OptionalGroupTestObject();
        object.setField1("field1");
        // one for the constraint, one for each field
        assertThat(validator.validate(object)).hasSize(3);
    }


}