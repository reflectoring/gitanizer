package org.wickedsource.gitanizer.mirror.list;

import org.wickedsource.gitanizer.mirror.domain.MirrorStatus;

import java.time.LocalDateTime;

public class MirrorDTO {

    private long id;

    private String name;

    private LocalDateTime lastChangeDate;

    private boolean syncActive;

    private String gitCloneUrl;

    private LocalDateTime lastImportFinishedDate;

    private MirrorStatus status;

    private Integer progress;

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

    public LocalDateTime getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(LocalDateTime lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public boolean isSyncActive() {
        return syncActive;
    }

    public void setSyncActive(boolean syncActive) {
        this.syncActive = syncActive;
    }

    public String getGitCloneUrl() {
        return gitCloneUrl;
    }

    public void setGitCloneUrl(String gitCloneUrl) {
        this.gitCloneUrl = gitCloneUrl;
    }

    public LocalDateTime getLastImportFinishedDate() {
        return lastImportFinishedDate;
    }

    public void setLastImportFinishedDate(LocalDateTime lastImportFinishedDate) {
        this.lastImportFinishedDate = lastImportFinishedDate;
    }

    public MirrorStatus getStatus() {
        return status;
    }

    public void setStatus(MirrorStatus status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
