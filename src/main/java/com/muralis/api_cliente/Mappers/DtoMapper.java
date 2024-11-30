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
