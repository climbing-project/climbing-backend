UPDATE chat_room c
    INNER JOIN member m
    ON c.room_name = m.nickname
SET c.member_id = m.id;
