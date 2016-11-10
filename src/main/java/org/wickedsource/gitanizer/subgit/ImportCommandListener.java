package org.wickedsource.gitanizer.subgit;

public interface ImportCommandListener {

    void onProgress(int percentage);

    void onError(String errorMessage);

}
