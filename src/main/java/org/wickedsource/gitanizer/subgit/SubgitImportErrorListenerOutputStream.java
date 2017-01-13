package org.wickedsource.gitanizer.subgit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An OutputStream that forwards error messages to a listener.
 */
public class SubgitImportErrorListenerOutputStream extends OutputStream {

    private StringBuilder currentLine = new StringBuilder();

    private List<ErrorListener> listeners = new ArrayList<>();

    @Override
    public void write(int b) throws IOException {
        if (b == '\n' || b == '\r') {
            for (ErrorListener listener : this.listeners) {
                listener.onError(currentLine.toString());
            }
            currentLine = new StringBuilder();
        } else {
            currentLine.append(new String(new byte[]{(byte) b}));
        }
    }

    /**
     * Register a progress monitor that will be notified each time an error event was
     * registered in this OutputStream.
     */
    public SubgitImportErrorListenerOutputStream withErrorListener(ErrorListener errorListener) {
        this.listeners.add(errorListener);
        return this;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
