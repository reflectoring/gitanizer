package org.wickedsource.gitanizer.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WorkdirConfiguration {

    private Path applicationWorkdir;

    @Autowired
    public WorkdirConfiguration(Environment environment) {
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
     * Returns a directory below the application workdir which is defined by the environment variable 'gitanizer.workdir'.
     * The name passed into this method is sanitized. If a sub directory with the sanitized name already exists,
     * the existing folder is returned. If it doesn't exist, a new folder is created and then returned.
     *
     * @param workdirName the name of the sub directory. Characters that are not allowed in file names are removed from it.
     */
    public Path getWorkdir(String workdirName) {
        String sanitizedFilename = sanitizeFilename(workdirName);
        if (sanitizedFilename.equals("")) {
            throw new IllegalArgumentException("The given filename must contain at least one character that is allowed in file names!");
        }
        Path subWorkdir = applicationWorkdir.resolve(sanitizedFilename);
        subWorkdir.toFile().mkdirs();
        return subWorkdir;
    }

    public Path getApplicationWorkdir() {
        return applicationWorkdir;
    }

    public Path getGitDir(String workdirName) {
        return getWorkdir(workdirName).resolve("git");
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("\\W+", "");
    }

}
