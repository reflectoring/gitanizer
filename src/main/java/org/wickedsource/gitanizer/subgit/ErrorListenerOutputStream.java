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

    private List<ImportCommandListener> listeners = new ArrayList<>();

    private OutputStream out;

    @Override
    public void write(int b) throws IOException {
        if (this.out != null) {
            out.write(b);
        }
        if (b == '\n' || b == '\r') {
            for (ImportCommandListener listener : this.listeners) {
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
    public void registerErrorListener(ImportCommandListener errorListener) {
        this.listeners.add(errorListener);
    }

    /**
     * Registers an OutputStream that receives all output that is sent to this ErrorListenerOutputStream.
     *
     * @param out the OutputStream to receive all output.
     */
    public void registerOutputStream(OutputStream out) {
        this.out = out;
    }
}
