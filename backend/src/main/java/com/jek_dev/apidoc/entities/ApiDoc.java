package com.jek_dev.apidoc.entities;


import com.jek_dev.apidoc.enums.ApiDocsStatus;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Repraesentiert eine API-Dokumentation innerhalb der Plattform.
 * Ein {@code ApiDoc} durchlaeuft einen Review-workflow ueber die Status
 * {@link ApiDocsStatus}. Aenderungen an einer bestehenden, veroeffentlichen
 * Version werden als neuer Draft mit Referenz auf das {@code original} angelegt
 * (siehe {@link #getOriginal()}).
 * Unterstuetzt Soft-Delete ueber {@link #isDeleted()} und {@link #getDeletedAt()}
 * statt physischem Loeschem aus der Datenbank.
 *
 * @author Danielle Matcheu
 */
@Entity
@Table(name = "api_doc")
@Getter
@Setter
public class ApiDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_url")
    private String baseUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApiDocsStatus status;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_id")
    private ApiDoc original;

    @OneToMany(mappedBy = "original")
    private List<ApiDoc> drafts = new ArrayList<>();

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endpoint> endpoints = new ArrayList<>();

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewRequest> reviewRequests = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "api_doc_tag",
            joinColumns = @JoinColumn(name = "api_doc_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ApiDocsStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void submitForReview() {
        this.status = ApiDocsStatus.IN_REVIEW;
    }

    public void publish() {
        this.status = ApiDocsStatus.PUBLISHED;
    }
}