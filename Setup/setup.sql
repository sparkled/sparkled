-- DDL.
DROP SCHEMA xmas CASCADE;
CREATE SCHEMA xmas;

CREATE TABLE xmas.animation_effect (
  code VARCHAR(64) PRIMARY KEY NOT NULL,
  name VARCHAR(64) UNIQUE NOT NULL,
  image_path VARCHAR(128) NOT NULL
);

CREATE TABLE xmas.animation_effect_param_type (
  code VARCHAR(64) PRIMARY KEY NOT NULL
);

CREATE TABLE xmas.animation_effect_param (
  code VARCHAR(64) PRIMARY KEY NOT NULL,
  name VARCHAR(64) UNIQUE NOT NULL,
  type_code VARCHAR(64) NOT NULL REFERENCES xmas.animation_effect_param_type (code)
);

CREATE TABLE xmas.animation_effect_param_map (
  animation_effect_code VARCHAR(64) NOT NULL REFERENCES xmas.animation_effect (code),
  animation_effect_param_code VARCHAR(64) NOT NULL REFERENCES xmas.animation_effect_param (code),
  PRIMARY KEY(animation_effect_code, animation_effect_param_code)
);

CREATE TABLE xmas.song (
  id SERIAL PRIMARY KEY,
  album VARCHAR(64) NOT NULL,
  animation_data TEXT,
  artist VARCHAR(64) NOT NULL,
  duration_seconds INTEGER NOT NULL,
name VARCHAR(64) NOT NULL
);

CREATE TABLE xmas.song_data (
  song_id INTEGER PRIMARY KEY NOT NULL,
  mp3_data BYTEA NOT NULL
);


-- Data insert.
INSERT INTO xmas.animation_effect (code, name, image_path) VALUES
  ('LINE_LEFT', 'Line (Left)', 'a'),
  ('LINE_RIGHT', 'Line (Right)', 'b');

INSERT INTO xmas.animation_effect_param_type (code) VALUES
  ('COLOUR'),
  ('MULTI_COLOUR');

INSERT INTO xmas.animation_effect_param (code, name, type_code) VALUES
  ('COLOUR', 'Colour', 'COLOUR'),
  ('COLOURS', 'Colours', 'MULTI_COLOUR');

INSERT INTO xmas.animation_effect_param_map (animation_effect_code, animation_effect_param_code) VALUES
  ('LINE_LEFT', 'COLOURS'),
  ('LINE_RIGHT', 'COLOURS');