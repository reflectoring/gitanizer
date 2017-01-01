package org.wickedsource.gitanizer.mirror.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class OptionalGroupValidator implements ConstraintValidator<OptionalGroup, Object> {

    private OptionalGroup optionalGroup;

    @Override
    public void initialize(OptionalGroup annotation) {
        this.optionalGroup = annotation;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            int nonNullFields = 0;
            int nullFields = 0;
            for (String fieldName : optionalGroup.fieldNames()) {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (field.get(object) == null || field.get(object).equals("")) {
                    nullFields++;
                } else {
                    nonNullFields++;
                }
            }
            boolean isValid = nonNullFields == 0 | nullFields == 0;
            if (!isValid) {
                for (String fieldName : optionalGroup.fieldNames()) {
                    context.buildConstraintViolationWithTemplate(optionalGroup.message())
                            .addPropertyNode(fieldName)
                            .addConstraintViolation();
                }
            }
            return isValid;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
