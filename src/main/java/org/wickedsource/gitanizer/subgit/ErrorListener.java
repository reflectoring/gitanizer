package org.wickedsource.gitanizer.subgit;

@FunctionalInterface
public interface ErrorListener {

    void onError(String errorMessage);

}
