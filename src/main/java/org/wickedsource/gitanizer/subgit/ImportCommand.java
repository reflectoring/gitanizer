package org.wickedsource.gitanizer.subgit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;

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

    private String gitPath;

    public ImportCommand(String subgitPath, String gitPath) {
        super(subgitPath);
        this.gitPath = gitPath;
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
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try {
            if (this.out != null) {
                out.write("----------------------\r\n".getBytes());
                out.write(String.format("%s - Starting subgit import run\r\n", formatter.format(LocalDateTime.now())).getBytes());
            }
            subgitImport();
            updateServerInfo();
            if (this.out != null) {
                out.write(String.format("%s - Finished subgit import run\r\n", formatter.format(LocalDateTime.now())).getBytes());
                out.write("----------------------\r\n".getBytes());
            }
        } catch (Exception e) {
            out.close();
        }
    }

    private void subgitImport() throws IOException {
        CommandLine commandLine = new CommandLine(getSubgitPath());
        commandLine.addArgument("import");
        commandLine.addArgument("--svn-url");
        commandLine.addArgument(this.sourceSvnUrl);
        commandLine.addArgument("--non-interactive");

        if (!StringUtils.isEmpty(this.username)) {
            commandLine.addArgument("--username");
            commandLine.addArgument(this.username);
        }

        if (!StringUtils.isEmpty(this.password)) {
            commandLine.addArgument("--password");
            commandLine.addArgument(this.password);
        }

        commandLine.addArgument(this.targetGitPath);

        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(this.workingDirectory));
        executor.setStreamHandler(new PumpStreamHandler(
                getProgressListener(this.listener),
                getErrorListener(this.listener)));

        executor.execute(commandLine);
    }

    private void updateServerInfo() throws IOException {
        CommandLine commandLine = new CommandLine(getGitPath());
        commandLine.addArgument("update-server-info");

        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(this.targetGitPath));
        executor.setStreamHandler(new PumpStreamHandler(out, out));

        executor.execute(commandLine);
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

    public String getGitPath() {
        return gitPath;
    }
}
