package com.ft.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the SmppRoutingInfo entity.
 */
public class SmppRoutingInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    @Size(max = 20)
    private String shortcode;

    private String keywords;

    @NotNull
    private Boolean status;

    private Long smppProfilesId;

    private String smppProfilesName;

    private Long httpProfilesId;

    private String httpProfilesName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getSmppProfilesId() {
        return smppProfilesId;
    }

    public void setSmppProfilesId(Long smppProfileId) {
        this.smppProfilesId = smppProfileId;
    }

    public String getSmppProfilesName() {
        return smppProfilesName;
    }

    public void setSmppProfilesName(String smppProfileName) {
        this.smppProfilesName = smppProfileName;
    }

    public Long getHttpProfilesId() {
        return httpProfilesId;
    }

    public void setHttpProfilesId(Long httpProfileId) {
        this.httpProfilesId = httpProfileId;
    }

    public String getHttpProfilesName() {
        return httpProfilesName;
    }

    public void setHttpProfilesName(String httpProfileName) {
        this.httpProfilesName = httpProfileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmppRoutingInfoDTO smppRoutingInfoDTO = (SmppRoutingInfoDTO) o;
        if (smppRoutingInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smppRoutingInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmppRoutingInfoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", shortcode='" + getShortcode() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", status='" + isStatus() + "'" +
            ", smppProfiles=" + getSmppProfilesId() +
            ", smppProfiles='" + getSmppProfilesName() + "'" +
            ", httpProfiles=" + getHttpProfilesId() +
            ", httpProfiles='" + getHttpProfilesName() + "'" +
            "}";
    }
}
