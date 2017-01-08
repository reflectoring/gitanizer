package org.wickedsource.gitanizer.mirror.domain;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This validator annotation may be added at class level to mark a group of fields
 * as belonging together. All fields in that group must together either be null or have a value.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalGroup {

    /**
     * Names of the fields that together form the group. Either all fields must be null
     * or no field must be null for validation to succeed.
     */
    String[] fieldNames();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
