package org.wickedsource.gitanizer.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WorkdirConfigurationTest {

    @Test(expected = IllegalArgumentException.class)
    public void failOnMissingPath() {
        MockEnvironment environment = new MockEnvironment();
        new WorkdirConfiguration(environment);
    }

    @Test
    public void failOnInvalidPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                MockEnvironment environment = new MockEnvironment();
                environment.setProperty("gitanizer.workdir", "oihtpß927t,;<>|gpsdn?`!?§?$)§");
                new WorkdirConfiguration(environment);
                Assert.fail("Expecting an IllegalArgumentException under Windows!");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
        }
    }

    @Test
    public void validPath() throws IOException {
        MockEnvironment environment = new MockEnvironment();
        File tempFile = File.createTempFile("gitanizer", "test");
        tempFile.deleteOnExit();
        environment.setProperty("gitanizer.workdir", tempFile.getAbsolutePath());
        WorkdirConfiguration workdirConfiguration = new WorkdirConfiguration(environment);
        Path workdir = workdirConfiguration.getSubWorkdir("fooöäüß_-*()bar");
        Assert.assertEquals("foo_bar", workdir.toFile().getName());
    }

}