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