package org.wickedsource.gitanizer.status.domain;

import org.wickedsource.gitanizer.mirror.domain.Mirror;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This entity contains a single status message for a repository mirror. A status message describes
 * a point in time in the lifecycle of a repository mirror.
 */
@Entity
@Table
public class StatusMessage {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String message;

    @Column
    private LocalDateTime timestamp;

    @ManyToOne
    private Mirror mirror;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public void setMirror(Mirror mirror) {
        this.mirror = mirror;
    }
}
