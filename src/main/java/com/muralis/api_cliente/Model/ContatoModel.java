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
