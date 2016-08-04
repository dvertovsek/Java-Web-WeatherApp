DROP TABLE dvertovs_dnevnik;
drop table dvertovs_users;

CREATE TABLE dvertovs_dnevnik (
iduser integer NOT NULL 
                PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
                (START WITH 1, INCREMENT BY 1),
  korisnik varchar(25) NOT NULL DEFAULT '',
  akcija varchar(255) NOT NULL DEFAULT '',
  vrijeme timestamp
);

CREATE TABLE dvertovs_users (
    iduser integer NOT NULL 
                PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
                (START WITH 1, INCREMENT BY 1),		
    username varchar(50) NOT NULL UNIQUE DEFAULT '',
    password varchar(50) NOT NULL DEFAULT '',
    isadmin boolean not null default false,
    isaccepted integer not null default 0
);