# devices and readings schema

# --- !Ups

CREATE TABLE device_types (
  id SERIAL PRIMARY KEY,
  name varchar(256) NOT NULL,
  version varchar(256) NOT NULL
);

CREATE TABLE devices (
  id SERIAL PRIMARY KEY,
  mac_addr char(12) NOT NULL UNIQUE,
  device_type_id integer NOT NULL,
  manufactured_at timestamp NOT NULL,
  registered_at timestamp,
  FOREIGN KEY (device_type_id) REFERENCES device_types (id)
);

CREATE TABLE readings (
  id SERIAL PRIMARY KEY,
  device_mac_addr char(12) NOT NULL,
  value text NOT NULL,
  created_at timestamp NOT NULL,
  FOREIGN KEY (device_mac_addr) REFERENCES devices (mac_addr)
);

# --- !Downs

DROP TABLE device_types;
DROP TABLE devices;
DROP TABLE readings;
