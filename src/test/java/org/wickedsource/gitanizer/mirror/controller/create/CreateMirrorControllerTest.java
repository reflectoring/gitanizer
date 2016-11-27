package org.wickedsource.gitanizer.mirror.controller.create;

import org.junit.Test;
import org.wickedsource.gitanizer.ControllerTestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class CreateMirrorControllerTest extends ControllerTestTemplate {

    @Test
    public void displayForm() throws Exception {
        mvc().perform(get("/mirrors/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/mirrors/create"));
    }

    @Test
    public void createMirror() throws Exception {
        mvc().perform(post("/mirrors/create")
                .param("repositoryName", "gitanizer")
                .param("remoteSvnUrl", "https://github.com/thombergs/gitanizer.git"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/mirrors/list"));
    }

    @Test
    public void createMirrorWithValidationError() throws Exception {
        mvc().perform(post("/mirrors/create")
                .param("repositoryName", "")
                .param("remoteSvnUrl", "invalidUrl"))
                .andExpect(status().isOk())
                .andExpect(view().name("/mirrors/create"));
    }

}