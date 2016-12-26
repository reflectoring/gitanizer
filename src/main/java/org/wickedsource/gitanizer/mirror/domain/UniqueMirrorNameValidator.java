package org.wickedsource.gitanizer.mirror.domain;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a specified repository name is not already in use.
 */
public class UniqueMirrorNameValidator implements ConstraintValidator<UniqueMirrorName, String> {

    private MirrorRepository mirrorRepository;

    @Autowired
    public UniqueMirrorNameValidator(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
    }

    @Override
    public void initialize(UniqueMirrorName constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return mirrorRepository.countByDisplayName(value) == 0;
    }
}
