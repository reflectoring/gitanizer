package org.wickedsource.gitanizer.subgit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An OutputStream that forwards error messages to a listener.
 */
public class ErrorListenerOutputStream extends OutputStream {

    private StringBuilder currentLine = new StringBuilder();

    private List<SubgitImportListener> listeners = new ArrayList<>();

    @Override
    public void write(int b) throws IOException {
        if (b == '\n' || b == '\r') {
            for (SubgitImportListener listener : this.listeners) {
                listener.onError(currentLine.toString());
            }
            currentLine = new StringBuilder();
        } else {
            currentLine.append(new String(new byte[]{(byte) b}));
        }
    }

    /**
     * Register a progress monitor that will be notified each time a progress event was
     * registered in this OutputStream.
     */
    public void registerErrorListener(SubgitImportListener errorListener) {
        this.listeners.add(errorListener);
    }
}
