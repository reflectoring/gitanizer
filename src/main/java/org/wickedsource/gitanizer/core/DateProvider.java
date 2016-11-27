package org.wickedsource.gitanizer.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateProvider {

    private LocalDateTime now;

    @Autowired
    public DateProvider(Environment environment) {
        try {
            String nowString = environment.getProperty("gitanizer.now");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            if (nowString != null) {
                this.now = LocalDateTime.parse(nowString, formatter);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Environment variable 'gitanizer.now' has an invalid value. Must be a valid ISO date string like '2011-12-03T10:15:30'!", e);
        }
    }

    /**
     * Returns the current date and time. The current date can be overriden by setting the environment variable 'gitanizer.now' to a
     * valid ISO date time string for testing purposes.
     */
    public LocalDateTime now() {
        if (this.now != null) {
            return now;
        } else {
            return LocalDateTime.now();
        }
    }

}
