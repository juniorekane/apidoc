package com.jek_dev.apidoc.services;


import com.jek_dev.apidoc.entities.ApiDoc;
import com.jek_dev.apidoc.entities.User;
import com.jek_dev.apidoc.enums.ApiDocsStatus;
import com.jek_dev.apidoc.repository.ApiDocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiDocService {

    private final ApiDocRepository apiDocRepository;

    public ApiDocService(ApiDocRepository apiDocRepository) {
        this.apiDocRepository = apiDocRepository;
    }

    /**
     * Reicht ein ApiDoc (Draft) zur Pruefung ein.
     * Entspricht der Methode submitForReview() im Klassendiagramm.
     */
    @Transactional
    public ApiDoc submitForReview(Long apiDocId) {
        ApiDoc apiDoc = apiDocRepository.findById(apiDocId)
                .orElseThrow(() -> new IllegalArgumentException("ApiDoc nicht gefunden: " + apiDocId));

        apiDoc.submitForReview();
        return apiDocRepository.save(apiDoc);
    }

    /**
     * Veroeffentlicht ein ApiDoc.
     * Falls es eine "original"-Referenz hat (also ein Draft einer bestehenden
     * Version ist), wird die alte Version automatisch auf DEPRECATED gesetzt.
     * Diese Koordination zwischen zwei ApiDoc-Objekten gehoert bewusst hier in
     * den Service und nicht in die Entity selbst.
     */
    @Transactional
    public ApiDoc publish(Long apiDocId) {
        ApiDoc apiDoc = apiDocRepository.findById(apiDocId)
                .orElseThrow(() -> new IllegalArgumentException("ApiDoc nicht gefunden: " + apiDocId));

        apiDoc.publish();

        ApiDoc original = apiDoc.getOriginal();
        if (original != null) {
            original.setStatus(ApiDocsStatus.DEPRECATED);
            // kein explizites save() noetig: original ist bereits vom
            // Persistence-Context verwaltet (Dirty Checking), wird aber
            // hier zur Klarheit trotzdem mitgespeichert
            apiDocRepository.save(original);
        }

        return apiDocRepository.save(apiDoc);
    }

    /**
     * Soft-Delete eines ApiDoc inkl. Erfassung, wer geloescht hat.
     * Kombiniert deletedBy (Service-Verantwortung, da User von aussen kommt)
     * mit markAsDeleted() (Entity-Verantwortung, reine Feldaenderung).
     */
    @Transactional
    public ApiDoc delete(Long apiDocId, User deletingUser) {
        ApiDoc apiDoc = apiDocRepository.findById(apiDocId)
                .orElseThrow(() -> new IllegalArgumentException("ApiDoc nicht gefunden: " + apiDocId));

        apiDoc.setDeletedBy(deletingUser);
        apiDoc.markAsDeleted();

        return apiDocRepository.save(apiDoc);
    }
}