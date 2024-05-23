CREATE TABLE gyms
(
    id          SERIAL PRIMARY KEY,
    name    VARCHAR(63) NOT NULL,
    jibun_address   VARCHAR(63) NOT NULL,
    road_address    VARCHAR(63) NOT NULL,
    unit_address    VARCHAR(63) NOT NULL,
    latitude        NUMERIC(8, 6) NOT NULL,
    longitude       NUMERIC(9, 6) NOT NULL,
    description     TEXT,
    latest_setting_date TIMESTAMP,
    twitter VARCHAR(31),
    facebook VARCHAR(31),
    instagram VARCHAR(31),
    homepage VARCHAR(63),
    images VARCHAR(1023),
    tags VARCHAR(255),
    pricing TEXT,
    open_hours TEXT,
    accommodations TEXT,
    contact VARCHAR(15),
    grades VARCHAR(127),
    comments TEXT,
    likes INTEGER DEFAULT 0,
    hits INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

create table members
(
    id          SERIAL PRIMARY KEY,
    email       VARCHAR(63)  NOT NULL UNIQUE,
    password    VARCHAR(127) NOT NULL,
    nickname    VARCHAR(31)  NOT NULL UNIQUE,
    role        VARCHAR(15)  NOT NULL,
    social_type VARCHAR(15),
    social_id   VARCHAR(63),
    is_blocked  TINYINT
--     created_at  TIMESTAMP    NOT NULL,
--     modified_at TIMESTAMP    NOT NULL
);
