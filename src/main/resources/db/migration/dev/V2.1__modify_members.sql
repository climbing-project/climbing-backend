ALTER TABLE members MODIFY COLUMN password VARCHAR(127);
ALTER TABLE members MODIFY COLUMN nickname VARCHAR(31) UNIQUE;