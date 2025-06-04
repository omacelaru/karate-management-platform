CREATE TABLE category_scheduling
(
    id             BIGSERIAL PRIMARY KEY,
    category_id    BIGINT                      NOT NULL,
    competition_id BIGINT                      NOT NULL,
    tatami_id      INTEGER                     NOT NULL,
    start_time     TIME                        NOT NULL,
    end_time       TIME                        NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (competition_id) REFERENCES competitions (id)
);