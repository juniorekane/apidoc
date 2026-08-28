package com.jek_dev.apidoc.repository;

import com.jek_dev.apidoc.entities.ReviewRequest;
import com.jek_dev.apidoc.enums.ReviewRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, Long> {

    List<ReviewRequest> findByStatus(ReviewRequestStatus status);

    List<ReviewRequest> findByApiDocId(Long apiDocId);
}