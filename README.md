# Sistema de Compras e Relatórios
Este é um sistema para gerenciamento de compras, produtos e geração de relatórios de compras por período. A aplicação utiliza Java com Spring Boot no backend, JPA para persistência de dados, e um banco de dados relacional (como H2 ou qualquer outro suportado pelo JPA).

## Funcionalidades
- **Cadastro de Compras:** Os usuários podem realizar compras, e a aplicação verifica se o limite máximo de itens por produto foi excedido.
- **Gerenciamento de Produtos:** Cadastro e busca de produtos no sistema.
- **Relatórios de Compras:** Geração de relatórios de compras por produto, filtrando por período e com suporte a paginação.
### Tecnologias Utilizadas
- Java 21
- Spring Boot 3.3.4
- Spring Data JPA
- H2 Database
- Lombok
- JUnit
- Mockito
- Swagger

### Requisitos
- **JDK 21**
- **Maven** para gerenciamento de dependências

### Configuração
#### 1. Clonar o repositório
```
git clone https://github.com/rafaelbraf/api-compras.git
cd api-compras
```
#### 2. Configurar o Banco de Dados
   Por padrão, o projeto está configurado para usar o H2 Database em memória para fins de desenvolvimento e teste.

#### 3. Executar a Aplicação
```
mvn spring-boot:run
```
A aplicação estará disponível em http://localhost:8080/api.

#### 4. Acessar o Console do H2 (se estiver utilizando H2)
   O console do H2 pode ser acessado em http://localhost:8080/api/h2-console. Use as seguintes credenciais para conectar:
```yaml
username: sa
password:
```

### Endpoints
Nesta seção, listamos os principais endpoints disponíveis na API.

Compras
- POST /compras: Cadastra uma nova compra
- GET /compras/search: Lista compras com filtro (opcional)
- GET /compras/relatorio: Gera um relatório de compras por produto com paginação
- Produtos
- POST /produtos: Cadastra um novo produto
- GET /produtos: Busca todos os produtos

#### Exemplos de Uso
Cadastrar um Produto
POST /produtos: 
```json
{
  "nome": "Produto A", 
  "valorUnitario": 100.0
}
```

Cadastrar uma Compra
POST /compras: 
```json
{
  "cpfComprador": "12345678900", 
  "itens": [
    {
      "produtoId": 1, 
      "quantidade": 2
    }
  ]
}
```

Gerar Relatório de Compras:
GET /compras/relatorio:
```text
Params:
dataInicio=2024-01-01&dataFim=2024-12-31&page=0&size=10
```


### Testes
#### Executar os Testes
Para rodar os testes, você pode usar o Maven:
```
mvn test
```

Os testes estão localizados no pacote src/test/java e utilizam JUnit 5 e Mockito para testar os serviços e repositórios.

### Documentação

#### Acessando o Swagger UI

Acesse o Swagger UI por meio da seguinte URL:

http://localhost:8080/api/