package com.muralis.api_cliente.Controller;

import com.muralis.api_cliente.DTO.ClienteDTO;
import com.muralis.api_cliente.Model.ClienteModel;
import com.muralis.api_cliente.Model.ContatoModel;
import com.muralis.api_cliente.Repositories.ClienteRepository;
import com.muralis.api_cliente.Repositories.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ContatoRepository contatoRepository;

    @GetMapping
    public List<ClienteModel> listar() {

        return clienteRepository.findAll();

    }

    @GetMapping("/{id}")
    public Optional<ClienteModel> pesquisa(@PathVariable Long id) {

        return clienteRepository.findById(id);
    }

    @PostMapping
    public ClienteModel cadastrar(@RequestBody ClienteDTO cliente) {

        if (cliente.getNome() != null && cliente.getDatacadastro() != null && cliente.getTexto() != null && cliente.getTipo() != null) {

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
            return clienteRepository.save(cli);

        } else {
            return null;
        }

    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {

        clienteRepository.deleteById(id);

    }

    @PutMapping("/{id}")
    public ClienteModel atualizar(@PathVariable Long id,@RequestBody ClienteDTO cliente) {

        ClienteModel cli = clienteRepository.getReferenceById(id);
        cli.setNome(cliente.getNome());
        if (!cliente.getTexto().equals(cli.getContatoid().getTexto()) || !cliente.getTipo().equals(cli.getContatoid().getTipo())) {

            long idcontato = cli.getContatoid().getId();
            String tipo = cliente.getTipo();
            String texto = cliente.getTexto();
            ContatoModel con = new ContatoModel();
            con.setTipo(tipo);
            con.setTexto(texto);
            contatoRepository.save(con);
            cli.setContatoid(con);
            /// Deletando o antigo contato já que a relação é 1 para 1 e ele não será mais usado
            contatoRepository.deleteById(idcontato);

        }



        return clienteRepository.save(cli);


    }


}