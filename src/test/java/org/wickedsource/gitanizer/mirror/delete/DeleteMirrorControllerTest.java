package org.wickedsource.gitanizer.mirror.delete;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Test;
import org.wickedsource.gitanizer.ControllerTestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteMirrorControllerTest extends ControllerTestTemplate {

    @Test
    @DatabaseSetup("/singleMirror.xml")
    @ExpectedDatabase("/empty.xml")
    public void deleteMirror() throws Exception {
        mvc().perform(get("/mirrors/1/delete"))
                .andExpect(status().is3xxRedirection());
    }

}