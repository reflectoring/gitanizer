package org.wickedsource.gitanizer.mirror.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class OptionalGroupsValidator implements ConstraintValidator<OptionalGroups, Object> {

    private OptionalGroups optionalGroups;

    @Override
    public void initialize(OptionalGroups annotation) {
        this.optionalGroups = annotation;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            boolean allValid = true;
            for (OptionalGroup optionalGroup : optionalGroups.value()) {
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
                allValid = allValid && isValid;
            }
            return allValid;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
