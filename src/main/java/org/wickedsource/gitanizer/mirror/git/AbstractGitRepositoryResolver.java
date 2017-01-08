package org.wickedsource.gitanizer.mirror.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;
import org.wickedsource.gitanizer.core.ForbiddenException;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Resolves files within a hosted git repository.
 */
public abstract class AbstractGitRepositoryResolver extends AbstractResourceResolver {

    private WorkdirConfiguration workdirConfiguration;

    private MirrorRepository mirrorRepository;

    @Autowired
    public AbstractGitRepositoryResolver(WorkdirConfiguration workdirConfiguration, MirrorRepository mirrorRepository) {
        this.workdirConfiguration = workdirConfiguration;
        this.mirrorRepository = mirrorRepository;
    }

    @Override
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        String gitRepositoryName = extractGitRepositoryNameFromRequestPath(requestPath);
        Mirror mirror = mirrorRepository.findByGitRepositoryName(gitRepositoryName);
        if (mirror == null) {
            return null;
        }

        if (!isAuthorizedForMirror(mirror)) {
            throw new ForbiddenException();
        }

        Path gitDir = workdirConfiguration.getGitDir(mirror.getWorkdirName());
        String filepath = requestPath.replace(gitRepositoryName + "/", "");
        Path fileToServe = gitDir.resolve(filepath);

        if (!Files.exists(fileToServe) || Files.isDirectory(fileToServe)) {
            return null;
        }

        return new FileSystemResource(fileToServe.toFile());
    }

    /**
     * Can be overridden by sub classes to add authorization.
     *
     * @param mirror the mirror whose authorization to check.
     * @return true if the current user is authorized, false if not.
     */
    protected abstract boolean isAuthorizedForMirror(Mirror mirror);

    private String extractGitRepositoryNameFromRequestPath(String requestPath) {
        if (requestPath.contains("/")) {
            return requestPath.substring(0, requestPath.indexOf('/')).toLowerCase();
        } else {
            return requestPath.toLowerCase();
        }
    }

    @Override
    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return null;
    }

}
