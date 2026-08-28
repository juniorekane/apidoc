package com.jek_dev.apidoc.entities;


import com.jek_dev.apidoc.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Repraesentiert einen Benutzer der API-Dokumentations-Plattform.
 * Ein {@code User} kann {@link ApiDoc}-Eintraege erstellen, aktualisieren
 * und loeschen (siehe {@link ApiDoc#getCreatedBy()}, {@link ApiDoc#getUpdatedBy()},
 * {@link ApiDoc#getDeletedBy()}, sowie {@link ReviewRequest}s stellen und pruefen.
 * Die {@link Role} steuert die Berechtigungen innerhalb der Plattform.
 *
 * @author Danielle Matcheu
 */
@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "user_favorite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "api_doc_id")
    )
    private Set<ApiDoc> favorites = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}