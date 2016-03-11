# Add User

# --- !Ups
CREATE TABLE USER
(ID IDENTITY NOT NULL,
FIRST_NAME VARCHAR(512) NOT NULL,
LAST_NAME VARCHAR(512) NOT NULL,
EMAIL VARCHAR(512) NOT NULL,
ROLE VARCHAR(128) NOT NULL,
PASSWORD VARCHAR(512) NOT NULL,
PRIMARY KEY (ID));

# --- !Downs
DROP TABLE USER;