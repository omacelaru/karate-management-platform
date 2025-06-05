-- Rename athlete_id to athlete_user_id in individual_category_participation
ALTER TABLE individual_category_participation
    RENAME COLUMN athlete_user_id TO athlete_id;

-- Rename the foreign key constraint
ALTER TABLE individual_category_participation
    RENAME CONSTRAINT fk_individual_category_participation_on_athlete_user TO fk_individual_category_participation_on_athlete;

-- Pentru individual_category_participation
ALTER TABLE individual_category_participation ALTER COLUMN id DROP IDENTITY IF EXISTS;
ALTER TABLE individual_category_participation ALTER COLUMN id SET DEFAULT nextval('category_participation_id_seq');

-- Pentru team_category_participation
ALTER TABLE team_category_participation ALTER COLUMN id DROP IDENTITY IF EXISTS;
ALTER TABLE team_category_participation ALTER COLUMN id SET DEFAULT nextval('category_participation_id_seq');

-- (Opțional) Șterge secvențele vechi
DROP SEQUENCE IF EXISTS individual_category_participation_id_seq;
DROP SEQUENCE IF EXISTS team_category_participation_id_seq;


