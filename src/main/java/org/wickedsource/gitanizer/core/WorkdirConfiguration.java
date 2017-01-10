package org.wickedsource.gitanizer.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WorkdirConfiguration {

    private Path applicationWorkdir;

    private MirrorRepository mirrorRepository;

    @Autowired
    public WorkdirConfiguration(Environment environment, MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
        initWorkdirPathFromEnvironment(environment);
    }

    private void initWorkdirPathFromEnvironment(Environment environment) {
        String workdir = environment.getProperty("gitanizer.workdir");
        if (workdir == null) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.workdir' not set!");
        }
        try {
            applicationWorkdir = Paths.get(workdir);
            if (!applicationWorkdir.toFile().exists()) {
                applicationWorkdir.toFile().mkdirs();
            }
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.workdir' contains an invalid path!", e);
        }
    }

    /**
     * Returns the path to the working directory for the specified mirror.
     *
     * @param mirrorId ID of the mirror whose working directory to return
     */
    public Path getWorkdir(long mirrorId) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        String sanitizedFilename = sanitizeFilename(mirror.getWorkdirName());
        if (sanitizedFilename.equals("")) {
            throw new IllegalArgumentException("The given filename must contain at least one character that is allowed in file names!");
        }
        Path subWorkdir = applicationWorkdir.resolve(sanitizedFilename);
        subWorkdir.toFile().mkdirs();
        return subWorkdir;
    }

    /**
     * Returns the main application working directory in which all mirror working directories are contained.
     */
    public Path getApplicationWorkdir() {
        return applicationWorkdir;
    }

    /**
     * Returns the git directory within the mirror's working directory.
     *
     * @param mirrorId ID of the mirror whose git directory to return.
     */
    public Path getGitDir(long mirrorId) {
        return getWorkdir(mirrorId).resolve("git");
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("\\W+", "");
    }

}
