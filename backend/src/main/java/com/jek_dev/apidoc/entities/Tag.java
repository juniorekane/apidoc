package com.jek_dev.apidoc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Repraesentiert ein Schlagwort, das zur Kategorisierung von {@link ApiDoc}-
 * Eintraegen verwendet wird.
 * Steht in einer n:m-Beziehung zu {@link ApiDoc} (siehe {@link ApiDoc#getTags()}
 * fuer die owning side dieser Beziehung).
 *
 * @author Danielle Matcheu
 */
@Entity
@Table(name = "tag")
@Getter
@Setter
    public class Tag {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        @ManyToMany(mappedBy = "tags")
        private Set<ApiDoc> apiDocs = new HashSet<>();
    }

