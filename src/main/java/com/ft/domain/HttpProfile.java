package com.ft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A HttpProfile.
 */
@Entity
@Table(name = "http_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HttpProfile implements Serializable {

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

    @Lob
    @Column(name = "configuration")
    private String configuration;

    @NotNull
    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @OneToMany(mappedBy = "httpProfiles")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SmppRoutingInfo> routes = new HashSet<>();
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

    public HttpProfile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public HttpProfile description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfiguration() {
        return configuration;
    }

    public HttpProfile configuration(String configuration) {
        this.configuration = configuration;
        return this;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public HttpProfile endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Set<SmppRoutingInfo> getRoutes() {
        return routes;
    }

    public HttpProfile routes(Set<SmppRoutingInfo> smppRoutingInfos) {
        this.routes = smppRoutingInfos;
        return this;
    }

    public HttpProfile addRoutes(SmppRoutingInfo smppRoutingInfo) {
        this.routes.add(smppRoutingInfo);
        smppRoutingInfo.setHttpProfiles(this);
        return this;
    }

    public HttpProfile removeRoutes(SmppRoutingInfo smppRoutingInfo) {
        this.routes.remove(smppRoutingInfo);
        smppRoutingInfo.setHttpProfiles(null);
        return this;
    }

    public void setRoutes(Set<SmppRoutingInfo> smppRoutingInfos) {
        this.routes = smppRoutingInfos;
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
        HttpProfile httpProfile = (HttpProfile) o;
        if (httpProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), httpProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HttpProfile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", endpoint='" + getEndpoint() + "'" +
            "}";
    }
}
