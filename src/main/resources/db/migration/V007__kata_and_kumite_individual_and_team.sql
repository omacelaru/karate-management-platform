CREATE TABLE kata_individual_category
(
    id              BIGINT                      NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    age_group       VARCHAR(255)                NOT NULL,
    gender          VARCHAR(255)                NOT NULL,
    category_type   VARCHAR(30)                 NOT NULL,
    is_default      BOOLEAN                     NOT NULL,
    kata_belt_range VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_kataindividualcategory PRIMARY KEY (id)
);

CREATE TABLE kata_team_category
(
    id             BIGINT                      NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    age_group      VARCHAR(255)                NOT NULL,
    gender         VARCHAR(255)                NOT NULL,
    category_type  VARCHAR(30)                 NOT NULL,
    is_default     BOOLEAN                     NOT NULL,
    kata_team_type VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_katateamcategory PRIMARY KEY (id)
);

CREATE TABLE kumite_individual_category
(
    id                    BIGINT                      NOT NULL,
    created_at            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    age_group             VARCHAR(255)                NOT NULL,
    gender                VARCHAR(255)                NOT NULL,
    category_type         VARCHAR(30)                 NOT NULL,
    is_default            BOOLEAN                     NOT NULL,
    kumite_division_range VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_kumiteindividualcategory PRIMARY KEY (id)
);

CREATE TABLE kumite_team_category
(
    id               BIGINT                      NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    age_group        VARCHAR(255)                NOT NULL,
    gender           VARCHAR(255)                NOT NULL,
    category_type    VARCHAR(30)                 NOT NULL,
    is_default       BOOLEAN                     NOT NULL,
    kumite_team_type VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_kumiteteamcategory PRIMARY KEY (id)
);

DROP TABLE kata_category CASCADE;

DROP TABLE kumite_category CASCADE;