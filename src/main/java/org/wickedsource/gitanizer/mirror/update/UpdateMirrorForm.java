package org.wickedsource.gitanizer.mirror.update;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.wickedsource.gitanizer.mirror.domain.OptionalGroup;

import javax.validation.constraints.NotNull;

/**
 * Contains the data necessary to edit an existing repository mirror.
 */
@OptionalGroup(fieldNames = {"svnUsername", "svnPassword"}, message = "{mirrorForm.svnCredentials.invalid}")
public class UpdateMirrorForm {

    private long id;

    @NotNull(message = "{mirrorForm.repositoryName.notNull}")
    @NotBlank(message = "{mirrorForm.repositoryName.notNull}")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
