package org.wickedsource.gitanizer.core;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkdirConfigurationTest {

    @Mock
    private MirrorRepository mirrorRepository;

    @Test(expected = IllegalArgumentException.class)
    public void failOnMissingPath() {
        MockEnvironment environment = new MockEnvironment();
        new WorkdirConfiguration(environment, mirrorRepository);
    }

    @Test
    public void failOnInvalidPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                MockEnvironment environment = new MockEnvironment();
                environment.setProperty("gitanizer.workdir", "oihtpß927t,;<>|gpsdn?`!?§?$)§");
                new WorkdirConfiguration(environment, mirrorRepository);
                Assert.fail("Expecting an IllegalArgumentException under Windows!");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
        }
    }

    @Test
    public void validPath() throws IOException {
        MockEnvironment environment = new MockEnvironment();
        when(mirrorRepository.findOne(42L)).thenReturn(mirror("fooöäüß_-*()bar"));
        File tempFile = File.createTempFile("gitanizer", "test");
        tempFile.deleteOnExit();
        environment.setProperty("gitanizer.workdir", tempFile.getAbsolutePath());
        WorkdirConfiguration workdirConfiguration = new WorkdirConfiguration(environment, mirrorRepository);
        Path workdir = workdirConfiguration.getWorkdir(42L);
        Assert.assertEquals("foo_bar", workdir.toFile().getName());
    }

    private Mirror mirror(String workdir) {
        Mirror mirror = new Mirror();
        mirror.setWorkdirName(workdir);
        return mirror;
    }

}