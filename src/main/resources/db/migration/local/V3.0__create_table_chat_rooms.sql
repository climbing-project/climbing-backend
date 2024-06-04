CREATE TABLE chat_room
(
    id           SERIAL PRIMARY KEY,
    room_name    VARCHAR(31) NOT NULL,
    gym_id       VARCHAR(15) NOT NULL,
    created_at TIMESTAMP   NOT NULL
);
