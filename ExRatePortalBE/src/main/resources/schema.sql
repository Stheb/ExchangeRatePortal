DROP TABLE IF EXISTS CURRENCY;
DROP TABLE IF EXISTS EXCHANGERATE;

CREATE TABLE CURRENCY (
    ID int AUTO_INCREMENT PRIMARY KEY,
    CODE varChar(3) NOT NULL UNIQUE,
    NAME varChar(255) NOT NULL
);

CREATE TABLE EXCHANGERATE (
    ID int AUTO_INCREMENT PRIMARY KEY,
    CODE varChar(3) NOT NULL UNIQUE,
    RATE double NOT NULL,
    DATE varChar(12) NOT NULL
);