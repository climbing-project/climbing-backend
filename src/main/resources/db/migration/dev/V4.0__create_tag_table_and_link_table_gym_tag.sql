CREATE TABLE tag
(
    id    SERIAL PRIMARY KEY,
    VALUE VARCHAR(15) NOT NULL
);

CREATE TABLE gym_tag
(
    id     SERIAL PRIMARY KEY,
    gym_id BIGINT UNSIGNED NOT NULL,
    tag_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT FK_gym_tag__gym
        FOREIGN KEY (gym_id)
            REFERENCES gym (id) ON UPDATE CASCADE,
    CONSTRAINT FK_gym_tag__tag
        FOREIGN KEY (tag_id)
            REFERENCES tag (id) ON UPDATE CASCADE
);
