CREATE DATABASE IF NOT EXISTS ospedale

USE ospedale;

CREATE TABLE pazienti (
  id    INT          AUTO_INCREMENT PRIMARY KEY,
  nome  VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE medici (
    id                 INT          AUTO_INCREMENT PRIMARY KEY,
    nome               VARCHAR(100) NOT NULL,
    specializzazione   VARCHAR(100) NOT NULL
);

CREATE TABLE visite (
    id           INT  AUTO_INCREMENT PRIMARY KEY,
    paziente_id  INT  NOT NULL,
    medico_id    INT  NOT NULL,
    data_visita  DATE NOT NULL,
    descrizione  TEXT NOT NULL,
    FOREIGN KEY (paziente_id) REFERENCES pazienti(id),
    FOREIGN KEY (medico_id)   REFERENCES medici(id)
);

INSERT INTO medici (nome, specializzazione) VALUES
    ('Marco Ferretti',   'cardiologia'),
    ('Laura Ricci',      'cardiologia'),
    ('Giovanni Esposito','ortopedia'),
    ('Sara Mancini',     'ortopedia'),
    ('Paolo Conti',      'neurologia'),
    ('Anna Greco',       'pediatria');

INSERT INTO pazienti (nome, email) VALUES
    ('Mario Rossi',    'mario.rossi@email.it'),
    ('Laura Bianchi',  'laura.bianchi@gmail.com'),
    ('Giovanni Verdi', 'g.verdi@outlook.com'),
    ('Sara Neri',      'sara.neri@libero.it');
