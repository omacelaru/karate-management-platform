-- Drop old tables if they exist (order matters due to FKs)
DROP TABLE IF EXISTS kata_team_category CASCADE;
DROP TABLE IF EXISTS kumite_team_category CASCADE;
DROP TABLE IF EXISTS team_category CASCADE;
DROP TABLE IF EXISTS kata_individual_category CASCADE;
DROP TABLE IF EXISTS kumite_individual_category CASCADE;
DROP TABLE IF EXISTS individual_category CASCADE;
DROP TABLE IF EXISTS category CASCADE;
CREATE SEQUENCE IF NOT EXISTS category_sequence START WITH 1 INCREMENT BY 1;

-- Create the main category table
CREATE TABLE category (
                          id BIGINT NOT NULL PRIMARY KEY,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          age_group VARCHAR(50) NOT NULL,
                          gender VARCHAR(20) NOT NULL,
                          is_default BOOLEAN NOT NULL
);

-- IndividualCategory (abstract, but needed for JOINED)
CREATE TABLE individual_category (
    id BIGINT NOT NULL PRIMARY KEY,
    category_type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_individualcategory_on_category FOREIGN KEY (id) REFERENCES category(id)
);

-- KataIndividualCategory
CREATE TABLE kata_individual_category (
    id BIGINT NOT NULL PRIMARY KEY,
    kata_belt_range VARCHAR(50) NOT NULL,
    CONSTRAINT fk_kataindividualcategory_on_individualcategory FOREIGN KEY (id) REFERENCES individual_category(id)
);

-- KumiteIndividualCategory
CREATE TABLE kumite_individual_category (
    id BIGINT NOT NULL PRIMARY KEY,
    kumite_division_range VARCHAR(50) NOT NULL,
    CONSTRAINT fk_kumiteindividualcategory_on_individualcategory FOREIGN KEY (id) REFERENCES individual_category(id)
);

-- TeamCategory (abstract, but needed for JOINED)
CREATE TABLE team_category (
    id BIGINT NOT NULL PRIMARY KEY,
    category_type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_teamcategory_on_category FOREIGN KEY (id) REFERENCES category(id)
);

-- KataTeamCategory
CREATE TABLE kata_team_category (
    id BIGINT NOT NULL PRIMARY KEY,
    kata_team_type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_katateamcategory_on_teamcategory FOREIGN KEY (id) REFERENCES team_category(id)
);

-- KumiteTeamCategory
CREATE TABLE kumite_team_category (
    id BIGINT NOT NULL PRIMARY KEY,
    kumite_team_type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_kumiteteamcategory_on_teamcategory FOREIGN KEY (id) REFERENCES team_category(id)
); 