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
 * A SmppProfile.
 */
@Entity
@Table(name = "smpp_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmppProfile implements Serializable {

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

    @OneToMany(mappedBy = "smppProfiles")
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

    public SmppProfile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SmppProfile description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfiguration() {
        return configuration;
    }

    public SmppProfile configuration(String configuration) {
        this.configuration = configuration;
        return this;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Set<SmppRoutingInfo> getRoutes() {
        return routes;
    }

    public SmppProfile routes(Set<SmppRoutingInfo> smppRoutingInfos) {
        this.routes = smppRoutingInfos;
        return this;
    }

    public SmppProfile addRoutes(SmppRoutingInfo smppRoutingInfo) {
        this.routes.add(smppRoutingInfo);
        smppRoutingInfo.setSmppProfiles(this);
        return this;
    }

    public SmppProfile removeRoutes(SmppRoutingInfo smppRoutingInfo) {
        this.routes.remove(smppRoutingInfo);
        smppRoutingInfo.setSmppProfiles(null);
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
        SmppProfile smppProfile = (SmppProfile) o;
        if (smppProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smppProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmppProfile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            "}";
    }
}
