DROP TABLE dvertovs_meteo;
DROP TABLE dvertovs_prognoza;
DROP TABLE dvertovs_adrese;
DROP TABLE dvertovs_dnevnik_WS;
DROP TABLE dvertovs_users;
DROP TABLE dvertovs_usertype;

CREATE TABLE dvertovs_usertype (
    idusertype integer NOT NULL PRIMARY KEY,		
    usertype varchar(50)
);

INSERT INTO dvertovs_usertype(idusertype, usertype) VALUES (1, 'admin'), (2, 'normal');

CREATE TABLE dvertovs_users (
    iduser integer NOT NULL 
                PRIMARY KEY auto_increment,		
    username varchar(50) NOT NULL UNIQUE DEFAULT '',
    password varchar(50) NOT NULL DEFAULT '',
    idusertype integer NOT NULL,
    category integer NOT NULL DEFAULT 1,
    requests integer NOT NULL DEFAULT 0,
    FOREIGN KEY(idusertype) REFERENCES dvertovs_usertype(idusertype)
);

insert into dvertovs_users(username, password, idusertype) values('dvertovs', 'a', 1);

CREATE TABLE dvertovs_adrese (
  idadresa integer NOT NULL 
                PRIMARY KEY auto_increment,
  adresa varchar(255) NOT NULL DEFAULT '' UNIQUE,
  latitude varchar(25) NOT NULL DEFAULT '',
  longitude varchar(25) NOT NULL DEFAULT '',
  iduser integer NOT NULL,
  FOREIGN KEY(iduser) REFERENCES dvertovs_users(iduser)
);

CREATE TABLE dvertovs_meteo (
  idmeteo integer NOT NULL 
                PRIMARY KEY auto_increment,
  idadresa integer NOT NULL,				
  vrijemeopis varchar(200) NOT NULL DEFAULT '',
  temp float NOT NULL DEFAULT -999,
  tempmin float NOT NULL DEFAULT -999,
  tempmax float NOT NULL DEFAULT -999,
  vlaga float NOT NULL DEFAULT -999,
  tlak float NOT NULL DEFAULT -999,
  vjetar float NOT NULL DEFAULT -999,
  vjetarsmjer float NOT NULL DEFAULT -999,
  vrijemeazuriranjastanice timestamp NOT NULL,
  preuzeto timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (idadresa) REFERENCES dvertovs_adrese (idadresa)
);

CREATE TABLE dvertovs_dnevnik_WS (
    iduser INTEGER NOT NULL,
    vrsta VARCHAR(50) NOT NULL DEFAULT '',
    naziv Varchar(50) NOT NULL DEFAULT '',
    vrijeme timestamp NOT NULL DEFAULT current_timestamp,
    FOREIGN KEY(iduser) REFERENCES dvertovs_users(iduser)
);

CREATE TABLE dvertovs_prognoza (
        idadresa integer NOT NULL,
        vrijemeopis varchar(25) NOT NULL DEFAULT '',	
	temp float NOT NULL DEFAULT -999,
	vlaga float NOT NULL DEFAULT -999,
	tlak float NOT NULL DEFAULT -999,
	vrijemeprognoze timestamp NOT NULL,
	preuzeto timestamp NOT NULL,
        FOREIGN KEY (idadresa) REFERENCES dvertovs_adrese (idadresa)
);