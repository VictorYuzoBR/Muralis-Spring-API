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
