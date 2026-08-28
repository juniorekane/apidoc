package com.jek_dev.apidoc.repository;

import com.jek_dev.apidoc.entities.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

    List<Endpoint> findByApiDocId(Long apiDocId);
}
