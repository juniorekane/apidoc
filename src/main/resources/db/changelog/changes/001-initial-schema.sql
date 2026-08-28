--liquibase formatted sql

-- Initiales Schema fuer die API-Documentation-Plattform.
-- Reihenfolge beachtet Fremdschluessel-Abhaengigkeiten:
-- user -> api_doc -> tag -> review_request -> endpoint -> parameter.

--changeset danielle:001-create-user
CREATE TABLE user (
                      id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username      VARCHAR(255) NOT NULL UNIQUE,
                      email         VARCHAR(255) NOT NULL UNIQUE,
                      password_hash VARCHAR(255) NOT NULL,
                      role          VARCHAR(20)  NOT NULL,
                      created_at    DATETIME     NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE user;

--changeset danielle:002-create-api_doc
CREATE TABLE api_doc (
                         id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title        VARCHAR(255) NOT NULL,
                         description  TEXT,
                         base_url     VARCHAR(255),
                         status       VARCHAR(20)  NOT NULL,
                         deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
                         deleted_at   DATETIME,
                         created_at   DATETIME     NOT NULL,
                         updated_at   DATETIME,
                         created_by   BIGINT,
                         updated_by   BIGINT,
                         deleted_by   BIGINT,
                         original_id  BIGINT,
                         CONSTRAINT fk_api_doc_created_by  FOREIGN KEY (created_by)  REFERENCES user(id),
                         CONSTRAINT fk_api_doc_updated_by  FOREIGN KEY (updated_by)  REFERENCES user(id),
                         CONSTRAINT fk_api_doc_deleted_by  FOREIGN KEY (deleted_by)  REFERENCES user(id),
                         CONSTRAINT fk_api_doc_original    FOREIGN KEY (original_id) REFERENCES api_doc(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE api_doc;

--changeset danielle:003-create-tag
CREATE TABLE tag (
                     id   BIGINT AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE tag;

--changeset danielle:004-create-api_doc_tag
CREATE TABLE api_doc_tag (
                             api_doc_id BIGINT NOT NULL,
                             tag_id     BIGINT NOT NULL,
                             PRIMARY KEY (api_doc_id, tag_id),
                             CONSTRAINT fk_api_doc_tag_doc FOREIGN KEY (api_doc_id) REFERENCES api_doc(id) ON DELETE CASCADE,
                             CONSTRAINT fk_api_doc_tag_tag FOREIGN KEY (tag_id)     REFERENCES tag(id)     ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE api_doc_tag;

--changeset danielle:005-create-user_favorite
CREATE TABLE user_favorite (
                               user_id    BIGINT NOT NULL,
                               api_doc_id BIGINT NOT NULL,
                               PRIMARY KEY (user_id, api_doc_id),
                               CONSTRAINT fk_user_favorite_user   FOREIGN KEY (user_id)    REFERENCES user(id)    ON DELETE CASCADE,
                               CONSTRAINT fk_user_favorite_apidoc FOREIGN KEY (api_doc_id) REFERENCES api_doc(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE user_favorite;

--changeset danielle:006-create-review_request
-- muss vor endpoint/parameter stehen, da beide per FK darauf verweisen
CREATE TABLE review_request (
                                id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                                api_doc_id       BIGINT       NOT NULL,
                                type             VARCHAR(20)  NOT NULL,
                                status           VARCHAR(20)  NOT NULL,
                                rejection_reason TEXT,
                                requested_by     BIGINT       NOT NULL,
                                reviewed_by      BIGINT,
                                created_at       DATETIME     NOT NULL,
                                reviewed_at      DATETIME,
                                CONSTRAINT fk_review_request_apidoc       FOREIGN KEY (api_doc_id)   REFERENCES api_doc(id),
                                CONSTRAINT fk_review_request_requested_by FOREIGN KEY (requested_by) REFERENCES user(id),
                                CONSTRAINT fk_review_request_reviewed_by  FOREIGN KEY (reviewed_by)  REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE review_request;

--changeset danielle:007-create-endpoint
CREATE TABLE endpoint (
                          id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                          api_doc_id        BIGINT       NOT NULL,
                          path              VARCHAR(255) NOT NULL,
                          http_method       VARCHAR(10)  NOT NULL,
                          description       TEXT,
                          status            VARCHAR(20)  NOT NULL,
                          created_at        DATETIME     NOT NULL,
                          updated_at        DATETIME,
                          original_id       BIGINT,
                          review_request_id BIGINT,
                          CONSTRAINT fk_endpoint_apidoc         FOREIGN KEY (api_doc_id)        REFERENCES api_doc(id) ON DELETE CASCADE,
                          CONSTRAINT fk_endpoint_original       FOREIGN KEY (original_id)       REFERENCES endpoint(id),
                          CONSTRAINT fk_endpoint_review_request FOREIGN KEY (review_request_id) REFERENCES review_request(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE endpoint;

--changeset danielle:008-create-parameter
CREATE TABLE parameter (
                           id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                           endpoint_id       BIGINT       NOT NULL,
                           name              VARCHAR(255) NOT NULL,
                           type              VARCHAR(50)  NOT NULL,
                           location          VARCHAR(20)  NOT NULL,
                           required          BOOLEAN      NOT NULL,
                           description       TEXT,
                           original_id       BIGINT,
                           review_request_id BIGINT,
                           CONSTRAINT fk_parameter_endpoint       FOREIGN KEY (endpoint_id)       REFERENCES endpoint(id) ON DELETE CASCADE,
                           CONSTRAINT fk_parameter_original       FOREIGN KEY (original_id)       REFERENCES parameter(id),
                           CONSTRAINT fk_parameter_review_request FOREIGN KEY (review_request_id) REFERENCES review_request(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--rollback DROP TABLE parameter;