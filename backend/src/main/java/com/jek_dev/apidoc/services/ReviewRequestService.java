package com.jek_dev.apidoc.services;

import com.jek_dev.apidoc.entities.ApiDoc;
import com.jek_dev.apidoc.entities.ReviewRequest;
import com.jek_dev.apidoc.entities.User;
import com.jek_dev.apidoc.enums.ApiDocsStatus;
import com.jek_dev.apidoc.enums.ReviewRequestType;
import com.jek_dev.apidoc.repository.ReviewRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewRequestService {

    private final ReviewRequestRepository reviewRequestRepository;

    public ReviewRequestService(ReviewRequestRepository reviewRequestRepository) {
        this.reviewRequestRepository = reviewRequestRepository;
    }

    /**
     * Legt einen neuen ReviewRequest fuer ein ApiDoc an.
     */
    @Transactional
    public ReviewRequest create(ApiDoc apiDoc, User requestedBy, ReviewRequestType type) {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setApiDoc(apiDoc);
        reviewRequest.setRequestedBy(requestedBy);
        reviewRequest.setType(type);

        return reviewRequestRepository.save(reviewRequest);
    }

    /**
     * Akzeptiert einen ReviewRequest.
     * Setzt reviewedBy, da das (wie deletedBy bei ApiDoc) von aussen kommt
     * und daher Service-Verantwortung ist, nicht Entity-Verantwortung.
     * Das eigentliche Veroeffentlichen des ApiDoc passiert bewusst NICHT hier,
     * sondern separat ueber ApiDocService.publish() - Trennung der Aggregate.
     */
    @Transactional
    public ReviewRequest accept(Long reviewRequestId, User reviewedBy) {
        ReviewRequest reviewRequest = reviewRequestRepository.findById(reviewRequestId)
                .orElseThrow(() -> new IllegalArgumentException("ReviewRequest nicht gefunden: " + reviewRequestId));

        reviewRequest.setReviewedBy(reviewedBy);
        reviewRequest.accept();

        return reviewRequestRepository.save(reviewRequest);
    }

    /**
     * Lehnt einen ReviewRequest ab.
     * Koordiniert zwei Objekte (ReviewRequest + zugehoeriges ApiDoc), daher
     * bewusst im Service statt in einer der beiden Entities.
     */
    @Transactional
    public ReviewRequest reject(Long reviewRequestId, User reviewedBy, String reason) {
        ReviewRequest reviewRequest = reviewRequestRepository.findById(reviewRequestId)
                .orElseThrow(() -> new IllegalArgumentException("ReviewRequest nicht gefunden: " + reviewRequestId));

        reviewRequest.setReviewedBy(reviewedBy);
        reviewRequest.reject(reason);

        reviewRequest.getApiDoc().setStatus(ApiDocsStatus.REJECTED);

        return reviewRequestRepository.save(reviewRequest);
    }
}
