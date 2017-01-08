package org.wickedsource.gitanizer.mirror.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

/**
 * Exposes public hosted Git repositories via "/git/public/**".
 * A Git repository is considered public when no username and password are specified for the mirror.
 */
@Component
public class PublicGitRepositoryResolver extends AbstractGitRepositoryResolver {

    @Autowired
    public PublicGitRepositoryResolver(WorkdirConfiguration workdirConfiguration, MirrorRepository mirrorRepository) {
        super(workdirConfiguration, mirrorRepository);
    }

    protected boolean isAuthorizedForMirror(Mirror mirror) {
        return !mirror.isGitRepositoryRestricted();
    }
}
