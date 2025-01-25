ALTER TABLE competition_categories
    DROP CONSTRAINT fk_comcat_on_category;

ALTER TABLE matches
    DROP CONSTRAINT fk_matches_on_category;

CREATE SEQUENCE IF NOT EXISTS category_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE kata_category
(
    id                 BIGINT                      NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    age_group          VARCHAR(255)                NOT NULL,
    gender             VARCHAR(255)                NOT NULL,
    is_default         BOOLEAN                     NOT NULL,
    kata_category_type VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_katacategory PRIMARY KEY (id)
);

CREATE TABLE kumite_category
(
    id         BIGINT                      NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    age_group  VARCHAR(255)                NOT NULL,
    gender     VARCHAR(255)                NOT NULL,
    is_default BOOLEAN                     NOT NULL,
    weight_min SMALLINT                    NOT NULL,
    weight_max SMALLINT                    NOT NULL,
    CONSTRAINT pk_kumitecategory PRIMARY KEY (id)
);

ALTER TABLE categories
    DROP COLUMN discipline_type;

ALTER TABLE categories
    DROP COLUMN kata_category_type;

ALTER TABLE categories
    DROP COLUMN weight_max;

ALTER TABLE categories
    DROP COLUMN weight_min;