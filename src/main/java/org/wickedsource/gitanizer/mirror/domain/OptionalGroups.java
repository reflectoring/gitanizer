package org.wickedsource.gitanizer.mirror.domain;

import javax.validation.Constraint;
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
@Constraint(validatedBy = OptionalGroupsValidator.class)
public @interface OptionalGroups {

    OptionalGroup[] value();

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
