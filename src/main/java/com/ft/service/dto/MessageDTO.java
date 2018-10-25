package com.ft.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Message entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    private String source;

    private String destination;

    @Lob
    private String text;

    private String tags;

    private ZonedDateTime requestAt;

    private ZonedDateTime responseAt;

    private ZonedDateTime deliveredAt;

    @Min(value = -9)
    @Max(value = 9)
    private Integer state;

    private Long routeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ZonedDateTime getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(ZonedDateTime requestAt) {
        this.requestAt = requestAt;
    }

    public ZonedDateTime getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(ZonedDateTime responseAt) {
        this.responseAt = responseAt;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long smppRoutingInfoId) {
        this.routeId = smppRoutingInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (messageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", text='" + getText() + "'" +
            ", tags='" + getTags() + "'" +
            ", requestAt='" + getRequestAt() + "'" +
            ", responseAt='" + getResponseAt() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", state=" + getState() +
            ", route=" + getRouteId() +
            "}";
    }
}
