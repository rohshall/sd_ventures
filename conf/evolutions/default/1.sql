# devices and readings schema

# --- !Ups

CREATE TABLE device_types (
  id SERIAL PRIMARY KEY,
  dev_type varchar(256) NOT NULL
);

CREATE TABLE devices (
  id SERIAL PRIMARY KEY,
  device_type_id integer NOT NULL,
  registered_at date NOT NULL,
  FOREIGN KEY (device_type_id) REFERENCES device_types (id)
);

CREATE TABLE readings (
  id SERIAL PRIMARY KEY,
  value varchar(256) NOT NULL,
  created_at date NOT NULL,
  device_id integer NOT NULL,
  FOREIGN KEY (device_id) REFERENCES devices (id)
);

# --- !Downs

DROP TABLE device_types;
DROP TABLE devices;
DROP TABLE readings;
