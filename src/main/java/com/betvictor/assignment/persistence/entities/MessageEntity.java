package com.betvictor.assignment.persistence.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class MessageEntity implements Serializable {

    @Id
    @Column(name="ID")
    private String id;

    @Column(name="SENDER")
    private String sender;

    @Column(name="RECIPIENT")
    private String recipient;

    private String text;

    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
