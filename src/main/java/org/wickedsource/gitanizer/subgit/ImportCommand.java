package org.wickedsource.gitanizer.subgit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

/**
 * Wrapper around the subgit import command that allows to create a local git mirror of a remote
 * SVN repository.
 */
public class ImportCommand extends SubgitCommand {

    private String sourceSvnUrl;

    private String targetGitPath;

    private String username;

    private String password;

    private ImportCommandListener listener;

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

    public void execute() throws IOException {
        CommandLine commandLine = new CommandLine(getSubgitPath());
        commandLine.addArgument("import");
        commandLine.addArgument("--svn-url");
        commandLine.addArgument(this.sourceSvnUrl);
        commandLine.addArgument("--non-interactive");
        commandLine.addArgument(this.targetGitPath);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(
                getProgressListener(this.listener),
                getErrorListener(this.listener)));
        executor.execute(commandLine);
    }

    private ProgressListenerOutputStream getProgressListener(ImportCommandListener listener) {
        ProgressListenerOutputStream progressListenerOutputStream = new ProgressListenerOutputStream();
        progressListenerOutputStream.registerProgressListener(listener);
        return progressListenerOutputStream;
    }

    private ErrorListenerOutputStream getErrorListener(ImportCommandListener listener) {
        ErrorListenerOutputStream errorListenerOutputStream = new ErrorListenerOutputStream();
        errorListenerOutputStream.registerErrorListener(listener);
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
