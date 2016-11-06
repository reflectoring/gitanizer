package org.wickedsource.gitanizer.subgit;

public abstract class SubgitCommand {

    private final String subgitPath;

    public SubgitCommand(String subgitPath) {
        this.subgitPath = subgitPath;
    }

    public String getSubgitPath() {
        return subgitPath;
    }
}
