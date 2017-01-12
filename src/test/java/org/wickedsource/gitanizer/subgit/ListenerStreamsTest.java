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
        CommandListener listener = new CommandListener();
        errorOutputStream.withErrorListener(listener);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(errorOutputStream));

        writer.write("complete line");
        writer.newLine();
        writer.write("line start.");
        writer.flush();
        writer.write("line end.");
        writer.newLine();
        writer.close();

        assertThat(listener.getErrorMessages()).contains("complete line");
        assertThat(listener.getErrorMessages()).contains("line start.line end.");
    }

    @Test
    public void progressEventsAreReported() throws IOException {
        SubgitImportProgressListenerOutputStream progressListenerOutputStream = new SubgitImportProgressListenerOutputStream();
        CommandListener listener = new CommandListener();
        progressListenerOutputStream.withProgressListener(listener);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(progressListenerOutputStream));

        writer.write("nothing to report");
        writer.flush();
        assertThat(listener.getProgressReports()).isEmpty();
        writer.write("new progress: 50%");
        writer.flush();
        assertThat(listener.getProgressReports()).hasSize(1);
        writer.newLine();
        writer.write("another progress event: 52% and some text after");
        writer.flush();
        assertThat(listener.getProgressReports()).hasSize(2);
        writer.write("yet another : 53% and some text after");
        writer.flush();
        writer.close();

        assertThat(listener.getProgressReports()).hasSize(3);
        assertThat(listener.getProgressReports()).contains(50);
        assertThat(listener.getProgressReports()).contains(52);
        assertThat(listener.getProgressReports()).contains(53);
    }

    private static class CommandListener implements ImportCommandListener {
        private List<String> errorMessages = new ArrayList<>();
        private List<Integer> progressReports = new ArrayList<>();

        @Override
        public void onProgress(int percentage) {
            this.progressReports.add(percentage);
        }

        @Override
        public void onError(String errorMessage) {
            errorMessages.add(errorMessage);
        }

        List<String> getErrorMessages() {
            return errorMessages;
        }

        List<Integer> getProgressReports() {
            return progressReports;
        }
    }

}