package org.wickedsource.gitanizer.core.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GitanizerUserDetailsServiceTest {

    @Mock
    MirrorRepository mirrorRepository;

    MockEnvironment environment = new MockEnvironment();


    @Test
    public void findsLoginFormUsers() {
        environment.setProperty("gitanizer.users.admin", "password,ADMIN,USER");
        environment.setProperty("gitanizer.users.user", "password,USER");
        GitanizerUserDetailsService service = new GitanizerUserDetailsService(mirrorRepository, environment);

        UserDetails admin = service.loadUserByUsername("admin");
        assertThat(admin.getUsername()).isEqualTo("admin");
        assertThat(admin.getPassword()).isEqualTo("password");
        assertThat(admin.getAuthorities()).hasSize(2);
        assertThat(containsRole(admin.getAuthorities(), "ADMIN")).isTrue();
        assertThat(containsRole(admin.getAuthorities(), "USER")).isTrue();

        UserDetails user = service.loadUserByUsername("user");
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getAuthorities()).hasSize(1);
        assertThat(containsRole(user.getAuthorities(), "USER")).isTrue();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void failsFindingLoginFormUsers() {
        environment.setProperty("gitanizer.users.admin.asd", "");
        environment.setProperty("gitanizer.users.user", "password,USER");
        GitanizerUserDetailsService service = new GitanizerUserDetailsService(mirrorRepository, environment);
        service.loadUserByUsername("admin");
    }

    @Test
    public void findsBasicAuthUsers() {
        Mirror mirror = new Mirror();
        mirror.setId(42);
        mirror.setGitUsername("user123");
        mirror.setGitPassword("password");

        when(mirrorRepository.findByGitUsername("user123")).thenReturn(mirror);
        GitanizerUserDetailsService service = new GitanizerUserDetailsService(mirrorRepository, environment);

        UserDetails user123 = service.loadUserByUsername("user123");
        assertThat(user123.getUsername()).isEqualTo("user123");
        assertThat(user123.getPassword()).isEqualTo("password");
        assertThat(user123.getAuthorities()).hasSize(1);
        assertThat(containsRole(user123.getAuthorities(), "MIRROR-42")).isTrue();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void failsFindingBasicAuthUsers(){
        when(mirrorRepository.findByGitUsername("user123")).thenReturn(null);
        GitanizerUserDetailsService service = new GitanizerUserDetailsService(mirrorRepository, environment);
        service.loadUserByUsername("user123");
    }

    private static boolean containsRole(Collection<? extends GrantedAuthority> authorities, String role) {
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        while (iterator.hasNext()) {
            GrantedAuthority authority = iterator.next();
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

}