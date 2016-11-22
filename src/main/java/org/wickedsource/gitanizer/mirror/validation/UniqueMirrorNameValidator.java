package org.wickedsource.gitanizer.mirror.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.gitanizer.mirror.domain.MirrorNameSanitizer;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a specified repository name is not already in use.
 */
public class UniqueMirrorNameValidator implements ConstraintValidator<UniqueMirrorName, String> {

    private MirrorRepository mirrorRepository;

    private MirrorNameSanitizer mirrorNameSanitizer;

    @Autowired
    public UniqueMirrorNameValidator(MirrorRepository mirrorRepository, MirrorNameSanitizer mirrorNameSanitizer) {
        this.mirrorRepository = mirrorRepository;
        this.mirrorNameSanitizer = mirrorNameSanitizer;
    }

    @Override
    public void initialize(UniqueMirrorName constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String sanitizedName = mirrorNameSanitizer.sanitizeName(value);
        return mirrorRepository.countByName(sanitizedName) == 0;
    }
}