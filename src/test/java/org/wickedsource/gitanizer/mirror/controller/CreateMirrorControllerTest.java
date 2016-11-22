package org.wickedsource.gitanizer.mirror.controller;

import org.junit.Test;
import org.wickedsource.gitanizer.ControllerTestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.wickedsource.gitanizer.core.Routes.*;

public class CreateMirrorControllerTest extends ControllerTestTemplate {

    @Test
    public void displayForm() throws Exception {
        mvc().perform(get(CREATE_MIRROR))
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_MIRROR));
    }

    @Test
    public void createMirror() throws Exception {
        mvc().perform(post(CREATE_MIRROR)
                .param("repositoryName", "gitanizer")
                .param("remoteSvnUrl", "https://github.com/thombergs/gitanizer.git"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(redirect(LIST_MIRRORS)));
    }

    @Test
    public void createMirrorWithValidationError() throws Exception {
        mvc().perform(post(CREATE_MIRROR)
                .param("repositoryName", "")
                .param("remoteSvnUrl", "invalidUrl"))
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_MIRROR));
    }

}