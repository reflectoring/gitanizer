package org.wickedsource.gitanizer.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

@Configuration
@EnableWebSecurity
public class GitanizerSecurityConfiguration {

    private MirrorRepository mirrorRepository;

    @Autowired
    public GitanizerSecurityConfiguration(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
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
                    /**/.failureUrl("/login-error")
                    /**/.loginProcessingUrl("/do-login")
                    /**/.passwordParameter("password")
                    /**/.usernameParameter("username")
                    .and()
                    .logout()
                    /**/.logoutSuccessUrl("/login")
                    .addLogoutHandler((request, response, authentication) -> {
                        request.getSession().invalidate();
                        SecurityContext securityContext = SecurityContextHolder.getContext();
                        securityContext.setAuthentication(null);
                    })
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
