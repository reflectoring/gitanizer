package org.wickedsource.gitanizer.mirror.domain;

import org.springframework.stereotype.Component;

@Component
public class MirrorNameSanitizer {

    public String sanitizeName(String mirrorName) {
        if (!mirrorName.endsWith(".git")) {
            return mirrorName + ".git";
        }
        return mirrorName;
    }

}
