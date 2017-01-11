package org.wickedsource.gitanizer.mirror.importing;

import java.io.OutputStream;
import java.util.concurrent.Future;

public class ImportTask {

    private final Future future;

    private final OutputStream logOutputStream;

    public ImportTask(Future future, OutputStream logOutputStream) {
        this.future = future;
        this.logOutputStream = logOutputStream;
    }

    public Future getFuture() {
        return future;
    }

    public OutputStream getLogOutputStream() {
        return logOutputStream;
    }
}
