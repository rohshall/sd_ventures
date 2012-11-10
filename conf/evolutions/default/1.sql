# devices and readings schema

# --- !Ups

CREATE TABLE devices (
        id SERIAL PRIMARY KEY,
        title varchar(256) NOT NULL
);

CREATE TABLE readings (
        id SERIAL PRIMARY KEY,
        value varchar(256) NOT NULL,
        created_at date NOT NULL,
        device_id integer NOT NULL,
        FOREIGN KEY (device_id) REFERENCES devices (id)
);

# --- !Downs

DROP TABLE devices;
DROP TABLE readings;
