package org.wickedsource.gitanizer.mirror.domain;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * A Mirror entity contains coordinates to a remote SVN repository and information on a GIT repository
 * into which the SVN remote should be imported.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Mirror {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private URL remoteSvnUrl;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Column(nullable = false)
    private boolean syncActive;

    @Column
    private String lastStatusMessage;

    @Column
    private String workdirName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getRemoteSvnUrl() {
        return remoteSvnUrl;
    }

    public void setRemoteSvnUrl(URL remoteSvnUrl) {
        this.remoteSvnUrl = remoteSvnUrl;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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
}
