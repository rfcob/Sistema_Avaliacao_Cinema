-- =======================================================
-- BANCO DE DADOS: Sistema de Avaliação de Cinemas (MySQL)
-- =======================================================
CREATE DATABASE IF NOT EXISTS sistema_avaliacao_cinema
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE sistema_avaliacao_cinema;

CREATE TABLE Cinema (
  id_cinema INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  localizacao VARCHAR(200),
  tipo_estabelecimento VARCHAR(50),
  numero_salas INT
);

CREATE TABLE Sala (
  num_sala INT AUTO_INCREMENT PRIMARY KEY,
  capacidade INT,
  tipo_som VARCHAR(50),
  formato_exibicao VARCHAR(20),
  tecnologia VARCHAR(50),
  tipo_sala VARCHAR(50),
  id_cinema INT,
  FOREIGN KEY (id_cinema) REFERENCES Cinema(id_cinema)
);

CREATE TABLE Filme (
  id_filme INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  duracao INT,
  genero VARCHAR(50),
  elenco TEXT,
  direcao VARCHAR(100),
  ano_producao INT
);

CREATE TABLE Sessao (
  id_sessao INT AUTO_INCREMENT PRIMARY KEY,
  data_inicio DATETIME,
  data_fim DATETIME,
  preco DECIMAL(6,2),
  hora TIME,
  idioma VARCHAR(50),
  id_sala INT,
  id_filme INT,
  FOREIGN KEY (id_sala) REFERENCES Sala(num_sala),
  FOREIGN KEY (id_filme) REFERENCES Filme(id_filme)
);

CREATE TABLE Cliente (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  cpf VARCHAR(14) UNIQUE,
  telefone VARCHAR(20),
  rua VARCHAR(100),
  numero VARCHAR(10),
  cidade VARCHAR(80),
  bairro VARCHAR(80),
  cep VARCHAR(10)
);

CREATE TABLE Item_Avaliado (
  id_item INT AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(30) NOT NULL,
  id_referencia INT NOT NULL,
  UNIQUE (tipo, id_referencia)
);

CREATE TABLE Avaliacao (
  id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
  data_avaliacao DATETIME DEFAULT CURRENT_TIMESTAMP,
  comentario TEXT,
  comentario_anonimo BOOLEAN DEFAULT FALSE,
  nota_geral DECIMAL(2,1),
  id_cliente INT NOT NULL,
  id_item INT NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
  FOREIGN KEY (id_item) REFERENCES Item_Avaliado(id_item),
  UNIQUE (id_cliente, id_item)
);

CREATE TABLE Criterio (
  id_criterio INT AUTO_INCREMENT PRIMARY KEY,
  nome_criterio VARCHAR(100),
  descricao TEXT
);

CREATE TABLE Avaliacao_Criterio (
  id_avaliacao_criterio INT AUTO_INCREMENT PRIMARY KEY,
  nota DECIMAL(2,1),
  id_avaliacao INT NOT NULL,
  id_criterio INT NOT NULL,
  FOREIGN KEY (id_avaliacao) REFERENCES Avaliacao(id_avaliacao),
  FOREIGN KEY (id_criterio) REFERENCES Criterio(id_criterio)
);
