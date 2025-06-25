CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON empresa.* TO 'root'@'localhost';
FLUSH PRIVILEGES;

-- Tabela Pessoa: armazena dados básicos dos indivíduos.
CREATE TABLE IF NOT EXISTS pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Tabela Funcionario: dados específicos de funcionário.
CREATE TABLE IF NOT EXISTS funcionario (
    id_pessoa INT PRIMARY KEY,
    matricula VARCHAR(10) NOT NULL UNIQUE,
    departamento VARCHAR(50) NOT NULL,
    CONSTRAINT fk_funcionario_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa(id) ON DELETE CASCADE
);

-- Tabela Projeto: armazena os projetos e os vincula a um funcionário responsável.
CREATE TABLE IF NOT EXISTS projeto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    id_funcionario INT NOT NULL,
    CONSTRAINT fk_projeto_funcionario FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_pessoa)
);

-- Pessoas
INSERT INTO pessoa (nome, email) VALUES
('Ana Silva', 'ana.silva@email.com'),
('Bruno Costa', 'bruno.costa@email.com'),
('Carla Dias', 'carla.dias@email.com'),
('Daniel Farias', 'daniel.farias@email.com'),
('Elisa Martins', 'elisa.martins@email.com'),
('Fábio Gomes', 'fabio.gomes@email.com'),
('Gisele Alves', 'gisele.alves@email.com'),
('Hugo Lima', 'hugo.lima@email.com'),
('Isabela Rocha', 'isabela.rocha@email.com'),
('João Mendes', 'joao.mendes@email.com'),
('Laura Santos', 'laura.santos@email.com');

-- Funcionários
INSERT INTO funcionario (id_pessoa, matricula, departamento) VALUES
(1, 'F001', 'TI'),
(2, 'F002', 'RH'),
(3, 'F003', 'Marketing'),
(4, 'F004', 'TI'),
(5, 'F005', 'Financeiro'),
(6, 'F006', 'Vendas'),
(7, 'F007', 'TI'),
(8, 'F008', 'Marketing'),
(9, 'F009', 'RH'),
(10, 'F010', 'Financeiro');

-- Projetos
INSERT INTO projeto (nome, descricao, id_funcionario) VALUES
('Sistema de Gestão Interna', 'Desenvolvimento de um novo ERP para a empresa.', 1),
('Campanha de Marketing Digital Q3', 'Planejamento e execução da campanha para o terceiro trimestre.', 3),
('Onboarding de Novos Talentos', 'Criação do processo de integração de novos colaboradores.', 2),
('Migração para Cloud AWS', 'Mover a infraestrutura atual para a nuvem da AWS.', 4),
('Análise de Risco Financeiro', 'Auditoria e análise dos riscos financeiros do último ano.', 5),
('Expansão de Mercado LATAM', 'Estudo de viabilidade para expansão de vendas na América Latina.', 6),
('App Mobile para Clientes', 'Desenvolvimento de um aplicativo para iOS e Android.', 7),
('Rebranding da Marca', 'Modernização da identidade visual da empresa.', 8),
('Programa de Bem-Estar', 'Implementação de um programa de saúde mental para funcionários.', 9),
('Otimização de Custos', 'Projeto para reduzir custos operacionais em 15%.', 10),
('Refatoração do Legado', 'Melhorar o código do sistema de faturamento antigo.', 1);