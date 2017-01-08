package org.wickedsource.gitanizer.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class GitanizerSecurityConfiguration {

    private MirrorRepository mirrorRepository;

    @Autowired
    public GitanizerSecurityConfiguration(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // UI users
            if (username.equals("admin")) {
                return new User("admin", "admin", Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER")));
            }

            if (username.equals("user")) {
                return new User("user", "user", Arrays.asList(new SimpleGrantedAuthority("USER")));
            }

            // Git users
            Mirror mirror = mirrorRepository.findByGitUsername(username);
            if (mirror == null) {
                throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
            }

            return new User(username, mirror.getGitPassword(), Collections.singleton(new SimpleGrantedAuthority(getAuthorityNameForMirror(mirror.getId()))));
        };
    }

    /**
     * Secures all web pages with a login form.
     */
    @Configuration
    @Order(2)
    public static class FormBasedSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/static/**/*").permitAll()
                    .antMatchers("/git/public/**/*").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    /**/.loginPage("/login")
                    /**/.loginProcessingUrl("/do-login")
                    /**/.passwordParameter("password")
                    /**/.usernameParameter("username")
                    .and()
                    .logout()
                    /**/.logoutSuccessUrl("/login")
                    /**/.logoutUrl("/logout");

            http.csrf().disable();
        }

    }

    /**
     * Secures all hosted Git repositories that require a username and password with Basic authentication.
     * These Git repositories are hosted under "/git/secure/**"
     */
    @Configuration
    @Order(1)
    public static class GitRepositorySecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/git/secure/**/*").authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
            http.csrf().disable();
        }
    }

    public static String getAuthorityNameForMirror(long mirrorId) {
        return String.format("MIRROR-%d", mirrorId);
    }

}
