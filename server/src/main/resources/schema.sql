--DROP SCHEMA public CASCADE;
-- CREATE SCHEMA public;
-- DROP TABLE users;
-- DROP TABLE requests;
-- DROP TABLE items;
-- DROP TABLE bookings;


CREATE TYPE booking_state as enum ('WAITING', 'APPROVED', 'REJECTED','CANCELED');


CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    512
) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY
(
    id
),
    CONSTRAINT UQ_USER_EMAIL UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    description
    VARCHAR
(
    255
) NOT NULL,
    requestor_id INTEGER NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY
(
    id
),
    FOREIGN KEY
(
    requestor_id
) REFERENCES users
(
    Id
)

    );


CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    512
) NOT NULL,
    request VARCHAR
(
    512
) NOT NULL,
    is_available BOOLEAN,
    owner_id INTEGER,
    request_id INTEGER,
    CONSTRAINT pk_item PRIMARY KEY
(
    id
),
    FOREIGN KEY
(
    owner_id
) REFERENCES users
(
    Id
),
    FOREIGN KEY
(
    request_id
) REFERENCES requests
(
    Id
)

    );


CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    start_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    end_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    item_id
    INTEGER
    NOT
    NULL,
    booker_id
    INTEGER
    NOT
    NULL,
    status
    booking_state,
    CONSTRAINT
    pk_booking
    PRIMARY
    KEY
(
    id
),
    FOREIGN KEY
(
    item_id
) REFERENCES items
(
    Id
),
    FOREIGN KEY
(
    booker_id
) REFERENCES bookings
(
    Id
)

    );













