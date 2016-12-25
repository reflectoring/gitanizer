package org.wickedsource.gitanizer.subgit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Wrapper around the subgit import command that allows to create a local git mirror of a remote
 * SVN repository.
 */
public class ImportCommand extends SubgitCommand {

    private String sourceSvnUrl;

    private String targetGitPath;

    private String username;

    private String password;

    private OutputStream out;

    private ImportCommandListener listener;

    private String workingDirectory;

    public ImportCommand(String subgitPath) {
        super(subgitPath);
    }

    public ImportCommand withSourceSvnUrl(String sourceSvnUrl) {
        this.sourceSvnUrl = sourceSvnUrl;
        return this;
    }

    public ImportCommand withTargetGitPath(String targetGitPath) {
        this.targetGitPath = targetGitPath;
        return this;
    }

    public ImportCommand withUsername(String username) {
        this.username = username;
        return this;
    }

    public ImportCommand withPassword(String password) {
        this.password = password;
        return this;
    }

    public ImportCommand withListener(ImportCommandListener listener) {
        this.listener = listener;
        return this;
    }

    public ImportCommand withWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    /**
     * Registers an OutputStream that receives all Output from subgit during the
     * import process. Can be used to create a log file of the import process.
     *
     * @param out the OutputStream to receive the output.
     * @return this object for chaining.
     */
    public ImportCommand withLogOutputStream(OutputStream out) {
        this.out = new NewlineAfterProgressBarOutputStream(out);
        return this;
    }

    public void execute() throws IOException {
        CommandLine commandLine = new CommandLine(getSubgitPath());
        commandLine.addArgument("import");
        commandLine.addArgument("--svn-url");
        commandLine.addArgument(this.sourceSvnUrl);
        commandLine.addArgument("--non-interactive");
        commandLine.addArgument(this.targetGitPath);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(this.workingDirectory));
        executor.setStreamHandler(new PumpStreamHandler(
                getProgressListener(this.listener),
                getErrorListener(this.listener)));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        try {
            if (this.out != null) {
                out.write("----------------------\r\n".getBytes());
                out.write(String.format("%s - Starting subgit import run\r\n", formatter.format(LocalDateTime.now())).getBytes());
            }
            executor.execute(commandLine);
            if (this.out != null) {
                out.write(String.format("%s - Finished subgit import run\r\n", formatter.format(LocalDateTime.now())).getBytes());
                out.write("----------------------\r\n".getBytes());
            }
        } catch (Exception e) {
            out.close();
        }
    }

    private ProgressListenerOutputStream getProgressListener(ImportCommandListener listener) {
        ProgressListenerOutputStream progressListenerOutputStream = new ProgressListenerOutputStream();
        progressListenerOutputStream.registerProgressListener(listener);
        progressListenerOutputStream.registerOutputStream(this.out);
        return progressListenerOutputStream;
    }

    private ErrorListenerOutputStream getErrorListener(ImportCommandListener listener) {
        ErrorListenerOutputStream errorListenerOutputStream = new ErrorListenerOutputStream();
        errorListenerOutputStream.registerErrorListener(listener);
        errorListenerOutputStream.registerOutputStream(this.out);
        return errorListenerOutputStream;
    }

    @Override
    public String toString() {
        return "ImportCommand{" +
                "sourceSvnUrl='" + sourceSvnUrl + '\'' +
                ", targetGitPath='" + targetGitPath + '\'' +
                '}';
    }
}
