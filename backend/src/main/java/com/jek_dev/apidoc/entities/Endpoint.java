package com.jek_dev.apidoc.entities;

import com.jek_dev.apidoc.enums.EndpointStatus;
import com.jek_dev.apidoc.enums.HttpMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

