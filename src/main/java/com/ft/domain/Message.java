package com.ft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;

    @Lob
    @Column(name = "text")
    private String text;

    @Column(name = "tags")
    private String tags;

    @Column(name = "request_at")
    private ZonedDateTime requestAt;

    @Column(name = "response_at")
    private ZonedDateTime responseAt;

    @Column(name = "delivered_at")
    private ZonedDateTime deliveredAt;

    @Min(value = -9)
    @Max(value = 9)
    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JsonIgnoreProperties("")
    private SmppRoutingInfo route;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public Message source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public Message destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public Message text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public Message tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ZonedDateTime getRequestAt() {
        return requestAt;
    }

    public Message requestAt(ZonedDateTime requestAt) {
        this.requestAt = requestAt;
        return this;
    }

    public void setRequestAt(ZonedDateTime requestAt) {
        this.requestAt = requestAt;
    }

    public ZonedDateTime getResponseAt() {
        return responseAt;
    }

    public Message responseAt(ZonedDateTime responseAt) {
        this.responseAt = responseAt;
        return this;
    }

    public void setResponseAt(ZonedDateTime responseAt) {
        this.responseAt = responseAt;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public Message deliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        return this;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Integer getState() {
        return state;
    }

    public Message state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public SmppRoutingInfo getRoute() {
        return route;
    }

    public Message route(SmppRoutingInfo smppRoutingInfo) {
        this.route = smppRoutingInfo;
        return this;
    }

    public void setRoute(SmppRoutingInfo smppRoutingInfo) {
        this.route = smppRoutingInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", text='" + getText() + "'" +
            ", tags='" + getTags() + "'" +
            ", requestAt='" + getRequestAt() + "'" +
            ", responseAt='" + getResponseAt() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", state=" + getState() +
            "}";
    }
}
