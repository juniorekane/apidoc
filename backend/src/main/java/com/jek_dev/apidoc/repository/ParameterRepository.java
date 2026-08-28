package com.jek_dev.apidoc.repository;


import com.jek_dev.apidoc.entities.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> findByEndpointId(Long endpointId);
}