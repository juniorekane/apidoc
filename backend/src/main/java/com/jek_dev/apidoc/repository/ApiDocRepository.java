package com.jek_dev.apidoc.repository;


import com.jek_dev.apidoc.entities.ApiDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiDocRepository extends JpaRepository<ApiDoc, Long> {

    List<ApiDoc> findByDeletedFalse();
}
