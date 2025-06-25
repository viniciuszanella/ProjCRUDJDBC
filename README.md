# Projeto CRUD com Java e JDBC

Aplicação Java para gestão de Pessoas, Funcionários e Projetos, utilizando JDBC puro para persistência de dados.

## ✨ Tecnologias Utilizadas

*   **Java 11**
*   **Maven** - Gerenciador de dependências
*   **MySQL** - Banco de dados relacional
*   **JDBC (Java Database Connectivity)** - Para comunicação com o banco de dados
*   **Lombok** - Para reduzir código (getters, setters, etc.)

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para configurar e rodar a aplicação em seu ambiente local.

### 1. Pré-requisitos

*   [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) ou superior
*   [Apache Maven](https://maven.apache.org/download.cgi)
*   [MySQL Server](https://dev.mysql.com/downloads/mysql/)

### 2. Configuração do Banco de Dados

1.  Inicie seu serviço do MySQL.
2.  Execute o script SQL contido no arquivo `empresa.sql` (na raiz do projeto).

### 3. Execução da Aplicação

1.  Clone este repositório:
    ```bash
    git clone https://github.com/viniciuszanella/ProjCRUDJDBC.git
    ```

2.  Navegue até o diretório do projeto:
    ```bash
    cd ProjCRUDJDBC
    ```

3.  Compile o projeto e baixe as dependências com o Maven:
    ```bash
    mvn clean install
    ```

4.  Execute a classe principal `Main.java` através da sua IDE (IntelliJ, Eclipse, etc.). O console da IDE exibirá os resultados dos testes das operações e regras de negócio.
