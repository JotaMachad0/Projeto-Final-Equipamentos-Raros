ALTER TABLE expected_hirings
    ADD COLUMN status VARCHAR(10) NOT NULL DEFAULT 'Criada';
ALTER TABLE expected_hirings
ALTER
COLUMN region TYPE VARCHAR(12);