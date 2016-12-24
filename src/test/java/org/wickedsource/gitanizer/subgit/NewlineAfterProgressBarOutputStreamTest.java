package org.wickedsource.gitanizer.subgit;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class NewlineAfterProgressBarOutputStreamTest {

    @Test
    public void noNewlineAddedWhenNoMatch() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NewlineAfterProgressBarOutputStream newlineAddingOut = new NewlineAfterProgressBarOutputStream(out);
        newlineAddingOut.write("abc foo bar".getBytes());
        newlineAddingOut.close();
        assertThat(out.toString().equals("abc foo bar"));
    }

    @Test
    public void newlineAddedWhenMatch() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NewlineAfterProgressBarOutputStream newlineAddingOut = new NewlineAfterProgressBarOutputStream(out);
        newlineAddingOut.write("abc foo bar [||||    ]".getBytes());
        newlineAddingOut.close();
        assertThat(out.toString().equals("abc foo abc foo bar [||||    ]\r\n"));
    }

}
