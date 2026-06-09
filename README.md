Trabalho Pratico: Concorrencia e Consistencia em Banco de Dados

Integrantes e Responsabilidades

  - Hyago Fernando Ferreira Rodrigues: Responsavel pela Parte 1. Implementou a entidade Conta sem
    controle de concorrencia, evidenciando o problema de Atualizacao Perdida
    (Lost Update).
  - Edson Davi Farias Martins: Responsavel pela Parte 2. Implementou a entidade
    ContaVersionada utilizando @Version, tratando o erro de conflito para
    garantir a integridade do saldo.

Instrucoes para Execucao

1.  Rodar a Aplicacao: Abra o terminal na pasta raiz do projeto e execute:

    ./mvnw spring-boot:run

    A aplicacao estara disponivel em http://localhost:8080.

2.  Acessar o Banco de Dados (H2 Console):

      - URL: http://localhost:8080/h2-console
      - JDBC URL: jdbc:h2:mem:testdb
      - Usuario: sa
      - Senha: (vazio)

3. Dados Iniciais (H2 Console)

      Para popular o banco de dados antes de iniciar os testes no JMeter:

      Com a aplicação rodando, acesse: http://localhost:8080/h2-console

      JDBC URL: jdbc:h2:mem:testdb | Usuário: sa | Senha: (vazio)

      Abra o arquivo src/main/resources/import.sql no seu projeto.

      Copie o conteúdo do arquivo, cole na área de comandos do H2 Console e clique em Run.

Plano de Testes (JMeter)

1.  Abra o JMeter.
2.  Carregue o arquivo teste_concorrencia.jmx localizado na raiz do projeto.
3.  Configure o Thread Group para 10 threads.
4.  Teste Aluno A: Aponte para o endpoint POST /contas/1/deposito.
5.  Teste Aluno B: Aponte para o endpoint POST /contas-versionadas/1/deposito.

Relatorio de Resultados

Parte 1: Sem Bloqueio

  - Observacao: O JMeter mostra todas as requisicoes com sucesso (Status 200).
  - Resultado: O saldo final no banco de dados fica incorreto. Como nao ha
    controle, uma operacao sobrescreve a outra (Lost Update).

Parte 2: Com @Version

  - Observacao: O JMeter mostra diversas falhas com Status 409 (Conflict).
  - Resultado: O saldo final no banco de dados permanece correto e consistente
    com o numero de operacoes que tiveram sucesso. O controle de versao impediu
    a gravacao de dados obsoletos.

Conclusao

A utilizacao da anotacao @Version do JPA/Hibernate soluciona o problema da
Atualizacao Perdida ao validar a versao do registro antes da persistencia,
garantindo a consistencia dos dados em ambientes com alta concorrencia.
