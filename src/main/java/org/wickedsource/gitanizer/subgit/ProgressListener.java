package org.wickedsource.gitanizer.subgit;

@FunctionalInterface
public interface ProgressListener {

    void onProgress(int percentage);

}
