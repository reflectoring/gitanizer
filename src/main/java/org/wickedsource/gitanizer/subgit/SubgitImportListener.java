package org.wickedsource.gitanizer.subgit;

public interface SubgitImportListener {

    void onProgress(int percentage);

    void onError(String errorMessage);

}
