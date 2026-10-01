# meu-soap-javaee

Projeto pequeno e didático de backend Java com Web Service SOAP. Ele demonstra um CRUD mínimo de clientes com armazenamento em memória, sem servidor de aplicação externo e sem banco de dados.

## Pré-requisitos

- JDK 11 ou superior (este projeto foi compilado e validado com Java 17).
- Maven 3.6 ou superior.

## Compilar e executar

Na raiz do projeto:

```bash
mvn clean test
mvn exec:java
```

O servidor publica o serviço em `http://localhost:8080/servicos/clientes`. Mantenha o processo em execução para chamar o serviço. O contrato WSDL fica em:

```text
http://localhost:8080/servicos/clientes?wsdl
```

O processo pode ser encerrado com `Ctrl+C`. Os dados são apagados quando ele termina.

## Operações SOAP

- `criarCliente(nome, email)`: valida e armazena um cliente, devolvendo o registro criado.
- `buscarCliente(id)`: devolve o cliente correspondente ao identificador.
- `listarClientes()`: devolve os clientes cadastrados.

Os três exemplos completos de requisição e resposta estão em [`docs/exemplos-soap.xml`](docs/exemplos-soap.xml). Exemplo de chamada com `curl` (a resposta é XML SOAP):

```bash
curl -X POST http://localhost:8080/servicos/clientes \
  -H 'Content-Type: text/xml; charset=utf-8' \
  -H 'SOAPAction: ""' \
  --data-binary @- <<'XML'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:cli="http://soap.rfcarmo.com.br/cliente">
  <soapenv:Header/>
  <soapenv:Body>
    <cli:criarCliente>
      <nome>Ana Silva</nome>
      <email>ana@example.com</email>
    </cli:criarCliente>
  </soapenv:Body>
</soapenv:Envelope>
XML
```

Também é possível importar a URL do WSDL no SoapUI e enviar as operações pela interface.

## Organização e responsabilidade de cada arquivo

```text
pom.xml
src/main/java/br/com/rfcarmo/soap/
  Application.java
  endpoint/ClienteSoapEndpoint.java
  exception/ClienteNaoEncontradoException.java
  exception/DadosInvalidosException.java
  model/Cliente.java
  service/ClienteService.java
src/test/java/br/com/rfcarmo/soap/service/ClienteServiceTest.java
docs/exemplos-soap.xml
```

- `pom.xml`: descreve o projeto para o Maven, configura o Java 11, as dependências JAX-WS/JAXB necessárias após a remoção dessas APIs do JDK, o plugin de execução e o JUnit 5 para testes.
- `Application.java`: inicia o serviço standalone por `Endpoint.publish` e mantém a aplicação disponível até ser encerrada.
- `model/Cliente.java`: modelo de dados com construtor vazio e getters/setters exigidos pelo JAXB, que converte objetos Java em XML e vice-versa.
- `exception/ClienteNaoEncontradoException.java`: representa a consulta de um identificador inexistente.
- `exception/DadosInvalidosException.java`: representa dados de entrada que não passam pelas validações.
- `service/ClienteService.java`: concentra as regras de negócio, validação, geração concorrente de IDs e armazenamento em memória. Não depende de SOAP.
- `endpoint/ClienteSoapEndpoint.java`: adapta as chamadas SOAP às operações do serviço de negócio. `@WebService` define o serviço, `@WebMethod` publica operações e `@WebParam` nomeia os elementos de entrada.
- `ClienteServiceTest.java`: verifica regras e comportamento isolado do serviço, sem iniciar um servidor.
- `docs/exemplos-soap.xml`: contém envelopes SOAP de exemplo para as três operações e respostas ilustrativas.

## Como uma chamada funciona

1. O consumidor envia um envelope SOAP XML para a URL do serviço; a operação e seus parâmetros estão no `Body`.
2. O Metro/JAX-WS interpreta o XML e chama o método correspondente do endpoint.
3. O endpoint delega a regra ao `ClienteService`, que valida os dados e acessa o armazenamento em memória.
4. JAX-WS/JAXB converte o resultado Java em uma resposta SOAP XML. As exceções de negócio são declaradas como faults SOAP, sem incluir detalhes internos da implementação.

## Configurações e escolhas didáticas

O `pom.xml` declara Metro (`jaxws-rt`) como implementação JAX-WS e JUnit Jupiter para os testes. O código usa os pacotes `javax.*` da linha JAX-WS 2.3, compatível com Java 11; a publicação local dispensa instalação/configuração de WildFly, Payara ou outro servidor. O plugin Maven Exec permite iniciar a classe `Application` com `mvn exec:java`.

O serviço mantém os clientes apenas na memória do processo. O mapa concorrente e o contador atômico protegem operações simultâneas; os métodos devolvem cópias dos clientes/lista para que alterações externas não modifiquem o estado interno. Não há persistência: ao parar o processo, todos os dados são perdidos.

As falhas de validação e de busca são modeladas como faults SOAP didáticos (`DadosInvalidosException` e `ClienteNaoEncontradoException`). Em uma aplicação real, ainda seria necessário definir códigos/contratos de fault estáveis, registrar erros internamente e configurar autenticação/autorização e transporte HTTPS. Próximos passos naturais são extrair um repositório e substituir a memória por JPA/banco de dados, além de implementar segurança e observabilidade.

## Validar manualmente

1. Execute `mvn clean test` para compilar e rodar os testes unitários.
2. Execute `mvn exec:java` e abra `http://localhost:8080/servicos/clientes?wsdl`.
3. Envie os exemplos de `docs/exemplos-soap.xml` com SoapUI ou `curl`.
