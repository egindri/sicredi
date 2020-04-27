## Solução

O projeto foi desenvolvido utilizando-se Java 8, SpringBoot2, H2 e Maven.
O servidor se encontra na porta 80.

Para executá-lo:

	mvn spring-boot:run

Fontes JPA são gerados automaticamente pelo plugin maven do hyperjaxb3 a partir do XSD do BCB.

Hibernate se encarrega da criação das tabelas.

Carga inicial é feita via SpringBatch.

Testes de integração realizados com SpringBootTest e SpringBatchTest.


Documentação REST:

	http://localhost/swagger-ui.html

## Objetivo

### SLC - Serviço de Liquidação Centralizada

O SLC - Serviço de Liquidação Centralizada - é o grupo de serviços operado pela CIP para processamento da compensação e da liquidação das ordens eletrônicas de crédito ou de débito entre instituições financeiras e/ou instituições de pagamento participantes de um mesmo arranjo de pagamento integrante do SPB, conforme dispõem os arts. 25 e 26 do Regulamento Anexo à Circular nº 3.682, de 24 de novembro de 2013, com as alterações da Circular nº 3.765, de 25 de setembro de 2015.

As mensagens deste grupo de serviços pertencem ao domínio de sistema SPB01. As mensagens informativas referentes a arquivos deste grupo de serviço (por exemplo, GEN0015) trafegarão no domínio de sistema MES01.

### Liquidação Financeira Multilateral

SLC0001 - SLC informa movimentos bilaterais de liquidação multilateral no dia

### O desafio

Consiste em criar uma rotina que irá carregar o arquivo SLC0001-modelo.xml que se encontra nesse repositório para um banco de dados e disponibilizar em uma API.