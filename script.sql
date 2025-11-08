-- ================================================
-- CRIAÇÃO DAS TABELAS
-- ================================================

CREATE TABLE Cinema (
    id_cinema SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    localizacao VARCHAR(200),
    tipo_estabelecimento VARCHAR(50),
    numero_salas INT
);

CREATE TABLE Sala (
    num_sala SERIAL PRIMARY KEY,
    capacidade INT,
    tipo_som VARCHAR(50),
    formato_exibicao VARCHAR(20),
    tecnologia VARCHAR(50),
    tipo_sala VARCHAR(50),
    id_cinema INT,
    FOREIGN KEY (id_cinema) REFERENCES Cinema(id_cinema)
);

CREATE TABLE Filme (
    id_filme SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    duracao INT,
    genero VARCHAR(50),
    elenco TEXT,
    direcao VARCHAR(100),
    ano_producao INT
);

CREATE TABLE Cliente (
    id_cliente SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    rua VARCHAR(100),
    numero VARCHAR(10),
    cidade VARCHAR(80),
    bairro VARCHAR(80),
    cep VARCHAR(10)
);

CREATE TABLE Sessao (
    id_sessao SERIAL PRIMARY KEY,
    data_inicio TIMESTAMP,
    data_fim TIMESTAMP,
    preco DECIMAL(6,2),
    hora TIME,
    idioma VARCHAR(50),
    id_sala INT,
    id_filme INT,
    FOREIGN KEY (id_sala) REFERENCES Sala(num_sala),
    FOREIGN KEY (id_filme) REFERENCES Filme(id_filme)
);

-- Tabela "Questões" 
CREATE TABLE Criterio (
    id_criterio SERIAL PRIMARY KEY,
    nome_criterio VARCHAR(100),
    descricao TEXT
);

CREATE TABLE Avaliacao (
    id_avaliacao SERIAL PRIMARY KEY,
    data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comentario TEXT,
    comentario_anonimo BOOLEAN DEFAULT FALSE,
    nota_geral NUMERIC(2,1),
    id_cliente INT NOT NULL,
    id_sessao INT NOT NULL, -- Ligado diretamente à Sessao
    
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_sessao) REFERENCES Sessao(id_sessao),
    
    -- Garante que um cliente só pode avaliar uma sessão específica uma única vez 
    CONSTRAINT unica_avaliacao UNIQUE (id_cliente, id_sessao)
);

-- Tabela "Respostas" (Ligação N-N entre Avaliacao e Criterio) 
CREATE TABLE Avaliacao_Criterio (
    id_avaliacao_criterio SERIAL PRIMARY KEY,
    nota NUMERIC(2,1),
    id_avaliacao INT NOT NULL,
    id_criterio INT NOT NULL,
    FOREIGN KEY (id_avaliacao) REFERENCES Avaliacao(id_avaliacao),
    FOREIGN KEY (id_criterio) REFERENCES Criterio(id_criterio)
);


-- ================================================
-- POVOAMENTO (INSERTS DE DADOS DE EXEMPLO)
-- ================================================

-- Cinemas
INSERT INTO Cinema (nome, localizacao, tipo_estabelecimento, numero_salas) VALUES
('Cine Plaza', 'Av. Ayrton Senna, 100 - Shopping Plaza', 'shopping', 8),
('Cine Centro', 'Rua Principal, 123 - Centro', 'cinema de rua', 1);

-- Salas (Ligadas aos Cinemas 1 e 2) 
INSERT INTO Sala (capacidade, tipo_som, formato_exibicao, tecnologia, tipo_sala, id_cinema) VALUES
(250, 'Dolby Atmos', '3D', 'IMAX', 'VIP', 1),
(150, 'Dolby 7.1', '2D', 'Standard', 'Convencional', 1),
(100, 'Estéreo', '2D', 'Standard', 'Convencional', 2);

-- Filmes 
INSERT INTO Filme (titulo, duracao, genero, elenco, direcao, ano_producao) VALUES
('O Segredo do Abismo', 171, 'Ficção Científica', 'Ed Harris, Mary Elizabeth Mastrantonio', 'James Cameron', 1989),
('Duna: Parte 2', 166, 'Ficção Científica', 'Timothée Chalamet, Zendaya', 'Denis Villeneuve', 2024),
('Godfather', 175, 'Crime/Drama', 'Marlon Brando, Al Pacino', 'Francis Ford Coppola', 1972);

-- Clientes
INSERT INTO Cliente (nome, cpf, telefone, rua, numero, cidade, bairro, cep) VALUES
('Ana Silva', '11122233344', '4399991111', 'Rua das Flores', '10', 'Londrina', 'Centro', '86000001'),
('Bruno Costa', '22233344455', '4399992222', 'Av. dos Pinheiros', '200', 'Londrina', 'Gleba', '86000002');

-- Sessões (Ligadas às Salas 1, 2, 3 e Filmes 1, 2, 3)
INSERT INTO Sessao (data_inicio, data_fim, preco, hora, idioma, id_sala, id_filme) VALUES
-- Duna (ID 2) na Sala 1 (IMAX)
('2025-10-25 20:00:00', '2025-10-25 22:46:00', 35.00, '20:00', 'Legendado', 1, 2), 
-- O Padrinho (ID 3) na Sala 2
('2025-10-25 19:30:00', '2025-10-25 22:25:00', 28.00, '19:30', 'Dublado', 2, 3),    
-- Duna (ID 2) na Sala 1 (outra sessão)
('2025-10-26 15:00:00', '2025-10-26 17:41:00', 30.00, '15:00', 'Legendado', 1, 2); 

-- Critérios (As "Questões")
-- (Mantém os teus 5 critérios originais, que terão os IDs 1 a 5)
INSERT INTO Criterio (nome_criterio, descricao) VALUES
('Qualidade de Imagem', 'Nível de definição, brilho e cor da projeção.'),
('Qualidade de Som', 'Clareza, volume e imersão do sistema de som.'),
('Conforto das Poltronas', 'Espaço, reclinação e conforto geral dos assentos.'),
('Limpeza da Sala', 'Limpeza geral da sala, incluindo poltronas e chão.'),
('Atendimento', 'Cortesia e eficiência dos funcionários.'),
('Temperatura da Sala (Ar Cond.)', 'Conforto térmico da sala durante a sessão.'),
('Facilidade de Estacionamento', 'Disponibilidade, preço e acesso ao estacionamento.'),
('Qualidade da Bomboniere', 'Variedade, preço e qualidade da lanchonete.'),
('Variedade de Horários', 'Disponibilidade de horários convenientes para as sessões.'),
('Mix Dublado/Legendado', 'Satisfação com a oferta de sessões dubladas vs. legendadas.');


-- Avaliação (Cliente 1 [Ana] avaliando a Sessão 1 [Duna])
-- (Ajustei o comentário e a nota geral para refletir as novas perguntas)
INSERT INTO Avaliacao (data_avaliacao, comentario, comentario_anonimo, nota_geral, id_cliente, id_sessao) VALUES
('2025-10-25 23:00:00', 'O som IMAX é incrível, mas a sala estava fria e a bomboniere cara.', false, 3.8, 1, 1);


-- Respostas da Avaliação (Notas por Critério, do exemplo)
-- Respostas para os 5 critérios originais
INSERT INTO Avaliacao_Criterio (nota, id_avaliacao, id_criterio) VALUES
(5.0, 1, 1), -- Resposta para 'Qualidade de Imagem' (ID 1)
(5.0, 1, 2), -- Resposta para 'Qualidade de Som' (ID 2)
(4.0, 1, 3), -- Resposta para 'Conforto das Poltronas' (ID 3)
(3.0, 1, 4), -- Resposta para 'Limpeza da Sala' (ID 4)
(4.0, 1, 5), -- Resposta para 'Atendimento' (ID 5)
(2.5, 1, 6), -- Resposta para 'Temperatura da Sala (Ar Cond.)' (ID 6)
(3.0, 1, 7), -- Resposta para 'Facilidade de Estacionamento' (ID 7)
(2.0, 1, 8), -- Resposta para 'Qualidade da Bomboniere' (ID 8)
(5.0, 1, 9), -- Resposta para 'Variedade de Horários' (ID 9)
(4.5, 1, 10);-- Resposta para 'Mix Dublado/Legendado' (ID 10)