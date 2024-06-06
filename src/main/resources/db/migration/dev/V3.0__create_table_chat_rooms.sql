RENAME TABLE gyms TO gym,
             members TO member;

CREATE TABLE chat_room
(
    id          SERIAL PRIMARY KEY,
    room_name   VARCHAR(31) NOT NULL,
    gym_id      BIGINT UNSIGNED NOT NULL,
    created_at  TIMESTAMP   NOT NULL,
    CONSTRAINT gym_id
    FOREIGN KEY (gym_id)
    REFERENCES gym (id) ON UPDATE CASCADE
);
