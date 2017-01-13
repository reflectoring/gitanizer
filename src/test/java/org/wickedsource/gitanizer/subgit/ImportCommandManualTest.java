package org.wickedsource.gitanizer.subgit;

import java.io.IOException;

public class ImportCommandManualTest {

    /**
     * Edit the parameters of the ImportCommand and run the main method manually to test it.
     */
    public static void main(String[] args) throws IOException {
        new ImportCommand("D:\\programs\\subgit-3.2.2\\bin\\subgit.bat", "git")
                .withSourceSvnUrl("http://svn.apache.org/repos/asf/velocity/engine/")
                .withTargetGitPath("velocity.git")
                .withProgressListener(percentage -> System.out.println("Progress: " + percentage))
                .execute();
    }

}