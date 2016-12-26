package org.wickedsource.gitanizer.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SubgitConfiguration {

    private Path subgitExecutable;

    private Path gitExecutable;

    @Autowired
    public SubgitConfiguration(Environment environment) {
        initSubgitPathFromEnvironment(environment);
        initGitPathFromEnvironment(environment);
    }

    private void initSubgitPathFromEnvironment(Environment environment) {
        String workdir = environment.getProperty("gitanizer.subgit.executable.path");
        if (workdir == null) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.subgit.executable.path' not set!");
        }
        try {
            subgitExecutable = Paths.get(workdir);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.subgit.executable.path' contains an invalid path!", e);
        }
    }

    private void initGitPathFromEnvironment(Environment environment) {
        String workdir = environment.getProperty("gitanizer.git.executable.path");
        if (workdir == null) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.git.executable.path' not set!");
        }
        try {
            gitExecutable = Paths.get(workdir);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.git.executable.path' contains an invalid path!", e);
        }
    }

    /**
     * Returns the path to the subgit executable.
     */
    public Path getSubgitExecutable() {
        return subgitExecutable;
    }

    /**
     * Returns the path to the git executable.
     */
    public Path getGitExecutable() {
        return gitExecutable;
    }
}
