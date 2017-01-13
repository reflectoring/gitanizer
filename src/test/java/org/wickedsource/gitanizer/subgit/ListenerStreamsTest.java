package org.wickedsource.gitanizer.subgit;


import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerStreamsTest {

    @Test
    public void errorEventsAreReported() throws IOException {
        SubgitImportErrorListenerOutputStream errorOutputStream = new SubgitImportErrorListenerOutputStream();
        List<String> errorMessages = new ArrayList<>();
        errorOutputStream.withErrorListener(errorMessage -> errorMessages.add(errorMessage));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(errorOutputStream));

        writer.write("complete line");
        writer.newLine();
        writer.write("line start.");
        writer.flush();
        writer.write("line end.");
        writer.newLine();
        writer.close();

        assertThat(errorMessages).contains("complete line");
        assertThat(errorMessages).contains("line start.line end.");
    }

    @Test
    public void progressEventsAreReported() throws IOException {
        SubgitImportProgressListenerOutputStream progressListenerOutputStream = new SubgitImportProgressListenerOutputStream();
        List<Integer> progressReports = new ArrayList<>();
        progressListenerOutputStream.withProgressListener(percentage -> progressReports.add(percentage));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(progressListenerOutputStream));

        writer.write("nothing to report");
        writer.flush();
        assertThat(progressReports).isEmpty();
        writer.write("new progress: 50%");
        writer.flush();
        assertThat(progressReports).hasSize(1);
        writer.newLine();
        writer.write("another progress event: 52% and some text after");
        writer.flush();
        assertThat(progressReports).hasSize(2);
        writer.write("yet another : 53% and some text after");
        writer.flush();
        writer.close();

        assertThat(progressReports).hasSize(3);
        assertThat(progressReports).contains(50);
        assertThat(progressReports).contains(52);
        assertThat(progressReports).contains(53);
    }

    private static class CommandListener implements ErrorListener {
        private List<String> errorMessages = new ArrayList<>();

        @Override
        public void onError(String errorMessage) {
            errorMessages.add(errorMessage);
        }

        List<String> getErrorMessages() {
            return errorMessages;
        }

    }

}