package org.wickedsource.gitanizer.subgit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps an OutputStream to add a carriage return and newline after each "progress bar" that looks
 * like this: "[|||||     ]". Subgit uses a progress bar like this but puts multiple into a single
 * line which makes a logfile rather unreadable.
 */
public class NewlineAfterProgressBarOutputStream extends OutputStream {

    private OutputStream wrappedOutputStream;

    private StringBuilder buffer = new StringBuilder();

    private final static Pattern PROGRESS_PATTERN = Pattern.compile("\\[\\|*\\s*\\]");

    public NewlineAfterProgressBarOutputStream(OutputStream wrappedOutputStream) {
        this.wrappedOutputStream = wrappedOutputStream;
    }

    @Override
    public void write(int b) throws IOException {
        wrappedOutputStream.write(b);
        buffer.append(new String(new byte[]{(byte) b}));
        addNewlineIfApplicable();
    }

    private void addNewlineIfApplicable() throws IOException {
        Matcher matcher = PROGRESS_PATTERN.matcher(buffer.toString());
        if (matcher.find()) {
            wrappedOutputStream.write("\r\n".getBytes());
            buffer = new StringBuilder();
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        wrappedOutputStream.close();
    }

}
