# API-RESTFUL CRUD CLIENTE COM JAVA SPRING BOOT

## DESCRIÇÃO
Projeto realizado utilizando java spring boot com intenção de criar uma API-Restful capaz de realizar as funções básicas de um CRUD (criar, ler, deletar e atualizar).

## Dependências utilizadas
![print1](https://github.com/user-attachments/assets/c08c233d-5cbe-47a3-8831-e525be308be3)

* Spring web: para criação de projetos web
* Spring Boot Dev Tools: para melhorias no processo de desenvolvimento
* Lombok: Para diminuição de códigos padrões e uma codigicação mais limpa
* H2 Database: Banco em memória para praticidade nos testes.
* Spring Data JPA: Para interações com o banco de dados de forma facilitada

## Aplicações utilizadas
* IntelliJ para construção do projeto
* Postman para testes de requisições

## Requisitos do teste

* Ser uma API-RESTFUL
* Utilização de mapper
* Utilização de DTO

## Código e explicações

A primeira etapa é realizar a análise do diagrama de classe proposto:

![print2](https://github.com/user-attachments/assets/aa7d7d1f-6a24-412f-81d4-fb7d7cdacea9)

Observa-se uma relação de cardinalidade entre as entidades Cliente e Contato, com dependência por parte da entidade contato, que irá existir somente caso a entidade Cliente exista. 
Como não houve uma especificação de quantidade na cardinalidade, foi adotado como base a forma um para um, deste modo, um Cliente irá possuir apenas um contato, e um contato
irá possuir apenas um cliente.

### Criação das entidades

```java
package com.muralis.api_cliente.Model;

import jakarta.persistence.*;

import lombok.Data;


@Entity
@Data
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String datacadastro;

    @Column
    private String nome;

    @OneToOne
    @JoinColumn(name = "ID_CONTATO")
    private ContatoModel contatoid;


}
```

```java
package com.muralis.api_cliente.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContatoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String texto;

}
```

![print3](https://github.com/user-attachments/assets/c58749b2-9fef-4460-b561-5560687bf656)

Após a criação das entidades, foi realizada a criação das classes de repositório, que serão responsáveis pela interação com o banco de dados

### Criação dos repositórios

```java
package com.muralis.api_cliente.Repositories;

import com.muralis.api_cliente.Model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

}

```

```java
package com.muralis.api_cliente.Repositories;

import com.muralis.api_cliente.Model.ContatoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoModel, Long> {
}


```
![print4](https://github.com/user-attachments/assets/67dcda7e-5b55-4f66-884d-143db74f89dc)

Com a criação dos repositórios feita, a próxima etapa foi a criação da entidade DTO, que será a classe responsável por intermediar o transporte dos dados entre as classes do projeto.

### Criação do DTO

```java
package com.muralis.api_cliente.DTO;

import lombok.Data;

@Data
public class ClienteDTO {

    private String nome;
    private String tipo;
    private String texto;
    private String datacadastro;

}

```

![print5](https://github.com/user-attachments/assets/20b00f62-52e2-4d86-b1c1-9ce08c50ce33)

Juntamente com a criação do DTO, também é feita a criação das classes Mappers, que possuem a função de converter os dados da classe DTO para estruturas em que a classe de serviço possa trabalhar.
Como por exemplo: uma função de cadastro de cliente espera receber um objeto da classe Cliente, a classe mapper será responsável por converter o objeto ClienteDTO para um objeto Cliente.

### Criação dos mappers

```java
package com.muralis.api_cliente.Mappers;

import com.muralis.api_cliente.DTO.ClienteDTO;
import com.muralis.api_cliente.Model.ClienteModel;
import com.muralis.api_cliente.Model.ContatoModel;
import com.muralis.api_cliente.Repositories.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteMapper {

    @Autowired
    private ContatoRepository contatoRepository;

    public ClienteModel convert(ClienteDTO cliente) {
        String tipo = cliente.getTipo();
        String texto = cliente.getTexto();
        ContatoModel con = new ContatoModel();
        con.setTipo(tipo);
        con.setTexto(texto);
        contatoRepository.save(con);

        String nome = cliente.getNome();
        String data = cliente.getDatacadastro();
        ClienteModel cli = new ClienteModel();
        cli.setNome(nome);
        cli.setDatacadastro(data);
        cli.setContatoid(con);

        return cli;
    }
}


```

```java
package com.muralis.api_cliente.Mappers;

import com.muralis.api_cliente.DTO.ClienteDTO;
import com.muralis.api_cliente.Model.ClienteModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DtoMapper {

    public ClienteDTO convert(ClienteModel cliente) {

        ClienteDTO cli = new ClienteDTO();
        cli.setNome(cliente.getNome());
        cli.setDatacadastro(cliente.getDatacadastro());
        cli.setTexto(cliente.getContatoid().getTexto());
        cli.setTipo(cliente.getContatoid().getTipo());

        return cli;

    }

}

```
![print6](https://github.com/user-attachments/assets/4dccd49b-e569-4863-81ee-42b554616097)

Com todas as classes anteriores criadas, agora é possível realizar a codificação da classe de serviço, onde estarão armazenadas as funções que realizam as tarefas de criação, salvamento,
leitura, remoção e atualização dos dados.

Durante a criação das funções, foi utilizado a técnica de Try Catch para o tratamento das possíveis exceções. 

### Classe de serviço

```java
package com.muralis.api_cliente.Service;

import com.muralis.api_cliente.DTO.ClienteDTO;
import com.muralis.api_cliente.Mappers.ClienteMapper;
import com.muralis.api_cliente.Mappers.DtoMapper;
import com.muralis.api_cliente.Model.ClienteModel;
import com.muralis.api_cliente.Model.ContatoModel;
import com.muralis.api_cliente.Repositories.ClienteRepository;
import com.muralis.api_cliente.Repositories.ContatoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private DtoMapper dtoMapper;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ContatoRepository contatoRepository;

    public List<ClienteModel> listar() {
        return clienteRepository.findAll();
    }

    public String pesquisa(Long id) {

        try {
            ClienteModel cliente = clienteRepository.findById(id).get();
            ClienteDTO cli = dtoMapper.convert(cliente);
            String res = "Nome do cliente: " + cli.getNome() + "\nData do cadastro: " + cli.getDatacadastro() +
                    "\nInformações de contato:  \nTipo: " + cli.getTipo() + "\n" + cli.getTexto();
            return res;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String cadastrar(ClienteDTO cliente) {

        if (cliente.getNome() != null && cliente.getDatacadastro() != null && cliente.getTexto() != null && cliente.getTipo() != null) {

            ClienteModel cli = clienteMapper.convert(cliente);
            clienteRepository.save(cli);
            return "Cadastrado com sucesso!";

        } else {
            return "Houve um erro ao tentar cadastrar o cliente!";
        }

    }

    public String remover( Long id) {

        try {
            ClienteModel cliente = clienteRepository.findById(id).get();

        } catch (Exception e) {
            return "Este cliente não existe!";
        }

        clienteRepository.deleteById(id);
        return "Removido com sucesso!";


    }

    public String atualizar(Long id, ClienteDTO cliente) {

        try {
            ClienteModel cli = clienteRepository.getReferenceById(id);
            cli.setNome(cliente.getNome());
            if (!cliente.getTexto().equals(cli.getContatoid().getTexto()) || !cliente.getTipo().equals(cli.getContatoid().getTipo())) {

                long idcontato = cli.getContatoid().getId();
                String tipo = cliente.getTipo();
                String texto = cliente.getTexto();

                ContatoModel con = contatoRepository.getReferenceById(idcontato);
                con.setTipo(tipo);
                con.setTexto(texto);
                contatoRepository.save(con);
                cli.setContatoid(con);

            }

            clienteRepository.save(cli);
            return "Atualizado com sucesso!";
        } catch (Exception e) {
            return "Houve um erro ao tentar atualizar o cliente!";
        }
    }

}



```
![print7](https://github.com/user-attachments/assets/3a495aca-2f60-4612-b94b-9034283655d9)

E por fim, a última etapa, onde acontece a criação da classe de controle, responsável por intermediar a comunicação entre a parte externa e as classes de serviço, direcionando os dados recebidos 
para o caminho certo dentro da aplicação, e adicionando uma camada de segurança para que o usuário não tenha contato com informações do sistema.

### Classe controller

```java
package com.muralis.api_cliente.Controller;

import com.muralis.api_cliente.DTO.ClienteDTO;
import com.muralis.api_cliente.Model.ClienteModel;
import com.muralis.api_cliente.Model.ContatoModel;
import com.muralis.api_cliente.Repositories.ClienteRepository;
import com.muralis.api_cliente.Repositories.ContatoRepository;
import com.muralis.api_cliente.Service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cliente")
public class ClienteController {


    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;
    private final ContatoRepository contatoRepository;

    @GetMapping
    public ResponseEntity<List<ClienteModel>> listar() {

            return ResponseEntity.ok(clienteService.listar());

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> pesquisa(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(clienteService.pesquisa(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody ClienteDTO cliente) {

        return ResponseEntity.ok(clienteService.cadastrar(cliente));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Long id) {


        return ResponseEntity.ok(clienteService.remover(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id,@RequestBody ClienteDTO cliente) {

        String res = clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(res);

    }


}

```

## Resultados

### Criação de um cliente

![print8](https://github.com/user-attachments/assets/a90304e1-70d5-4f28-a9b5-a570dc8475a9)

### Listagem de clientes cadastrados

![print9](https://github.com/user-attachments/assets/78b12c90-538a-4fd5-805c-d281e320a386)

### Pesquisando por um ID específico 

![print10](https://github.com/user-attachments/assets/fd95e301-d764-4d4b-ba8c-3f678c852116)

### Realizando uma alteração no cliente Kevin, trocando seu nome, tipo de contato e texto do contato

![print11](https://github.com/user-attachments/assets/3e98f037-556b-4faf-9a74-f5d1a9a3bb74)

### Verificando na listagem que o cliente de ID 2 Kevin sofreu as alterações

![print12](https://github.com/user-attachments/assets/9393ef62-0eed-43bd-afb5-57af7c8c89dc)

### Removendo o cliente joao

![print13](https://github.com/user-attachments/assets/d170a253-dc9e-4b4a-8459-2c70b57592f2)

### Verificando na listagem a remoção

![print14](https://github.com/user-attachments/assets/ddb7656f-aa16-4168-a3be-3444e628259a)
