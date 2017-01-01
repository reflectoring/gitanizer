package org.wickedsource.gitanizer.mirror.create;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.wickedsource.gitanizer.mirror.domain.OptionalGroup;
import org.wickedsource.gitanizer.mirror.domain.UniqueMirrorName;

import javax.validation.constraints.NotNull;

/**
 * Contains the data necessary to create a new repository mirror.
 */
@OptionalGroup(fieldNames = {"svnUsername", "svnPassword"}, message="{mirrorForm.svnCredentials.invalid}")
public class CreateMirrorForm {

    @NotNull(message = "{mirrorForm.repositoryName.notNull}")
    @NotBlank(message = "{mirrorForm.repositoryName.notNull}")
    @UniqueMirrorName(message = "{mirrorForm.repositoryName.duplicate}")
    private String repositoryName;

    @NotNull(message = "{mirrorForm.remoteSvnUrl.notNull}")
    @NotBlank(message = "{mirrorForm.remoteSvnUrl.notNull}")
    @URL(message = "{mirrorForm.remoteSvnUrl.invalid}")
    private String remoteSvnUrl;

    private String svnUsername;

    private String svnPassword;

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
