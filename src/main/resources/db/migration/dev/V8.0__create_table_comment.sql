CREATE TABLE comment
(
    id         SERIAL PRIMARY KEY,
    member_id  BIGINT UNSIGNED NOT NULL,
    gym_id     BIGINT UNSIGNED NOT NULL,
    text       VARCHAR(256)    NOT NULL,
    created_at TIMESTAMP       NOT NULL,
    CONSTRAINT FK_comment__member
        FOREIGN KEY (member_id)
            REFERENCES member (id) ON UPDATE CASCADE,
    CONSTRAINT FK_comment__gym
        FOREIGN KEY (gym_id)
            REFERENCES gym (id) ON UPDATE CASCADE
);
