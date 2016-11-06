package org.wickedsource.gitanizer.subgit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

public class ImportCommand extends SubgitCommand {

    private String sourceSvnUrl;

    private String targetGitPath;

    private String username;

    private String password;

    private SubgitImportListener listener;

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

    public ImportCommand withListener(SubgitImportListener listener) {
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
        ProgressListenerOutputStream progressListenerOutputStream = new ProgressListenerOutputStream();
        progressListenerOutputStream.registerProgressListener(this.listener);
        // TODO: route error output to a separate monitored outputstream
        executor.setStreamHandler(new PumpStreamHandler(progressListenerOutputStream, System.err));
        executor.execute(commandLine);
    }


}
