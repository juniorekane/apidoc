package com.jek_dev.apidoc.entities;

import com.jek_dev.apidoc.enums.ReviewRequestStatus;
import com.jek_dev.apidoc.enums.ReviewRequestType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_request")
@Getter
@Setter
public class ReviewRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_doc_id", nullable = false)
    private ApiDoc apiDoc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewRequestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewRequestStatus status;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ReviewRequestStatus.PENDING;
        }
    }


    public void accept() {
        this.status = ReviewRequestStatus.ACCEPTED;
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        this.status = ReviewRequestStatus.REJECTED;
        this.rejectionReason = reason;
        this.reviewedAt = LocalDateTime.now();
    }
}

