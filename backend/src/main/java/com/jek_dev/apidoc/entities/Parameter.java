package com.jek_dev.apidoc.entities;

import com.jek_dev.apidoc.enums.ParameterLocation;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Repraesentiert einen einzelnen Parameter einen {@link Endpoint}
 * Ein {@code Parameter} gehoert fest zu genau einem {@link #getLocation()}
 * gibt an, wo der Parameter in der HTT-Anfrage erwartet wird (siehe {@link ParameterLocation}).
 * Wie {@link Endpoint} unterstuetzt auch {@code Parameter} das VersionierungsPattern
 * ueber {@link #getOriginal()}.
 *
 * @author Danielle Matcheu
 */
@Entity
@Table(name = "parameter")
@Getter
@Setter
public class Parameter {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ParameterLocation location;

        @Column(nullable = false)
        private boolean required;

        @Column(columnDefinition = "TEXT")
        private String description;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "endpoint_id", nullable = false)
        private Endpoint endpoint;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "original_id")
        private Parameter original;

        @OneToMany(mappedBy = "original")
        private List<Parameter> drafts = new ArrayList<>();

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "review_request_id")
        private ReviewRequest reviewRequest;

}
