
CREATE TABLE metadata_tag (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id  bigint(20) NOT NULL,
  description VARCHAR(255) DEFAULT NOT NULL,
  CONSTRAINT mm9iNgvUl8rwUq9 FOREIGN KEY (user_id) REFERENCES app_user (id),
);

CREATE TABLE document_metadata (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  document_key varchar(255) DEFAULT NOT NULL,
  metadata_tag_id bigint(20) not null,
  value NVARCHAR(500) DEFAULT NOT NULL,
  CONSTRAINT hT6guf6d4Ad48np FOREIGN KEY (metadata_tag_id) REFERENCES metadata_tag (id)
);

CREATE UNIQUE INDEX IDXhT6guer44Ad48np ON document_metadata
(
  document_key, metadata_tag_id
);

CREATE TABLE app_role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  role_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE app_user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  username varchar(255) NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE user_role (
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  CONSTRAINT k0JbjEQf7J3VL34 FOREIGN KEY (user_id) REFERENCES app_user (id),
  CONSTRAINT fPZFAHKRl0ELpek FOREIGN KEY (role_id) REFERENCES app_role (id)
);