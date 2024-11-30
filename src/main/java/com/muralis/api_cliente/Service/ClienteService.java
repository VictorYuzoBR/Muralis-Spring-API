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
