package org.wickedsource.gitanizer;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class})
@SpringBootTest(classes = GitanizerApplication.class, properties = {
        "gitanizer.now=2016-11-27T12:00:00",
        "gitanizer.workdir=gitanizerWorkdir",
        "gitanizer.subgit.executable.path=foobar",
        "gitanizer.git.executable.path=git"
})
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
