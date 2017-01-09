package org.wickedsource.gitanizer.mirror.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.core.security.GitanizerSecurityConfiguration;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

/**
 * Exposes restricted hosted Git repositories via "/git/secure/**".
 * A Git repository is considered restricted when the mirror has a username and password defined.
 */
@Component
public class SecureGitRepositoryResolver extends AbstractGitRepositoryResolver {

    @Autowired
    public SecureGitRepositoryResolver(WorkdirConfiguration workdirConfiguration, MirrorRepository mirrorRepository) {
        super(workdirConfiguration, mirrorRepository);
    }

    protected boolean isAuthorizedForMirror(Mirror mirror) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return false;
        }
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return false;
        }

        String requiredRole = GitanizerSecurityConfiguration.getAuthorityNameForMirror(mirror.getId());
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (requiredRole.equals(auth.getAuthority())) {
                return true;
            }
        }

        return false;
    }

}
