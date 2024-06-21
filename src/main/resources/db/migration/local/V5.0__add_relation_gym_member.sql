ALTER TABLE gym
    ADD COLUMN member_id BIGINT UNSIGNED NOT NULL;

ALTER TABLE gym
    ADD CONSTRAINT FK_gym__member
        FOREIGN KEY (member_id)
            REFERENCES member (id) ON UPDATE CASCADE;
