CREATE DATABASE agendou;
USE agendou;

-- Visualizações de apoio
SELECT id_empresa, role FROM empresa;
SELECT id_usuario, role FROM usuario;
select * from empresa;
select * from usuario;
select * from servico;
select * from agendamento;
select * from historico_agendamento;

-- Usuario
CREATE USER 'darkklua'@'localhost' IDENTIFIED BY 'lua';
GRANT ALL PRIVILEGES ON agendou.* TO 'darkklua'@'localhost';
FLUSH PRIVILEGES;
SELECT user, host FROM mysql.user WHERE user = 'darkklua';

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(60) NOT NULL,
    telefone VARCHAR(11) NOT NULL,
    email VARCHAR(45) NOT NULL,
    senha VARCHAR(300) NOT NULL,
    role VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS empresa (
    id_empresa INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(60) NOT NULL,
    representante VARCHAR(60) NOT NULL,
    telefone VARCHAR(11) NOT NULL,
    email VARCHAR(45) NOT NULL,
    senha VARCHAR(300) NOT NULL,
    cnpj VARCHAR(45) NOT NULL,
    role VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS servico (
    id_servico INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(60) NOT NULL,
    descricao VARCHAR(100),
    preco DECIMAL(10,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS agendamento (
    id_agendamento INT PRIMARY KEY AUTO_INCREMENT,
    fk_empresa INT NOT NULL,
    fk_usuario INT NOT NULL,
    data DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descricao VARCHAR(100),
    status ENUM('AGENDADO', 'CANCELADO', 'REALIZADO', 'ALTERADO', 'ADIADO') DEFAULT 'AGENDADO',
    fk_servico INT NOT NULL,
    FOREIGN KEY (fk_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (fk_servico) REFERENCES servico(id_servico) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS historico_agendamento (
    id_historico INT PRIMARY KEY AUTO_INCREMENT,
    data DATETIME NOT NULL,
    status_anterior ENUM('AGENDADO', 'CANCELADO', 'REALIZADO', 'ALTERADO', 'ADIADO') NOT NULL,
    status_atual ENUM('AGENDADO', 'CANCELADO', 'REALIZADO', 'ALTERADO', 'ADIADO') NOT NULL,
    fk_agendamento INT NOT NULL,
    fk_empresa INT NOT NULL,
    fk_usuario INT NOT NULL,
    FOREIGN KEY (fk_agendamento) REFERENCES agendamento(id_agendamento) ON DELETE CASCADE,
    FOREIGN KEY (fk_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inserts mocks
INSERT INTO empresa (nome, representante, telefone, email, senha, cnpj, role) VALUES
('Barbearia Estilo', 'Lucas Silva', '11999999999', 'estilo@empresa.com', 'senha123', '12.345.678/0001-00', 'ADMIN'),
('Barber Top', 'Ana Costa', '11888888888', 'top@empresa.com', 'senha123', '98.765.432/0001-99', 'ADMIN');

INSERT INTO usuario (nome, telefone, email, senha, role) VALUES
('João Pedro', '11911112222', 'joao@email.com', 'senha123', 'USER'),
('Carlos Moura', '11933334444', 'carlos@email.com', 'senha123', 'USER'),
('Maria Souza', '11955556666', 'maria@email.com', 'senha123', 'USER');

INSERT INTO servico (nome, descricao, preco) VALUES
('Corte Masculino', 'Corte com máquina e tesoura', 30.00),
('Barba', 'Barba na navalha', 20.00),
('Hidratação Capilar', 'Tratamento de hidratação profunda', 40.00),
('Coloração', 'Coloração e corte americano', 50.00);

-- Agendamento mock
INSERT INTO agendamento (fk_empresa, fk_usuario, data, descricao, fk_servico) VALUES
(1, 1, '2025-12-31 15:30:00', 'Coloração e corte americano', 4);

-- Histórico relacionado
INSERT INTO historico_agendamento (data, status_anterior, status_atual, fk_agendamento, fk_empresa, fk_usuario) VALUES
('2025-12-31 14:59:00', 'AGENDADO', 'REALIZADO', 1, 1, 1);