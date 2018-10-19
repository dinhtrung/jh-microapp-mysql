package com.ft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SmppRoutingInfo.
 */
@Entity
@Table(name = "smpp_routing_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmppRoutingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Size(max = 20)
    @Column(name = "shortcode", length = 20, nullable = false)
    private String shortcode;

    @Column(name = "keywords")
    private String keywords;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne
    @JsonIgnoreProperties("routes")
    private SmppProfile smppProfiles;

    @ManyToOne
    @JsonIgnoreProperties("routes")
    private HttpProfile httpProfiles;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SmppRoutingInfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SmppRoutingInfo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortcode() {
        return shortcode;
    }

    public SmppRoutingInfo shortcode(String shortcode) {
        this.shortcode = shortcode;
        return this;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getKeywords() {
        return keywords;
    }

    public SmppRoutingInfo keywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean isStatus() {
        return status;
    }

    public SmppRoutingInfo status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SmppProfile getSmppProfiles() {
        return smppProfiles;
    }

    public SmppRoutingInfo smppProfiles(SmppProfile smppProfile) {
        this.smppProfiles = smppProfile;
        return this;
    }

    public void setSmppProfiles(SmppProfile smppProfile) {
        this.smppProfiles = smppProfile;
    }

    public HttpProfile getHttpProfiles() {
        return httpProfiles;
    }

    public SmppRoutingInfo httpProfiles(HttpProfile httpProfile) {
        this.httpProfiles = httpProfile;
        return this;
    }

    public void setHttpProfiles(HttpProfile httpProfile) {
        this.httpProfiles = httpProfile;
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
        SmppRoutingInfo smppRoutingInfo = (SmppRoutingInfo) o;
        if (smppRoutingInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smppRoutingInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmppRoutingInfo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", shortcode='" + getShortcode() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
