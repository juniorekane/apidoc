package com.jek_dev.apidoc.entities;

import com.jek_dev.apidoc.enums.EndpointStatus;
import com.jek_dev.apidoc.enums.HttpMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repraesentiert einen einzelnen API-Endpunkt innerhalb eines {@link ApiDoc}
 * Ein {@code Endpoint} gehoert fest zu genau einem {@link ApiDoc}  und kann mehrere
 * {@link Parameter} besitzen. Wie {@link ApiDoc} unterstuetzt auch {@code Endpoint} das
 * Versionierungs-Pattern ueber {@link #getOriginal()} sowie eine optionale Verknuepfung
 * zum ausloesenden {@link ReviewRequest}.
 *
 * @author Danielle Matcheu
 */
    @Entity
    @Table(name = "endpoint")
    @Getter
    @Setter
    public class Endpoint {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String path;

        @Enumerated(EnumType.STRING)
        @Column(name = "http_method", nullable = false)
        private HttpMethod httpMethod;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private EndpointStatus status;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "api_doc_id", nullable = false)
        private ApiDoc apiDoc;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "original_id")
        private Endpoint original;

        @OneToMany(mappedBy = "original")
        private List<Endpoint> drafts = new ArrayList<>();

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "review_request_id")
        private ReviewRequest reviewRequest;

        @OneToMany(mappedBy = "endpoint", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Parameter> parameters = new ArrayList<>();

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
            if (this.status == null) {
                this.status = EndpointStatus.ACTIVE;
            }
        }

        @PreUpdate
        protected void onUpdate() {
            this.updatedAt = LocalDateTime.now();
        }
    }

