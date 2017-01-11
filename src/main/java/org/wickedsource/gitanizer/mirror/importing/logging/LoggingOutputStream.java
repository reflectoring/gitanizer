package org.wickedsource.gitanizer.mirror.importing.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class LoggingOutputStream extends OutputStream {

    private Logger logger;

    private StringBuilder buffer = new StringBuilder();

    public LoggingOutputStream(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void write(int b) throws IOException {
        buffer.append(new String(new byte[]{(byte) b}));
        String line = buffer.toString();
        if (line.endsWith(System.getProperty("line.separator"))) {
            if (!StringUtils.isEmpty(line)) {
                logger.info(line.trim());
            }
            buffer = new StringBuilder();
        }
    }

}
