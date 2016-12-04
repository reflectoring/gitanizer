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

    @Autowired
    public SubgitConfiguration(Environment environment) {
        initSubgitPathFromEnvironment(environment);
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

    /**
     * Returns the path to the subgit executable.
     */
    public Path getSubgitExecutable() {
        return subgitExecutable;
    }

}
