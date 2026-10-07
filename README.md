# Estacionamento

Sistema de gerenciamento de estacionamento desenvolvido em Java, com interface gráfica em JavaFX e persistência de dados utilizando SQLite.

O projeto foi desenvolvido como um projeto de estudo e portfólio, com foco principalmente na construção do backend, aplicação de regras de negócio, persistência de dados e organização do código em camadas.

---

## Sobre o projeto

O sistema tem como objetivo controlar a entrada e saída de veículos de um estacionamento, mantendo um histórico das movimentações e permitindo que o operador acompanhe os veículos atualmente estacionados.

A aplicação funciona localmente e utiliza um banco de dados SQLite, permitindo que todos os dados sejam armazenados em um único arquivo `.db`, sem a necessidade de configurar um servidor de banco de dados.

A interface gráfica foi desenvolvida utilizando JavaFX.

---

## Funcionalidades

### Entrada de veículos

- Registro de veículos pela placa
- Validação da placa
- Registro automático do horário de entrada
- Identificação de veículos atualmente ativos
- Prevenção de múltiplas entradas simultâneas para a mesma placa

### Controle de veículos ativos

- Visualização dos veículos atualmente estacionados
- Consulta de veículo ativo pela placa
- Atualização da lista após novas entradas e saídas

### Saída de veículos

- Consulta do veículo ativo
- Registro do horário de saída
- Validação para impedir uma saída anterior à entrada
- Cálculo automático do valor da permanência
- Exibição do valor calculado
- Possibilidade de alteração manual do valor final pelo operador
- Finalização do estacionamento

### Persistência

- Banco de dados SQLite
- Persistência das entradas e saídas
- Armazenamento do valor calculado e do valor final
- Histórico de registros por placa

---

## Regra de cálculo

O sistema utiliza inicialmente uma regra de cobrança proporcional ao tempo de permanência.

Exemplo de configuração utilizada durante o desenvolvimento:

- Valor por hora: **R$ 10,00**
- Valor diário máximo: **R$ 20,00**

O valor por minuto é calculado a partir do valor da hora.

O sistema calcula automaticamente um `valorCalculado`, que serve como sugestão para o operador.

O `valorFinal` é independente e pode ser alterado manualmente antes da finalização da saída.

> A regra de preços utilizada atualmente é uma configuração inicial do projeto e poderá ser adaptada conforme as regras reais do estabelecimento.

---

## Arquitetura

O projeto utiliza uma arquitetura organizada em camadas, separando responsabilidades entre interface, regras de negócio e persistência.

```text
                    JavaFX
                      │
                      ▼
                  Service
                      │
                      ▼
                 Repository
                      │
                      ▼
                    SQLite
```

### Model

Responsável por representar os dados e comportamentos relacionados ao estacionamento.

```text
Estacionamento
```

Entre suas responsabilidades estão:

- Dados do veículo
- Entrada e saída
- Status
- Valores
- Cálculo do valor da permanência

### Service

Responsável pelas regras de negócio da aplicação.

```text
EstacionamentoService
```

Entre suas responsabilidades estão:

- Validar operações
- Impedir entrada duplicada
- Validar saída
- Calcular valores
- Coordenar operações entre a interface e o repositório

### Repository

Responsável pela comunicação com o banco de dados.

```text
EstacionamentoRepository
```

Responsável por operações como:

- Inserir registros
- Buscar veículos ativos
- Registrar saídas
- Consultar histórico
- Consultar registros por placa

### Database

Responsável pela conexão e inicialização do SQLite.

```text
DatabaseConnection
DatabaseInitializer
```

---

## Estrutura do projeto

```text
src/
└── main/
    └── java/
        └── br/
            └── com/
                └── estacionamento/
                    ├── Main.java
                    │
                    ├── database/
                    │   ├── DatabaseConnection.java
                    │   └── DatabaseInitializer.java
                    │
                    ├── model/
                    │   └── Estacionamento.java
                    │
                    ├── repository/
                    │   └── EstacionamentoRepository.java
                    │
                    └── service/
                        └── EstacionamentoService.java
```

---

## Tecnologias utilizadas

- **Java 17**
- **Maven**
- **JavaFX**
- **SQLite**
- **JDBC**
- **Git / GitHub**

---

## Como executar

### Pré-requisitos

É necessário ter instalado:

- JDK 17 ou superior
- Maven

### Clonar o projeto

```bash
git clone <URL_DO_REPOSITORIO>
```

### Entrar no projeto

```bash
cd estacionamento
```

### Executar

```bash
mvn javafx:run
```

Na primeira execução, o sistema cria automaticamente o banco de dados SQLite utilizado pela aplicação.

---

##  Banco de dados

O projeto utiliza SQLite para manter a aplicação simples e local.

O banco é armazenado em:

```text
estacionamento.db
```

Esse arquivo é criado localmente durante a execução e **não é versionado no Git**, pois cada instalação deve possuir sua própria base de dados.

---

##  Status do projeto

### Implementado

- [x] Configuração do projeto Maven
- [x] Integração com SQLite
- [x] Inicialização automática do banco
- [x] Modelagem da entidade de estacionamento
- [x] Validação de placa
- [x] Registro de entrada
- [x] Controle de veículos ativos
- [x] Prevenção de entrada duplicada
- [x] Registro de saída
- [x] Cálculo automático da permanência
- [x] Cálculo do valor
- [x] Edição manual do valor final
- [x] Interface gráfica com JavaFX
- [x] Integração entre JavaFX, Service e Repository
- [x] Persistência dos registros no SQLite
- [x] Histórico de registros

### Próximos passos

- [ ] Melhorar experiência e validações da interface
- [ ] Criar tela dedicada ao histórico
- [ ] Permitir configuração das tarifas pela aplicação
- [ ] Melhorar tratamento de erros
- [ ] Adicionar testes automatizados
- [ ] Melhorar documentação técnica
- [ ] Empacotar a aplicação para distribuição local

---

## 🎯 Objetivo do projeto

Este projeto foi desenvolvido como parte do processo de aprendizado e evolução em desenvolvimento de software.

O principal objetivo é aplicar, na prática, conceitos como:

- Programação Orientada a Objetos
- Separação de responsabilidades
- Arquitetura em camadas
- Regras de negócio
- Persistência de dados
- SQL
- JDBC
- Integração com banco de dados
- Desenvolvimento de interfaces gráficas
- Maven
- Git e GitHub

O projeto também serve como laboratório para experimentar decisões de arquitetura e transformar requisitos de um problema real em uma aplicação funcional.

---

## Nota sobre a interface

A interface gráfica foi desenvolvida em JavaFX como camada de apresentação do sistema.

O foco principal deste projeto está na implementação do backend, incluindo regras de negócio, persistência de dados, arquitetura em camadas e integração com SQLite.

A implementação visual da interface não representa o principal objetivo de aprendizado deste projeto.

---

## Disclaimer

Este projeto foi desenvolvido de forma incremental durante um processo de estudo, utilizando ferramentas de IA como apoio para pesquisa, revisão de código, discussão de arquitetura, resolução de problemas e exploração de conceitos.

Um agradecimento especial ao **Sodo**, assistente utilizado durante o desenvolvimento do projeto, que participou das discussões técnicas e ajudou a transformar dúvidas em etapas práticas de aprendizado.

> A IA foi utilizada como ferramenta de apoio ao desenvolvimento e aprendizado. As decisões, testes, validações e implementação do projeto fizeram parte do processo de estudo e evolução do desenvolvedor.

---

## Aprendizado

Mais do que simplesmente construir uma aplicação funcional, este projeto representa uma etapa de aprendizado prático em desenvolvimento Java.
