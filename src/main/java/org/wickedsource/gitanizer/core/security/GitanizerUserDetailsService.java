package org.wickedsource.gitanizer.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.wickedsource.gitanizer.core.security.GitanizerSecurityConfiguration.getAuthorityNameForMirror;

@Component
public class GitanizerUserDetailsService implements UserDetailsService {

    private MirrorRepository mirrorRepository;

    private Map<String, UserDetails> configuredUsers;

    @Autowired
    public GitanizerUserDetailsService(MirrorRepository mirrorRepository, Environment environment) {
        this.mirrorRepository = mirrorRepository;
        Map<String, String> userProperties = getAllUserProperties(environment);
        configuredUsers = mapToUsers(userProperties);
    }

    /**
     * Returns true if a user with the specified user name exists.
     */
    public boolean usernameExists(String username) {
        if (configuredUsers.containsKey(username)) {
            return true;
        }
        return mirrorRepository.countByGitUsername(username) > 0;
    }

    /**
     * Returns true if a user with the specified user name exists for a mirror other then the mirror with the
     * specified ID.
     */
    public boolean usernameExists(String username, Long excludedMirrorId) {
        if (configuredUsers.containsKey(username)) {
            return true;
        }
        return mirrorRepository.countByGitUsernameExcludeId(username, excludedMirrorId) > 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // UI user that logs in via login form
        if (configuredUsers.containsKey(username)) {
            // returning a copy since the password is set to null by Spring Security
            return copy(configuredUsers.get(username));
        }

        // Technical user that accesses Git repositories via Basic Authentication
        Mirror mirror = mirrorRepository.findByGitUsername(username);
        if (mirror == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
        }

        return new User(username, mirror.getGitPassword(), Collections.singleton(new SimpleGrantedAuthority(getAuthorityNameForMirror(mirror.getId()))));
    }

    private User copy(UserDetails user) {
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    private Map<String, String> getAllUserProperties(Environment env) {
        Map<String, String> map = new HashMap<>();
        if (env instanceof ConfigurableEnvironment) {
            for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
                if (propertySource instanceof EnumerablePropertySource) {
                    for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
                        if (key.startsWith("gitanizer.users")) {
                            map.put(key, (String) propertySource.getProperty(key));
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, UserDetails> mapToUsers(Map<String, String> userProperties) {
        Map<String, UserDetails> users = new HashMap<>();

        for (Map.Entry entry : userProperties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            String username;

            Pattern userKeyPattern = Pattern.compile("^gitanizer\\.users\\.([^.]+)");
            Matcher matcher = userKeyPattern.matcher(key);
            if (matcher.matches()) {
                username = matcher.group(1);
            } else {
                throw new UsernameNotFoundException(String.format("Invalid property key used for user configuration! Found key '%s'. Required format: '%s'", key, "gitanizer.users.<USERNAME>"));
            }

            String[] attributes = value.split("\\s*,\\s*");
            String password = attributes[0];

            Set<SimpleGrantedAuthority> roles = new HashSet<>();
            for (int i = 1; i < attributes.length; i++) {
                roles.add(new SimpleGrantedAuthority(attributes[i]));
            }

            users.put(username, new User(username, password, roles));
        }
        return users;
    }
}
