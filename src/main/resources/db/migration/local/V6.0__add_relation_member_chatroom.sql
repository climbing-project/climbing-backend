ALTER TABLE chat_room
    ADD COLUMN member_id BIGINT UNSIGNED;

ALTER TABLE chat_room
    ADD CONSTRAINT FK_chat_room__member
        FOREIGN KEY (member_id)
            REFERENCES member (id) ON UPDATE CASCADE;
