package com.jek_dev.apidoc.entities;

import com.jek_dev.apidoc.enums.ParameterLocation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
