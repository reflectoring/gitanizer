package org.wickedsource.gitanizer.subgit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Wrapper around the subgit import command that allows to create a local git mirror of a remote
 * SVN repository.
 */
public class ImportCommand extends SubgitCommand {

    private String sourceSvnUrl;

    private String targetGitPath;

    private String username;

    private String password;

    private String workingDirectory;

    private String gitPath;

    private Logger logger;

    private ProgressListener progressListener;

    private ErrorListener errorListener;

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

    public ImportCommand withErrorListener(ErrorListener listener) {
        this.errorListener = listener;
        return this;
    }

    public ImportCommand withProgressListener(ProgressListener listener) {
        this.progressListener = listener;
        return this;
    }

    public ImportCommand withWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    public ImportCommand withLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public void execute() throws Exception {
        subgitImport();
        updateServerInfo();
    }

    private void subgitImport() throws Exception {
        List<String> commands = new ArrayList<>();
        commands.add(this.getSubgitPath());
        commands.add("import");
        commands.add("--svn-url");
        commands.add(this.sourceSvnUrl);
        commands.add("--non-interactive");

        if (!StringUtils.isEmpty(this.username)) {
            commands.add("--username");
            commands.add(this.username);
        }

        if (!StringUtils.isEmpty(this.password)) {
            commands.add("--password");
            commands.add(this.password);
        }

        commands.add(this.targetGitPath);

        try {
            logger.info(String.format("Calling subgit with command line '%s'", commandLine(commands)));
            new ProcessExecutor()
                    .command(commands)
                    .directory(new File(this.workingDirectory))
                    .redirectError(new NewlineAfterProgressBarOutputStream(Slf4jStream.of(this.logger).asError()))
                    .redirectErrorAlsoTo(new SubgitImportErrorListenerOutputStream()
                            .withErrorListener(this.errorListener))
                    .redirectOutput(new NewlineAfterProgressBarOutputStream(Slf4jStream.of(this.logger).asInfo()))
                    .redirectOutputAlsoTo(new SubgitImportProgressListenerOutputStream()
                            .withProgressListener(this.progressListener))
                    .destroyOnExit()
                    .execute();
        } catch (InterruptedException e) {
            logger.error(String.format("Import Task has been interrupted! Command line: '%s'", commandLine(commands)), e);
            throw e;
        } catch (TimeoutException e) {
            logger.error(String.format("Import Task has timed out! Command line: '%s'", commandLine(commands)), e);
            throw e;
        } catch (IOException e) {
            // Removing the first Exception in the stacktrace, since it outputs all command line arguments
            // and we don't want to log the password parameter. The cause exceptions are more interesting anyway.
            throw (Exception) e.getCause();
        }

    }

    private String commandLine(List<String> commands) {
        String commandLine = StringUtils.join(commands, " ");
        commandLine = commandLine.replaceAll("--password [^ ]+", "--password *****");
        return commandLine;
    }

    private void updateServerInfo() throws Exception {
        List<String> commands = Arrays.asList(getGitPath(), "update-server-info");
        try {
            logger.info(String.format("Calling git with command line '%s'", commandLine(commands)));
            new ProcessExecutor()
                    .command(commands)
                    .directory(new File(this.targetGitPath))
                    .redirectError(Slf4jStream.of(this.logger).asError())
                    .redirectOutput(Slf4jStream.of(this.logger).asInfo())
                    .destroyOnExit()
                    .execute();
        } catch (InterruptedException e) {
            logger.error(String.format("Updating git server info has been interrupted. Command line: '%s'", commandLine(commands)), e);
            throw e;
        } catch (TimeoutException e) {
            logger.error(String.format("Updating git server info has timed out. Command line: '%s'", commandLine(commands)), e);
            throw e;
        }
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
