package org.wickedsource.gitanizer.mirror.create;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.wickedsource.gitanizer.mirror.domain.UniqueMirrorName;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Contains the data necessary to create a new repository mirror.
 */
public class CreateMirrorForm {

    private long id;

    @NotNull(message = "{mirrorForm.repositoryName.notNull}")
    @NotBlank(message = "{mirrorForm.repositoryName.notNull}")
    @UniqueMirrorName(message = "{mirrorForm.repositoryName.duplicate}")
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
