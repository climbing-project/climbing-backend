CREATE TABLE bookmark
(
    id         SERIAL PRIMARY KEY,
    member_id  BIGINT UNSIGNED NOT NULL,
    gym_id     BIGINT UNSIGNED NOT NULL,
    status     BOOLEAN         NOT NULL,
    created_at TIMESTAMP       NOT NULL,
    CONSTRAINT FK_bookmark_member
        FOREIGN KEY (member_id)
            REFERENCES member (id) ON UPDATE CASCADE,
    CONSTRAINT FK_bookmark_gym
        FOREIGN KEY (gym_id)
            REFERENCES gym (id) ON UPDATE CASCADE
);
