package org.wickedsource.gitanizer.mirror.update;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Test;
import org.wickedsource.gitanizer.ControllerTestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UpdateMirrorControllerTest extends ControllerTestTemplate {

    @Test
    @DatabaseSetup("/empty.xml")
    @ExpectedDatabase("/empty.xml")
    public void displayForm404() throws Exception {
        mvc().perform(get("/mirrors/42/update"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("/singleMirror.xml")
    @ExpectedDatabase("/singleMirror.xml")
    public void displayForm() throws Exception {
        mvc().perform(get("/mirrors/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/mirrors/update"));
    }

    @Test
    @DatabaseSetup("/singleMirror.xml")
    @ExpectedDatabase("/singleMirror2.xml")
    public void editMirror() throws Exception {
        mvc().perform(post("/mirrors/1/update")
                .param("repositoryName", "gitanizer")
                .param("remoteSvnUrl", "https://github.com/thombergs/gitanizer.git"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/mirrors/list"));
    }

    @Test
    @DatabaseSetup("/singleMirror.xml")
    @ExpectedDatabase("/singleMirror.xml")
    public void editMirrorWithValidationError() throws Exception {
        mvc().perform(post("/mirrors/1/update")
                .param("repositoryName", "")
                .param("remoteSvnUrl", "invalidUrl"))
                .andExpect(status().isOk())
                .andExpect(view().name("/mirrors/update"));
    }

    @Test
    @DatabaseSetup("/singleMirror.xml")
    @ExpectedDatabase("/singleMirror.xml")
    public void editMirrorWithDuplicateName() throws Exception {
        mvc().perform(post("/mirrors/1/update")
                .param("repositoryName", "coderadar")
                .param("remoteSvnUrl", "invalidUrl"))
                .andExpect(status().isOk())
                .andExpect(view().name("/mirrors/update"));
    }

}