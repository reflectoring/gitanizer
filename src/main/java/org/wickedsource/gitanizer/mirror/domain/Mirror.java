package org.wickedsource.gitanizer.mirror.domain;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * A Mirror entity contains coordinates to a remote SVN repository and information on a GIT repository
 * into which the SVN remote should be imported.
 */
@Entity
@Table
public class Mirror {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String displayName;

    @Column(nullable = false)
    private URL remoteSvnUrl;

    @Column
    private String svnUsername;

    @Column
    private String svnPassword;

    @Column(nullable = false)
    private LocalDateTime lastStatusUpdate;

    @Column
    private LocalDateTime lastImportFinished;

    @Column(nullable = false)
    private boolean syncActive;

    @Column
    private String lastStatusMessage;

    @Column(unique = true, nullable = false)
    private String gitRepositoryName;

    @Column(unique = true, nullable = false)
    private String workdirName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.gitRepositoryName = displayName.replaceAll("\\W", "-") + ".git";
    }

    public URL getRemoteSvnUrl() {
        return remoteSvnUrl;
    }

    public void setRemoteSvnUrl(URL remoteSvnUrl) {
        this.remoteSvnUrl = remoteSvnUrl;
    }

    public LocalDateTime getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    public void setLastStatusUpdate(LocalDateTime lastStatusUpdate) {
        this.lastStatusUpdate = lastStatusUpdate;
    }

    public boolean isSyncActive() {
        return syncActive;
    }

    public void setSyncActive(boolean syncActive) {
        this.syncActive = syncActive;
    }

    public String getLastStatusMessage() {
        return lastStatusMessage;
    }

    public void setLastStatusMessage(String lastStatusMessage) {
        this.lastStatusMessage = lastStatusMessage;
    }

    public String getWorkdirName() {
        return workdirName;
    }

    public void setWorkdirName(String workdirName) {
        this.workdirName = workdirName;
    }

    private String getGitRepositoryName() {
        return gitRepositoryName;
    }

    private void setGitRepositoryName(String gitRepositoryName) {
        this.gitRepositoryName = gitRepositoryName;
    }

    public LocalDateTime getLastImportFinished() {
        return lastImportFinished;
    }

    public void setLastImportFinished(LocalDateTime lastImportFinished) {
        this.lastImportFinished = lastImportFinished;
    }

    public String getSvnUsername() {
        return svnUsername;
    }

    public void setSvnUsername(String svnUsername) {
        this.svnUsername = svnUsername;
    }

    public String getSvnPassword() {
        return svnPassword;
    }

    public void setSvnPassword(String svnPassword) {
        this.svnPassword = svnPassword;
    }
}
