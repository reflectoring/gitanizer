package org.wickedsource.gitanizer.status.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class StatusMessage {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private StatusMessageType type;

    @Column
    private String message;

    @Column
    private LocalDateTime timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusMessageType getType() {
        return type;
    }

    public void setType(StatusMessageType type) {
        this.type = type;
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
}
