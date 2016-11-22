package org.wickedsource.gitanizer.mirror.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.wickedsource.gitanizer.mirror.validation.UniqueMirrorName;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Contains the data necessary to create a new repository mirror.
 */
public class CreateMirrorForm {

    @NotNull(message = "{createMirrorForm.repositoryName.notNull}")
    @NotBlank(message = "{createMirrorForm.repositoryName.notNull}")
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "{createMirrorForm.repositoryName.invalid}")
    @UniqueMirrorName(message = "{createMirrorForm.repositoryName.duplicate}")
    private String repositoryName;

    @NotNull(message = "{createMirrorForm.remoteSvnUrl.notNull}")
    @NotBlank(message = "{createMirrorForm.remoteSvnUrl.notNull}")
    @URL(message = "{createMirrorForm.remoteSvnUrl.invalid}")
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
}
