package org.wickedsource.gitanizer.mirror.create;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.wickedsource.gitanizer.mirror.domain.OptionalGroup;
import org.wickedsource.gitanizer.mirror.domain.OptionalGroups;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Contains the data necessary to create a new repository mirror.
 */
@OptionalGroups({
        @OptionalGroup(fieldNames = {"svnUsername", "svnPassword"}, message = "{mirrorForm.svnCredentials.invalid}"),
        @OptionalGroup(fieldNames = {"gitUsername", "gitPassword"}, message = "{mirrorForm.gitCredentials.invalid}")
})
public class CreateMirrorForm {

    @NotNull(message = "{mirrorForm.repositoryName.notNull}")
    @NotBlank(message = "{mirrorForm.repositoryName.notNull}")
    private String displayName;

    @NotNull(message = "{mirrorForm.remoteSvnUrl.notNull}")
    @NotBlank(message = "{mirrorForm.remoteSvnUrl.notNull}")
    @URL(message = "{mirrorForm.remoteSvnUrl.invalid}")
    private String remoteSvnUrl;

    private String svnUsername;

    private String svnPassword;

    private String gitUsername;

    private String gitPassword;

    @NotNull
    @Pattern(regexp = "[.a-zA-Z0-9_-]+", message = "{mirrorForm.gitRepositoryName.invalid}")
    private String gitRepositoryName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }

    public String getGitRepositoryName() {
        return gitRepositoryName;
    }

    public void setGitRepositoryName(String gitRepositoryName) {
        this.gitRepositoryName = gitRepositoryName;
    }
}
