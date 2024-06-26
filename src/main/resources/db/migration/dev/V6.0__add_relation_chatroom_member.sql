ALTER TABLE chat_room
    ADD COLUMN member_id BIGINT UNSIGNED;

ALTER TABLE chat_room
    ADD CONSTRAINT FK_chat_room__member
        FOREIGN KEY (member_id)
            REFERENCES member (id) ON UPDATE CASCADE;

UPDATE chat_room c
    INNER JOIN member m
    ON c.room_name = m.nickname
SET c.member_id = m.id;

ALTER TABLE chat_room
    MODIFY member_id BIGINT UNSIGNED NOT NULL;

ALTER TABLE chat_room
    DROP COLUMN room_name;
