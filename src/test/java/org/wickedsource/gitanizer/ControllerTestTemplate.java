package org.wickedsource.gitanizer;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GitanizerApplication.class)
@WebAppConfiguration
public abstract class ControllerTestTemplate {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .build();
    }

    protected MockMvc mvc() {
        return mvc;
    }

}
