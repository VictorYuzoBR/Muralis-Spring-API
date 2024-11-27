package com.muralis.api_cliente.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

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
