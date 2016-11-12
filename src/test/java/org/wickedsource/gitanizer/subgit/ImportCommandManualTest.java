package org.wickedsource.gitanizer.subgit;

import java.io.IOException;

public class ImportCommandManualTest {

    /**
     * Edit the parameters of the ImportCommand and run the main method manually to test it.
     */
    public static void main(String[] args) throws IOException {
        new ImportCommand("D:\\programs\\subgit-3.2.2\\bin\\subgit.bat")
                .withSourceSvnUrl("http://svn.apache.org/repos/asf/velocity/engine/")
                .withTargetGitPath("velocity.git")
                .withListener(new ImportCommandListener() {
                    @Override
                    public void onProgress(int percentage) {
                        System.out.println("PROGRESS: " + percentage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        System.err.println(errorMessage);
                    }
                })
                .execute();
    }

}