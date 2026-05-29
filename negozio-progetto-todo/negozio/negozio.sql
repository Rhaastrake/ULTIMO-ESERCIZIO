CREATE DATABASE IF NOT EXISTS negozio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE negozio;

CREATE TABLE IF NOT EXISTS prodotti (
    id        INT           AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100)  NOT NULL,
    categoria VARCHAR(50)   NOT NULL,
    prezzo    DECIMAL(8,2)  NOT NULL,
    quantita  INT           NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS clienti (
    id    INT          AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ordini (
    id                INT  AUTO_INCREMENT PRIMARY KEY,
    cliente_id        INT  NOT NULL,
    prodotto_id       INT  NOT NULL,
    quantita_ordinata INT  NOT NULL,
    data_ordine       DATE NOT NULL,
    FOREIGN KEY (cliente_id)  REFERENCES clienti(id),
    FOREIGN KEY (prodotto_id) REFERENCES prodotti(id)
);

INSERT INTO prodotti (nome, categoria, prezzo, quantita) VALUES
    ('Smartphone X12',      'elettronica',    499.99, 15),
    ('Cuffie Wireless Pro', 'elettronica',     89.90, 30),
    ('Tastiera Meccanica',  'elettronica',     69.50,  8),
    ('T-Shirt Basic',       'abbigliamento',   19.90, 50),
    ('Jeans Slim',          'abbigliamento',   59.90, 25),
    ('Felpa con Cappuccio', 'abbigliamento',   39.90,  3),
    ('Pasta di Semola 1kg', 'alimentari',       1.49, 100),
    ('Olio EVO 750ml',      'alimentari',       7.90,  2),
    ('Caffè Arabica 250g',  'alimentari',       5.50,  0);

INSERT INTO clienti (nome, email) VALUES
    ('Mario Rossi',    'mario.rossi@email.it'),
    ('Laura Bianchi',  'laura.bianchi@gmail.com'),
    ('Giovanni Verdi', 'g.verdi@outlook.com'),
    ('Sara Neri',      'sara.neri@libero.it');
