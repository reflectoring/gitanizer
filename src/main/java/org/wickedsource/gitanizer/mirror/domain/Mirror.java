package org.wickedsource.gitanizer.mirror.domain;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Table
public class Mirror {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column
    private URL remoteSvnUrl;

    @Column
    private URL localGitUrl;

    @Column
    private LocalDateTime lastUpdated;

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

    public URL getLocalGitUrl() {
        return localGitUrl;
    }

    public void setLocalGitUrl(URL localGitUrl) {
        this.localGitUrl = localGitUrl;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
