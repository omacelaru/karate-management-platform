ALTER TABLE competitions
    ADD COLUMN registration_open BOOLEAN NOT NULL DEFAULT true;

-- Update existing competitions based on date
UPDATE competitions
SET registration_open = false
WHERE date <  now();