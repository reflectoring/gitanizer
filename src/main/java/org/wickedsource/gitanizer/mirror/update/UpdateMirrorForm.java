package org.wickedsource.gitanizer.mirror.update;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Contains the data necessary to edit an existing repository mirror.
 */
public class UpdateMirrorForm {

    private long id;

    @NotNull(message = "{mirrorForm.repositoryName.notNull}")
    @NotBlank(message = "{mirrorForm.repositoryName.notNull}")
    private String repositoryName;

    @NotNull(message = "{mirrorForm.remoteSvnUrl.notNull}")
    @NotBlank(message = "{mirrorForm.remoteSvnUrl.notNull}")
    @URL(message = "{mirrorForm.remoteSvnUrl.invalid}")
    private String remoteSvnUrl;

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRemoteSvnUrl() {
        return remoteSvnUrl;
    }

    public void setRemoteSvnUrl(String remoteSvnUrl) {
        this.remoteSvnUrl = remoteSvnUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
